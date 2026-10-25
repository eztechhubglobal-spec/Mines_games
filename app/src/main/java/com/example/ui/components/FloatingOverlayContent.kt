package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DoubleColor
import com.example.model.GameType
import com.example.model.TileStatus
import com.example.service.OverlayStateManager
import com.example.ui.theme.DangerRed
import com.example.ui.theme.DoubleBlack
import com.example.ui.theme.DoubleGreen
import com.example.ui.theme.DoubleRed
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

@Composable
fun FloatingOverlayContent(
    onCloseRequested: () -> Unit
) {
    val isMinimized by OverlayStateManager.isMinimized.collectAsState()
    val isPaused by OverlayStateManager.isPaused.collectAsState()
    val activeGameType by OverlayStateManager.activeGameType.collectAsState()
    val minesState by OverlayStateManager.minesState.collectAsState()
    val doubleAnalysis by OverlayStateManager.doubleAnalysis.collectAsState()
    val screenMessage by OverlayStateManager.screenAnalysisMessage.collectAsState()

    if (isMinimized) {
        // Compact Draggable Bubble View
        MinimizedBubble(
            gameType = activeGameType,
            minesProb = minesState.currentStepSafeProb,
            isPaused = isPaused,
            onExpand = { OverlayStateManager.setMinimized(false) }
        )
    } else {
        // Expanded Full Overlay HUD
        ExpandedHud(
            isPaused = isPaused,
            activeGameType = activeGameType,
            minesState = minesState,
            doubleAnalysis = doubleAnalysis,
            screenMessage = screenMessage,
            onTogglePause = { OverlayStateManager.togglePaused() },
            onMinimize = { OverlayStateManager.setMinimized(true) },
            onClose = onCloseRequested,
            onSelectGame = { OverlayStateManager.setActiveGameType(it) }
        )
    }
}

