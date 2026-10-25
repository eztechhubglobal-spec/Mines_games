package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DangerRed
import com.example.model.DoubleColor
import com.example.service.OverlayStateManager
import com.example.ui.theme.DoubleBlack
import com.example.ui.theme.DoubleGreen
import com.example.ui.theme.DoubleRed
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
fun DoubleScreen(viewModel: AssistantViewModel) {
    val doubleAnalysis by OverlayStateManager.doubleAnalysis.collectAsState()
    val bankrollNgn by OverlayStateManager.bankrollNgn.collectAsState()
    val currentStakeNgn by OverlayStateManager.currentStakeNgn.collectAsState()

    val totalSpins = doubleAnalysis.rollHistory.size

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
                        text = "1WIN DOUBLE WHEEL",
                        color = EmeraldPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Drum Multiplier & Color Streak Engine",
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
                        text = "$totalSpins Logged",
                        color = GoldAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Live Log Controls (Record Spin Outcome)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianStroke)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "RECORD OBSERVED DRUM RESULT",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.addDoubleRoll(DoubleColor.RED) },
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp)
                                .testTag("record_red_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = DoubleRed),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("RED", color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Black)
                                Text("x2 (46.7%)", color = TextWhite.copy(alpha = 0.8f), fontSize = 9.sp)
                            }
                        }

                        Button(
                            onClick = { viewModel.addDoubleRoll(DoubleColor.BLACK) },
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp)
                                .testTag("record_black_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = DoubleBlack),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianStroke)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("BLACK", color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Black)
                                Text("x2 (46.7%)", color = TextWhite.copy(alpha = 0.8f), fontSize = 9.sp)
                            }
                        }

                        Button(
                            onClick = { viewModel.addDoubleRoll(DoubleColor.LUCKY) },
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp)
                                .testTag("record_lucky_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = DoubleGreen),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("LUCKY", color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Black)
                                Text("x14 (6.7%)", color = TextWhite.copy(alpha = 0.8f), fontSize = 9.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Recent History Pills Strip
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Recent Rolls:", color = TextMuted, fontSize = 10.sp)
                        Text(
                            text = "Clear History",
                            color = TextMuted,
                            fontSize = 10.sp,
                            modifier = Modifier.clickable { viewModel.clearDoubleRolls() }
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    if (doubleAnalysis.rollHistory.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(ObsidianBackground, RoundedCornerShape(8.dp))
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No rolls logged. Tap Red, Black, or Lucky above.", color = TextMuted, fontSize = 11.sp)
                        }
                    } else {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(ObsidianBackground, RoundedCornerShape(8.dp))
                                .padding(8.dp)
                        ) {
                            items(doubleAnalysis.rollHistory) { roll ->
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(roll.color.color),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = when (roll.color) {
                                            DoubleColor.RED -> "R"
                                            DoubleColor.BLACK -> "B"
                                            DoubleColor.LUCKY -> "14"
                                        },
                                        color = TextWhite,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Statistical Distribution Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianStroke)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "OBSERVED VS THEORETICAL DISTRIBUTION (15 SLOTS)",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    ColorDistributionRow(
                        name = "Red (x2)",
                        color = DoubleRed,
                        observedCount = doubleAnalysis.redCount,
                        total = totalSpins,
                        observedProb = doubleAnalysis.observedRedProb,
                        theoreticalProb = DoubleColor.RED.theoreticalProb
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ColorDistributionRow(
                        name = "Black (x2)",
                        color = DoubleBlack,
                        observedCount = doubleAnalysis.blackCount,
                        total = totalSpins,
                        observedProb = doubleAnalysis.observedBlackProb,
                        theoreticalProb = DoubleColor.BLACK.theoreticalProb
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ColorDistributionRow(
                        name = "Lucky (x14)",
                        color = DoubleGreen,
                        observedCount = doubleAnalysis.luckyCount,
                        total = totalSpins,
                        observedProb = doubleAnalysis.observedLuckyProb,
                        theoreticalProb = DoubleColor.LUCKY.theoreticalProb
                    )
                }
            }
        }

        // Streak & Variance Analysis Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianStroke)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.TrendingUp, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Streak & Variance Analytics", color = GoldAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (doubleAnalysis.currentStreakLength > 0 && doubleAnalysis.currentStreakColor != null) {
                        val colorName = doubleAnalysis.currentStreakColor?.title ?: ""
                        val streakP = Math.pow(
                            (doubleAnalysis.currentStreakColor?.theoreticalProb ?: 0.4667f).toDouble(),
                            doubleAnalysis.currentStreakLength.toDouble()
                        )
                        Text(
                            text = "Current Streak: ${doubleAnalysis.currentStreakLength} consecutive $colorName",
                            color = TextWhite,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Binomial probability of occurrence: ${String.format("%.3f%%", streakP * 100)}",
                            color = EmeraldPrimary,
                            fontSize = 11.sp
                        )
                    } else {
                        Text(text = "No active streak detected.", color = TextMuted, fontSize = 11.sp)
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Rounds since Lucky (x14): ${doubleAnalysis.roundsSinceLucky}",
                        color = TextWhite,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        color = ObsidianBackground,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = doubleAnalysis.statisticalAdvice,
                            color = TextMuted,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(8.dp),
                            lineHeight = 14.sp
                        )
                    }
                }
            }
        }

        // Quick Wager Logger (Record Profit/Loss)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianStroke)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "QUICK ROUND WAGER LOGGING",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Active Stake: ₦${String.format("%,.0f", currentStakeNgn)}",
                        color = TextWhite,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                viewModel.logDoubleOutcome(DoubleColor.RED, true, currentStakeNgn)
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = SafeGreen),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Won 2x (₦${String.format("%,.0f", currentStakeNgn * 2)})", color = ObsidianBackground, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                viewModel.logDoubleOutcome(DoubleColor.RED, false, currentStakeNgn)
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = DangerRed),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Lost Round (-₦${String.format("%,.0f", currentStakeNgn)})", color = TextWhite, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ColorDistributionRow(
    name: String,
    color: androidx.compose.ui.graphics.Color,
    observedCount: Int,
    total: Int,
    observedProb: Float,
    theoreticalProb: Float
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(name, color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            Text(
                text = "Observed: $observedCount (${if (total > 0) String.format("%.1f%%", observedProb * 100) else "0.0%"}) | Math: ${String.format("%.1f%%", theoreticalProb * 100)}",
                color = TextMuted,
                fontSize = 10.sp
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        LinearProgressIndicator(
            progress = { if (total > 0) observedProb else theoreticalProb },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp)),
            color = color,
            trackColor = ObsidianBackground
        )
    }
}
