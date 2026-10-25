package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.DangerRed
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.SafeGreen
import com.example.ui.theme.WarningOrange

enum class RiskLevel(
    val title: String,
    val description: String,
    val color: Color
) {
    LOW(
        title = "Low Risk",
        description = "High statistical survival odds (>80%). Conservative cashout zone.",
        color = SafeGreen
    ),
    MODERATE(
        title = "Moderate Risk",
        description = "Balanced odds (60% - 80%). Standard calculated risk profile.",
        color = GoldAccent
    ),
    HIGH(
        title = "High Risk",
        description = "Substantial loss probability (35% - 60%). Recommend cashout.",
        color = WarningOrange
    ),
    CRITICAL(
        title = "Critical Risk",
        description = "Extreme hazard (<35% survival). Mathematical EV negative without stop-loss.",
        color = DangerRed
    );

    companion object {
        fun fromSurvivalRate(survivalRate: Float): RiskLevel {
            return when {
                survivalRate >= 0.80f -> LOW
                survivalRate >= 0.60f -> MODERATE
                survivalRate >= 0.35f -> HIGH
                else -> CRITICAL
            }
        }
    }
}