@Composable
fun MinimizedBubble(
    gameType: GameType,
    minesProb: Float,
    isPaused: Boolean,
    onExpand: () -> Unit
) {
    Surface(
        modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)
            .border(2.dp, if (isPaused) GoldAccent else EmeraldPrimary, CircleShape)
            .clickable(onClick = onExpand),
        color = ObsidianSurface,
        shadowElevation = 8.dp
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            if (isPaused) GoldAccent.copy(alpha = 0.25f) else EmeraldPrimary.copy(alpha = 0.25f),
                            ObsidianBackground
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (isPaused) {
                    Icon(
                        imageVector = Icons.Default.Pause,
                        contentDescription = "Paused",
                        tint = GoldAccent,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "PAUSED",
                        color = GoldAccent,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                } else if (gameType == GameType.MINES) {
                    Text(
                        text = "${(minesProb * 100).toInt()}%",
                        color = EmeraldPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "SAFE",
                        color = TextMuted,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Text(
                        text = "2x/14x",
                        color = GoldAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "DOUBLE",
                        color = TextMuted,
                        fontSize = 7.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun ExpandedHud(
    isPaused: Boolean,
    activeGameType: GameType,
    minesState: com.example.model.MinesState,
    doubleAnalysis: com.example.model.DoubleAnalysis,
    screenMessage: String?,
    onTogglePause: () -> Unit,
    onMinimize: () -> Unit,
    onClose: () -> Unit,
    onSelectGame: (GameType) -> Unit
) {
    Card(
        modifier = Modifier
            .width(320.dp)
            .border(1.5.dp, ObsidianStroke, RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = ObsidianSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // Header: Title & Window Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DragHandle,
                        contentDescription = "Drag overlay",
                        tint = TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "MINES AI HUD",
                        color = EmeraldPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onTogglePause,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                            contentDescription = if (isPaused) "Resume" else "Pause",
                            tint = if (isPaused) GoldAccent else TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = onMinimize,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FullscreenExit,
                            contentDescription = "Minimize",
                            tint = TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close overlay",
                            tint = DangerRed,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Game Type Switcher Pills
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .background(ObsidianBackground, RoundedCornerShape(10.dp))
                    .padding(2.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                GameTypePill(
                    title = "Mines (5x5)",
                    isSelected = activeGameType == GameType.MINES,
                    onClick = { onSelectGame(GameType.MINES) }
                )
                GameTypePill(
                    title = "Double (Wheel)",
                    isSelected = activeGameType == GameType.DOUBLE_DRUM,
                    onClick = { onSelectGame(GameType.DOUBLE_DRUM) }
                )
            }

            AnimatedVisibility(visible = screenMessage != null) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    color = EmeraldContainer.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = screenMessage ?: "",
                        color = EmeraldPrimary,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(6.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            if (isPaused) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "ASSISTANT PAUSED\nTap Play to resume real-time analysis",
                        color = GoldAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                when (activeGameType) {
                    GameType.MINES -> FloatingMinesView(minesState = minesState)
                    GameType.DOUBLE_DRUM -> FloatingDoubleView(doubleAnalysis = doubleAnalysis)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Independent Trials: Past rounds don't change future RNG.",
                color = TextMuted.copy(alpha = 0.7f),
                fontSize = 8.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun GameTypePill(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bg = if (isSelected) EmeraldPrimary else Color.Transparent
    val textColor = if (isSelected) ObsidianBackground else TextMuted
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bg)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
fun FloatingMinesView(minesState: com.example.model.MinesState) {
    Column {
        // Quick Metrics Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "NEXT SAFE PROB",
                    color = TextMuted,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${String.format("%.1f", minesState.currentStepSafeProb * 100)}%",
                    color = minesState.riskLevel.color,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "NEXT MULTIPLIER",
                    color = TextMuted,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${String.format("%.2f", minesState.nextMultiplier)}x",
                    color = EmeraldPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "RISK",
                    color = TextMuted,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = minesState.riskLevel.title.replace(" Risk", ""),
                    color = minesState.riskLevel.color,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        LinearProgressIndicator(
            progress = { minesState.currentStepSafeProb },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp)),
            color = minesState.riskLevel.color,
            trackColor = ObsidianBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Mini 5x5 Grid
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(ObsidianBackground, RoundedCornerShape(12.dp))
                .padding(6.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                for (row in 0 until 5) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        for (col in 0 until 5) {
                            val index = row * 5 + col
                            val tile = minesState.tiles.getOrNull(index)
                            MiniTileItem(
                                tile = tile,
                                onToggle = { OverlayStateManager.toggleTileOpened(index) },
                                onLongToggle = { OverlayStateManager.toggleTileFlagged(index) }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Action Buttons Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            OutlinedButton(
                onClick = { OverlayStateManager.triggerScreenAnalysis() },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = EmeraldPrimary),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = Brush.horizontalGradient(listOf(EmeraldPrimary, EmeraldContainer))
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(text = "Scan Canvas", fontSize = 10.sp)
            }

            OutlinedButton(
                onClick = { OverlayStateManager.resetMinesBoard() },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextWhite),
                shape = RoundedCornerShape(8.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(text = "Clear Grid", fontSize = 10.sp)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MiniTileItem(
    tile: com.example.model.TileInfo?,
    onToggle: () -> Unit,
    onLongToggle: () -> Unit
) {
    if (tile == null) return

    val bgColor = when (tile.status) {
        TileStatus.SAFE_GEM -> EmeraldContainer
        TileStatus.FLAGGED_MINE -> DangerRed.copy(alpha = 0.8f)
        TileStatus.SUGGESTED_SAFE -> EmeraldPrimary.copy(alpha = 0.25f)
        TileStatus.HIGH_RISK_MINE -> DangerRed.copy(alpha = 0.3f)
        TileStatus.UNOPENED -> ObsidianSurfaceVariant
    }

    val borderColor = when {
        tile.isRecommended -> EmeraldPrimary
        tile.status == TileStatus.SAFE_GEM -> SafeGreen
        tile.status == TileStatus.FLAGGED_MINE -> DangerRed
        else -> ObsidianStroke
    }

    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .border(if (tile.isRecommended) 1.5.dp else 1.dp, borderColor, RoundedCornerShape(6.dp))
            .combinedClickable(
                onClick = onToggle,
                onLongClick = onLongToggle
            ),
        contentAlignment = Alignment.Center
    ) {
        when (tile.status) {
            TileStatus.SAFE_GEM -> {
                Text(text = "💎", fontSize = 14.sp)
            }
            TileStatus.FLAGGED_MINE -> {
                Text(text = "💣", fontSize = 14.sp)
            }
            TileStatus.SUGGESTED_SAFE -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "★", color = EmeraldPrimary, fontSize = 10.sp)
                    Text(text = "SAFE", color = EmeraldPrimary, fontSize = 7.sp, fontWeight = FontWeight.Bold)
                }
            }
            else -> {
                Text(
                    text = "${tile.index + 1}",
                    color = TextMuted.copy(alpha = 0.5f),
                    fontSize = 9.sp
                )
            }
        }
    }
}

@Composable
fun FloatingDoubleView(doubleAnalysis: com.example.model.DoubleAnalysis) {
    Column {
        // Roll Logger Buttons: Red, Black, Lucky
        Text(
            text = "LOG OBSERVED COLOR IN CHROME",
            color = TextMuted,
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Button(
                onClick = { OverlayStateManager.addDoubleRoll(DoubleColor.RED) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = DoubleRed),
                shape = RoundedCornerShape(8.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(4.dp)
            ) {
                Text(text = "RED (2x)", fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { OverlayStateManager.addDoubleRoll(DoubleColor.BLACK) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = DoubleBlack),
                shape = RoundedCornerShape(8.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(4.dp)
            ) {
                Text(text = "BLACK (2x)", fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { OverlayStateManager.addDoubleRoll(DoubleColor.LUCKY) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = DoubleGreen),
                shape = RoundedCornerShape(8.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(4.dp)
            ) {
                Text(text = "LUCKY (14x)", fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // History Strip
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ObsidianBackground, RoundedCornerShape(8.dp))
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Last: ",
                color = TextMuted,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
            )
            if (doubleAnalysis.rollHistory.isEmpty()) {
                Text(
                    text = "No rolls recorded yet",
                    color = TextMuted,
                    fontSize = 9.sp
                )
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    doubleAnalysis.rollHistory.take(8).forEach { roll ->
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(roll.color.color)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Stats summary
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Red: ${doubleAnalysis.redCount}",
                color = DoubleRed,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Black: ${doubleAnalysis.blackCount}",
                color = TextWhite,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Lucky: ${doubleAnalysis.luckyCount}",
                color = DoubleGreen,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (doubleAnalysis.currentStreakLength >= 2) {
            Text(
                text = "Streak: ${doubleAnalysis.currentStreakLength}x ${doubleAnalysis.currentStreakColor?.title}",
                color = GoldAccent,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}
