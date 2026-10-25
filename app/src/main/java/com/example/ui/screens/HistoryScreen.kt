package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AnalysisRecordEntity
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(viewModel: AssistantViewModel) {
    val history by viewModel.analysisHistory.collectAsState()

    val totalRounds = history.size
    val totalWonRounds = history.count { it.multiplierResult > 0.0 }
    val winRate = if (totalRounds > 0) (totalWonRounds.toFloat() / totalRounds.toFloat()) * 100f else 0f
    val totalStake = history.sumOf { it.stakeNgn }
    val totalPayout = history.sumOf { it.payoutNgn }
    val netProfit = totalPayout - totalStake

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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "SESSION HISTORY",
                            color = EmeraldPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Performance Metrics & Wager Logs",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }
                }

                if (history.isNotEmpty()) {
                    OutlinedButton(
                        onClick = { viewModel.clearAllHistory() },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = DangerRed),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("clear_history_button")
                    ) {
                        Icon(imageVector = Icons.Default.DeleteOutline, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Clear", fontSize = 10.sp)
                    }
                }
            }
        }

        // Summary Performance KPI Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianStroke)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "ANALYTICS SUMMARY",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Total Rounds", color = TextMuted, fontSize = 9.sp)
                            Text("$totalRounds", color = TextWhite, fontSize = 18.sp, fontWeight = FontWeight.Black)
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Win Rate", color = TextMuted, fontSize = 9.sp)
                            Text("${String.format("%.1f", winRate)}%", color = if (winRate >= 50f) SafeGreen else GoldAccent, fontSize = 18.sp, fontWeight = FontWeight.Black)
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text("Net Profit / Loss", color = TextMuted, fontSize = 9.sp)
                            Text(
                                text = "${if (netProfit >= 0) "+₦" else "-₦"}${String.format("%,.0f", Math.abs(netProfit))}",
                                color = if (netProfit >= 0) SafeGreen else DangerRed,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
            }
        }

        // Responsible Gaming Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianBackground),
                border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianStroke)
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                    Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("Responsible Gaming & Nigerian Regulations", color = GoldAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text(
                            text = "Gambling is 18+. Only bet what you can afford to lose. If gambling is causing distress, call the GambleAlert Nigeria support line at 0904 400 0000.",
                            color = TextMuted,
                            fontSize = 9.sp,
                            lineHeight = 13.sp
                        )
                    }
                }
            }
        }

        // History Log Items
        if (history.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No recorded sessions yet.\nUse the Mines Simulator or Double Tracker to log rounds.",
                        color = TextMuted,
                        fontSize = 12.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        } else {
            items(history) { record ->
                HistoryItemCard(record = record)
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun HistoryItemCard(record: AnalysisRecordEntity) {
    val isWin = record.multiplierResult > 0.0
    val net = record.payoutNgn - record.stakeNgn
    val dateFormat = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault())
    val dateStr = dateFormat.format(Date(record.timestamp))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ObsidianSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianStroke)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = if (isWin) EmeraldContainer else DangerRed.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = record.gameType,
                            color = if (isWin) EmeraldPrimary else DangerRed,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = dateStr, color = TextMuted, fontSize = 10.sp)
                }

                Text(
                    text = "${if (net >= 0) "+₦" else "-₦"}${String.format("%,.0f", Math.abs(net))}",
                    color = if (isWin) SafeGreen else DangerRed,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Stake: ₦${String.format("%,.0f", record.stakeNgn)} | Result: ${String.format("%.2f", record.multiplierResult)}x",
                    color = TextWhite,
                    fontSize = 11.sp
                )
                Text(
                    text = "Risk: ${record.riskRating}",
                    color = GoldAccent,
                    fontSize = 10.sp
                )
            }

            if (record.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = record.notes,
                    color = TextMuted,
                    fontSize = 10.sp
                )
            }
        }
    }
}
