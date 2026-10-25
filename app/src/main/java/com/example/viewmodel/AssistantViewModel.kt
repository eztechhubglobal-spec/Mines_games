package com.example.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.MinesAiApp
import com.example.data.AnalysisRecordEntity
import com.example.model.DoubleColor
import com.example.model.GameType
import com.example.service.FloatingOverlayService
import com.example.service.OverlayStateManager
import com.example.util.ProvablyFairUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AssistantViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = (application as MinesAiApp).database.analysisDao()

    val analysisHistory: StateFlow<List<AnalysisRecordEntity>> = dao.getAllRecords()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    // Provably fair verification state
    private val _serverSeedInput = MutableStateFlow("")
    val serverSeedInput: StateFlow<String> = _serverSeedInput.asStateFlow()

    private val _clientSeedInput = MutableStateFlow("")
    val clientSeedInput: StateFlow<String> = _clientSeedInput.asStateFlow()

    private val _nonceInput = MutableStateFlow("1")
    val nonceInput: StateFlow<String> = _nonceInput.asStateFlow()

    private val _expectedHashInput = MutableStateFlow("")
    val expectedHashInput: StateFlow<String> = _expectedHashInput.asStateFlow()

    private val _verificationResult = MutableStateFlow<ProvablyFairUtil.VerificationResult?>(null)
    val verificationResult: StateFlow<ProvablyFairUtil.VerificationResult?> = _verificationResult.asStateFlow()

    fun selectTab(index: Int) {
        _selectedTab.value = index
    }

    fun updateUrl(url: String) {
        OverlayStateManager.updateTargetUrl(url)
    }

    fun setGameType(type: GameType) {
        OverlayStateManager.setActiveGameType(type)
    }

    fun setMineCount(count: Int) {
        OverlayStateManager.setMineCount(count)
    }

    fun toggleTile(index: Int) {
        OverlayStateManager.toggleTileOpened(index)
    }

    fun toggleFlag(index: Int) {
        OverlayStateManager.toggleTileFlagged(index)
    }

    fun resetMines() {
        OverlayStateManager.resetMinesBoard()
    }

    fun addDoubleRoll(color: DoubleColor) {
        OverlayStateManager.addDoubleRoll(color)
    }

    fun clearDoubleRolls() {
        OverlayStateManager.clearDoubleRolls()
    }

    fun updateStake(stake: Double) {
        OverlayStateManager.updateStake(stake)
    }

    fun updateBankroll(bankroll: Double) {
        OverlayStateManager.updateBankroll(bankroll)
    }

    fun toggleOverlayService(context: Context) {
        if (!Settings.canDrawOverlays(context)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${context.packageName}")
            ).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
            return
        }

        if (OverlayStateManager.isServiceRunning.value) {
            FloatingOverlayService.stop(context)
        } else {
            FloatingOverlayService.start(context)
        }
    }

    fun launchGameInChrome(context: Context, targetUrl: String) {
        // Automatically start the overlay first so it appears on top of Chrome
        if (Settings.canDrawOverlays(context) && !OverlayStateManager.isServiceRunning.value) {
            FloatingOverlayService.start(context)
        }

        val parsedUri = Uri.parse(targetUrl.ifBlank { OverlayStateManager.DEFAULT_TARGET_URL })
        val chromeIntent = Intent(Intent.ACTION_VIEW, parsedUri).apply {
            setPackage("com.android.chrome")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        try {
            context.startActivity(chromeIntent)
        } catch (e: Exception) {
            // Fall back to general browser if Chrome is not installed
            val genericIntent = Intent(Intent.ACTION_VIEW, parsedUri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            try {
                context.startActivity(genericIntent)
            } catch (err: Exception) {
                err.printStackTrace()
            }
        }
    }

    fun logMinesCashout(stake: Double, multiplier: Double, safeCount: Int) {
        viewModelScope.launch {
            val payout = stake * multiplier
            val profit = payout - stake
            dao.insertRecord(
                AnalysisRecordEntity(
                    gameType = "1Win Mines",
                    stakeNgn = stake,
                    multiplierResult = multiplier,
                    payoutNgn = payout,
                    safePicksCount = safeCount,
                    riskRating = OverlayStateManager.minesState.value.riskLevel.title,
                    outcomeType = "CASHOUT",
                    notes = "Cashed out at ${String.format("%.2f", multiplier)}x with $safeCount safe diamonds. Net: ₦${String.format("%,.0f", profit)}"
                )
            )
            OverlayStateManager.updateBankroll(OverlayStateManager.bankrollNgn.value + profit)
            OverlayStateManager.resetMinesBoard()
        }
    }

    fun logMinesLoss(stake: Double, safeCount: Int) {
        viewModelScope.launch {
            dao.insertRecord(
                AnalysisRecordEntity(
                    gameType = "1Win Mines",
                    stakeNgn = stake,
                    multiplierResult = 0.0,
                    payoutNgn = 0.0,
                    safePicksCount = safeCount,
                    riskRating = OverlayStateManager.minesState.value.riskLevel.title,
                    outcomeType = "MINE_HIT",
                    notes = "Detonated mine on pick #${safeCount + 1}. Loss: ₦${String.format("%,.0f", stake)}"
                )
            )
            OverlayStateManager.updateBankroll(OverlayStateManager.bankrollNgn.value - stake)
            OverlayStateManager.resetMinesBoard()
        }
    }

    fun logDoubleOutcome(color: DoubleColor, won: Boolean, stake: Double) {
        viewModelScope.launch {
            val mult = if (won) color.multiplier else 0.0
            val payout = if (won) stake * mult else 0.0
            val profit = if (won) payout - stake else -stake
            dao.insertRecord(
                AnalysisRecordEntity(
                    gameType = "1Win Double",
                    stakeNgn = stake,
                    multiplierResult = mult,
                    payoutNgn = payout,
                    safePicksCount = 1,
                    riskRating = if (color == DoubleColor.LUCKY) "High" else "Moderate",
                    outcomeType = if (won) "DOUBLE_WIN" else "DOUBLE_LOSS",
                    notes = "Bet on ${color.title}. Result: ${if (won) "Won ₦${String.format("%,.0f", payout)}" else "Lost ₦${String.format("%,.0f", stake)}"}"
                )
            )
            OverlayStateManager.updateBankroll(OverlayStateManager.bankrollNgn.value + profit)
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            dao.clearAll()
        }
    }

    // Provably fair verification actions
    fun updateServerSeed(seed: String) { _serverSeedInput.value = seed }
    fun updateClientSeed(seed: String) { _clientSeedInput.value = seed }
    fun updateNonce(nonce: String) { _nonceInput.value = nonce }
    fun updateExpectedHash(hash: String) { _expectedHashInput.value = hash }

    fun verifyProvablyFair() {
        _verificationResult.value = ProvablyFairUtil.verifyRound(
            serverSeed = _serverSeedInput.value,
            clientSeed = _clientSeedInput.value,
            nonce = _nonceInput.value,
            expectedHash = _expectedHashInput.value
        )
    }
}
