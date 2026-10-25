package com.example.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.TileStatus
import com.example.service.OverlayStateManager
import com.example.ui.theme.DangerRed
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.ObsidianBackground
import com.example.ui.theme.ObsidianStroke
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceVariant
import com.example.ui.theme.SafeGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.viewmodel.AssistantViewModel

@Composable
fun MinesScreen(viewModel: AssistantViewModel) {
    val minesState by OverlayStateManager.minesState.collectAsState()
    val mineCount by OverlayStateManager.mineCount.collectAsState()
    val stakeNgn by OverlayStateManager.currentStakeNgn.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "1WIN MINES SIMULATOR",
                        color = EmeraldPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "5x5 Grid Combinatorics & Safe Path Engine",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }

                Surface(
                    color = ObsidianSurface,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianStroke)
                ) {
                    Text(
                        text = "Stake: ₦${String.format("%,.0f", stakeNgn)}",
                        color = GoldAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Live Odds & Multiplier Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianStroke)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("NEXT SAFE PROBABILITY", color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = "${String.format("%.2f", minesState.currentStepSafeProb * 100)}%",
                                color = minesState.riskLevel.color,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("CUMULATIVE SURVIVAL", color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = "${String.format("%.2f", minesState.cumulativeSurvivalProb * 100)}%",
                                color = if (minesState.cumulativeSurvivalProb > 0.5f) SafeGreen else GoldAccent,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text("CURRENT MULTIPLIER", color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = "${String.format("%.2f", minesState.currentMultiplier)}x",
                                color = EmeraldPrimary,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { minesState.currentStepSafeProb },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = minesState.riskLevel.color,
                        trackColor = ObsidianBackground
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Next: ${String.format("%.2f", minesState.nextMultiplier)}x | Payout: ₦${String.format("%,.0f", minesState.potentialPayoutNgn)}",
                            color = TextWhite,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = minesState.riskLevel.title,
                            color = minesState.riskLevel.color,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Mine Count Controller
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianStroke)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "MINE COUNT ON BOARD",
                            color = TextMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "$mineCount Mines (${25 - mineCount} Gems)",
                            color = DangerRed,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    Slider(
                        value = mineCount.toFloat(),
                        onValueChange = { viewModel.setMineCount(it.toInt()) },
                        valueRange = 1f..24f,
                        steps = 22,
                        colors = SliderDefaults.colors(
                            thumbColor = DangerRed,
                            activeTrackColor = DangerRed,
                            inactiveTrackColor = ObsidianBackground
                        ),
                        modifier = Modifier.testTag("mine_count_slider")
                    )

                    // Quick Mine Presets
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(1, 2, 3, 5, 7, 10, 15).forEach { count ->
                            MinePresetChip(
                                count = count,
                                isSelected = mineCount == count,
                                onClick = { viewModel.setMineCount(count) }
                            )
                        }
                    }
                }
            }
        }

        // 5x5 Mines Board
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianStroke)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "5X5 INTERACTIVE GRID MAP",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Tap = Uncover Gem | Long Press = Flag Mine",
                        color = EmeraldPrimary,
                        fontSize = 10.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(ObsidianBackground, RoundedCornerShape(14.dp))
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            for (row in 0 until 5) {
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    for (col in 0 until 5) {
                                        val idx = row * 5 + col
                                        val tile = minesState.tiles.getOrNull(idx)
                                        MinesTileCell(
                                            tile = tile,
                                            onToggle = { viewModel.toggleTile(idx) },
                                            onLongToggle = { viewModel.toggleFlag(idx) }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Round Outcome Actions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                viewModel.logMinesCashout(
                                    stake = stakeNgn,
                                    multiplier = minesState.currentMultiplier,
                                    safeCount = minesState.openedSafeCount
                                )
                            },
                            enabled = minesState.openedSafeCount > 0,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("cashout_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = SafeGreen),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = ObsidianBackground, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Cash Out", color = ObsidianBackground, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                viewModel.logMinesLoss(
                                    stake = stakeNgn,
                                    safeCount = minesState.openedSafeCount
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("detonate_loss_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = DangerRed),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = null, tint = TextWhite, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Hit Mine", color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = { viewModel.resetMines() },
                            modifier = Modifier
                                .weight(0.8f)
                                .testTag("reset_mines_button"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextWhite)
                        ) {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Reset", fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // Mathematical Combinatorics Proof Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianBackground),
                border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianStroke)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Calculate, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Rigorous Mathematical Breakdown", color = EmeraldPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = minesState.mathematicalProof,
                        color = TextWhite,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Formula: P(safe_k) = (25 - M - k) / (25 - k). In a truly random provably fair game, all unrevealed tiles possess identical prior probability. Recommendations highlight optimal dispersed opening strategies.",
                        color = TextMuted,
                        fontSize = 9.sp,
                        lineHeight = 13.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun MinePresetChip(
    count: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bg = if (isSelected) DangerRed else ObsidianSurfaceVariant
    val textColor = if (isSelected) TextWhite else TextMuted
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bg)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "$count", color = textColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MinesTileCell(
    tile: com.example.model.TileInfo?,
    onToggle: () -> Unit,
    onLongToggle: () -> Unit
) {
    if (tile == null) return

    val bg = when (tile.status) {
        TileStatus.SAFE_GEM -> EmeraldContainer
        TileStatus.FLAGGED_MINE -> DangerRed.copy(alpha = 0.85f)
        TileStatus.SUGGESTED_SAFE -> EmeraldPrimary.copy(alpha = 0.22f)
        TileStatus.HIGH_RISK_MINE -> DangerRed.copy(alpha = 0.3f)
        TileStatus.UNOPENED -> ObsidianSurfaceVariant
    }

    val border = when {
        tile.isRecommended -> EmeraldPrimary
        tile.status == TileStatus.SAFE_GEM -> SafeGreen
        tile.status == TileStatus.FLAGGED_MINE -> DangerRed
        else -> ObsidianStroke
    }

    Box(
        modifier = Modifier
            .size(54.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(bg)
            .border(if (tile.isRecommended) 2.dp else 1.dp, border, RoundedCornerShape(8.dp))
            .combinedClickable(
                onClick = onToggle,
                onLongClick = onLongToggle
            ),
        contentAlignment = Alignment.Center
    ) {
        when (tile.status) {
            TileStatus.SAFE_GEM -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "💎", fontSize = 18.sp)
                    Text(text = "SAFE", color = SafeGreen, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                }
            }
            TileStatus.FLAGGED_MINE -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "💣", fontSize = 18.sp)
                    Text(text = "MINE", color = TextWhite, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                }
            }
            TileStatus.SUGGESTED_SAFE -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "★", color = EmeraldPrimary, fontSize = 14.sp)
                    Text(text = "${(tile.safeProbability * 100).toInt()}%", color = EmeraldPrimary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }
            else -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "${tile.index + 1}", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text(text = "${(tile.safeProbability * 100).toInt()}%", color = TextMuted.copy(alpha = 0.6f), fontSize = 8.sp)
                }
            }
        }
    }
}
