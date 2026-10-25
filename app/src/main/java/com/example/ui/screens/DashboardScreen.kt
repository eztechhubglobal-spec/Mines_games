package com.example.ui.screens

import android.provider.Settings
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GameType
import com.example.service.OverlayStateManager
import com.example.ui.theme.DangerRed
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.ObsidianBackground
import com.example.ui.theme.ObsidianStroke
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceVariant
import com.example.ui.theme.SafeGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.viewmodel.AssistantViewModel

@Composable
fun DashboardScreen(viewModel: AssistantViewModel) {
    val context = LocalContext.current
    val targetUrl by OverlayStateManager.targetUrl.collectAsState()
    val activeGameType by OverlayStateManager.activeGameType.collectAsState()
    val isRunning by OverlayStateManager.isServiceRunning.collectAsState()
    val isPaused by OverlayStateManager.isPaused.collectAsState()
    val bankrollNgn by OverlayStateManager.bankrollNgn.collectAsState()
    val currentStakeNgn by OverlayStateManager.currentStakeNgn.collectAsState()

    val hasOverlayPermission = Settings.canDrawOverlays(context)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            // Hero Title Banner
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "MINES AI ASSISTANT",
                        color = EmeraldPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Real-Time Probability Engine & Floating HUD",
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                }

                // Currency Badge
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = GoldContainer,
                    border = androidx.compose.foundation.BorderStroke(1.dp, GoldAccent)
                ) {
                    Text(
                        text = "₦ NGN",
                        color = GoldAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Overlay Permission Banner if missing
        if (!hasOverlayPermission) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = GoldContainer.copy(alpha = 0.4f)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GoldAccent)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = GoldAccent,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Overlay Permission Required",
                                color = GoldAccent,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Grant permission to float over Chrome while you play 1Win.",
                                color = TextWhite,
                                fontSize = 11.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { viewModel.toggleOverlayService(context) },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("grant_permission_button")
                        ) {
                            Text("Grant", color = ObsidianBackground, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Game Target URL Input & Auto-Detection
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianStroke)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "TARGET 1WIN GAME URL",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = targetUrl,
                        onValueChange = { viewModel.updateUrl(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("target_url_input"),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldPrimary,
                            unfocusedBorderColor = ObsidianStroke,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            cursorColor = EmeraldPrimary
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Detected Game Mechanic Chip
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Detected: ${activeGameType.displayName}",
                                color = EmeraldPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Manual Mode Toggle
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            GameTypeChip(
                                title = "Mines",
                                isSelected = activeGameType == GameType.MINES,
                                onClick = { viewModel.setGameType(GameType.MINES) }
                            )
                            GameTypeChip(
                                title = "Double",
                                isSelected = activeGameType == GameType.DOUBLE_DRUM,
                                onClick = { viewModel.setGameType(GameType.DOUBLE_DRUM) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (activeGameType == GameType.DOUBLE_DRUM)
                            "URL target uses 1Win Double drum mechanics (Red 2x, Black 2x, Lucky 14x). Probability engine active."
                        else
                            "URL target uses 1Win Mines 5x5 grid combinatorics. Real-time gem/mine odds active.",
                        color = TextMuted,
                        fontSize = 10.sp
                    )
                }
            }
        }

        // Floating Assistant Controller Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianSurface),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isRunning) EmeraldPrimary else ObsidianStroke
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(if (isRunning) SafeGreen else TextMuted)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isRunning) "FLOATING HUD ACTIVE" else "FLOATING HUD STOPPED",
                                color = if (isRunning) EmeraldPrimary else TextMuted,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        if (isRunning) {
                            Text(
                                text = if (isPaused) "PAUSED" else "ALWAYS-ON-TOP",
                                color = if (isPaused) GoldAccent else SafeGreen,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Big Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { viewModel.toggleOverlayService(context) },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("toggle_overlay_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isRunning) DangerRed else EmeraldPrimary
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = if (isRunning) Icons.Default.Layers else Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = if (isRunning) TextWhite else ObsidianBackground,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isRunning) "Stop HUD" else "Start Floating HUD",
                                color = if (isRunning) TextWhite else ObsidianBackground,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = { viewModel.launchGameInChrome(context, targetUrl) },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("launch_chrome_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldContainer),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldPrimary)
                        ) {
                            Icon(
                                imageVector = Icons.Default.OpenInBrowser,
                                contentDescription = null,
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Open in Chrome",
                                color = EmeraldPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Tip: When playing in Chrome, reposition the floating badge anywhere with your finger, or tap it to open the live HUD.",
                        color = TextMuted,
                        fontSize = 10.sp
                    )
                }
            }
        }

        // Bankroll & Stake Management Card (in Nigerian Naira)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianStroke)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "BANKROLL & STAKING (₦ NGN)",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Current Bankroll",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                            Text(
                                text = "₦${String.format("%,.0f", bankrollNgn)}",
                                color = TextWhite,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Active Stake",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                            Text(
                                text = "₦${String.format("%,.0f", currentStakeNgn)}",
                                color = GoldAccent,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Preset Stake Chips
                    Text(
                        text = "Quick Stake Selection:",
                        color = TextMuted,
                        fontSize = 10.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(200.0, 500.0, 1000.0, 2000.0, 5000.0).forEach { amount ->
                            StakeChip(
                                label = "₦${amount.toInt()}",
                                isSelected = currentStakeNgn == amount,
                                onClick = { viewModel.updateStake(amount) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    // Kelly criterion note
                    val kellySafe = (bankrollNgn * 0.02).coerceAtLeast(100.0)
                    Text(
                        text = "Safe Risk Rule: Recommended max bet is 2% of bankroll (₦${String.format("%,.0f", kellySafe)}).",
                        color = SafeGreen,
                        fontSize = 10.sp
                    )
                }
            }
        }

        // Truth in AI & Mathematics Ethics Callout
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianBackground),
                border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianStroke)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Truth in AI & Game Independence",
                            color = EmeraldPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Every round in 1Win uses an independent server-generated random number generator (RNG). Previous spins or revealed tiles never alter future probabilities. We calculate exact verified combinatorics, never fake 100% predictions.",
                            color = TextMuted,
                            fontSize = 10.sp,
                            lineHeight = 14.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun GameTypeChip(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bg = if (isSelected) EmeraldPrimary else ObsidianSurfaceVariant
    val textColor = if (isSelected) ObsidianBackground else TextWhite

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bg)
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = title, color = textColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun StakeChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bg = if (isSelected) GoldAccent else ObsidianSurfaceVariant
    val textColor = if (isSelected) ObsidianBackground else TextWhite

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bg)
            .border(1.dp, if (isSelected) GoldAccent else ObsidianStroke, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, color = textColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}
