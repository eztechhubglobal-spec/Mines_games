package com.example.service

import com.example.model.DoubleAnalysis
import com.example.model.DoubleColor
import com.example.model.DoubleRoll
import com.example.model.GameType
import com.example.model.MinesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object OverlayStateManager {
    const val DEFAULT_TARGET_URL = "https://1win.ng/casino/play/v_1wingames:double?p=x8kz"

    private val _isServiceRunning = MutableStateFlow(false)
    val isServiceRunning: StateFlow<Boolean> = _isServiceRunning.asStateFlow()

    private val _isMinimized = MutableStateFlow(false)
    val isMinimized: StateFlow<Boolean> = _isMinimized.asStateFlow()

    private val _isPaused = MutableStateFlow(false)
    val isPaused: StateFlow<Boolean> = _isPaused.asStateFlow()

    private val _targetUrl = MutableStateFlow(DEFAULT_TARGET_URL)
    val targetUrl: StateFlow<String> = _targetUrl.asStateFlow()

    private val _activeGameType = MutableStateFlow(GameType.detectFromUrl(DEFAULT_TARGET_URL))
    val activeGameType: StateFlow<GameType> = _activeGameType.asStateFlow()

    private val _bankrollNgn = MutableStateFlow(10000.0)
    val bankrollNgn: StateFlow<Double> = _bankrollNgn.asStateFlow()

    private val _currentStakeNgn = MutableStateFlow(1000.0)
    val currentStakeNgn: StateFlow<Double> = _currentStakeNgn.asStateFlow()

    // Mines State Management
    private val _openedSafeIndices = MutableStateFlow<Set<Int>>(emptySet())
    val openedSafeIndices: StateFlow<Set<Int>> = _openedSafeIndices.asStateFlow()

    private val _flaggedMineIndices = MutableStateFlow<Set<Int>>(emptySet())
    val flaggedMineIndices: StateFlow<Set<Int>> = _flaggedMineIndices.asStateFlow()

    private val _mineCount = MutableStateFlow(3)
    val mineCount: StateFlow<Int> = _mineCount.asStateFlow()

    private val _minesState = MutableStateFlow(
        MinesState.calculateCombinatorics(3, emptySet(), emptySet(), 1000.0)
    )
    val minesState: StateFlow<MinesState> = _minesState.asStateFlow()

    // Double Drum State Management
    private val _doubleRolls = MutableStateFlow<List<DoubleRoll>>(emptyList())
    val doubleRolls: StateFlow<List<DoubleRoll>> = _doubleRolls.asStateFlow()

    private val _doubleAnalysis = MutableStateFlow(DoubleAnalysis())
    val doubleAnalysis: StateFlow<DoubleAnalysis> = _doubleAnalysis.asStateFlow()

    // Vision / screen capture status message
    private val _screenAnalysisMessage = MutableStateFlow<String?>(null)
    val screenAnalysisMessage: StateFlow<String?> = _screenAnalysisMessage.asStateFlow()

    fun setServiceRunning(running: Boolean) {
        _isServiceRunning.value = running
    }

    fun setMinimized(minimized: Boolean) {
        _isMinimized.value = minimized
    }

    fun toggleMinimized() {
        _isMinimized.value = !_isMinimized.value
    }

    fun setPaused(paused: Boolean) {
        _isPaused.value = paused
    }

    fun togglePaused() {
        _isPaused.value = !_isPaused.value
    }

    fun updateTargetUrl(url: String) {
        _targetUrl.value = url
        val detected = GameType.detectFromUrl(url)
        _activeGameType.value = detected
    }

    fun setActiveGameType(type: GameType) {
        _activeGameType.value = type
    }

    fun updateBankroll(amount: Double) {
        _bankrollNgn.value = amount.coerceAtLeast(0.0)
    }

    fun updateStake(amount: Double) {
        _currentStakeNgn.value = amount.coerceAtLeast(100.0)
        recalculateMines()
    }

    fun setMineCount(count: Int) {
        _mineCount.value = count.coerceIn(1, 24)
        recalculateMines()
    }

    fun toggleTileOpened(index: Int) {
        val currentOpened = _openedSafeIndices.value.toMutableSet()
        val currentFlagged = _flaggedMineIndices.value.toMutableSet()

        if (currentOpened.contains(index)) {
            currentOpened.remove(index)
        } else {
            currentFlagged.remove(index)
            currentOpened.add(index)
        }

        _openedSafeIndices.value = currentOpened
        _flaggedMineIndices.value = currentFlagged
        recalculateMines()
    }

    fun toggleTileFlagged(index: Int) {
        val currentOpened = _openedSafeIndices.value.toMutableSet()
        val currentFlagged = _flaggedMineIndices.value.toMutableSet()

        if (currentFlagged.contains(index)) {
            currentFlagged.remove(index)
        } else {
            currentOpened.remove(index)
            currentFlagged.add(index)
        }

        _openedSafeIndices.value = currentOpened
        _flaggedMineIndices.value = currentFlagged
        recalculateMines()
    }

    fun resetMinesBoard() {
        _openedSafeIndices.value = emptySet()
        _flaggedMineIndices.value = emptySet()
        recalculateMines()
    }

    private fun recalculateMines() {
        _minesState.value = MinesState.calculateCombinatorics(
            mineCount = _mineCount.value,
            openedSafeIndices = _openedSafeIndices.value,
            flaggedMineIndices = _flaggedMineIndices.value,
            stakeNgn = _currentStakeNgn.value
        )
    }

    // Double Roll Controls
    fun addDoubleRoll(color: DoubleColor) {
        val updated = listOf(DoubleRoll(color = color)) + _doubleRolls.value.take(49)
        _doubleRolls.value = updated
        _doubleAnalysis.value = DoubleAnalysis.analyze(updated)
    }

    fun clearDoubleRolls() {
        _doubleRolls.value = emptyList()
        _doubleAnalysis.value = DoubleAnalysis()
    }

    fun triggerScreenAnalysis() {
        _screenAnalysisMessage.value = "Scanning Chrome game canvas... Analyzing visible grid elements & multipliers."
    }

    fun clearScreenAnalysisMessage() {
        _screenAnalysisMessage.value = null
    }
}
