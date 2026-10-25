package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.DoubleBlack
import com.example.ui.theme.DoubleGreen
import com.example.ui.theme.DoubleRed

enum class DoubleColor(
    val title: String,
    val multiplier: Double,
    val theoreticalProb: Float,
    val color: Color,
    val slotCount: Int
) {
    RED(
        title = "Red (x2)",
        multiplier = 2.0,
        theoreticalProb = 7f / 15f,
        color = DoubleRed,
        slotCount = 7
    ),
    BLACK(
        title = "Black (x2)",
        multiplier = 2.0,
        theoreticalProb = 7f / 15f,
        color = DoubleBlack,
        slotCount = 7
    ),
    LUCKY(
        title = "Lucky (x14)",
        multiplier = 14.0,
        theoreticalProb = 1f / 15f,
        color = DoubleGreen,
        slotCount = 1
    );

    val theoreticalPercentage: String
        get() = String.format("%.2f%%", theoreticalProb * 100)
}

data class DoubleRoll(
    val id: Long = System.currentTimeMillis(),
    val color: DoubleColor,
    val timestamp: Long = System.currentTimeMillis()
)

data class DoubleAnalysis(
    val rollHistory: List<DoubleRoll> = emptyList(),
    val redCount: Int = 0,
    val blackCount: Int = 0,
    val luckyCount: Int = 0,
    val currentStreakColor: DoubleColor? = null,
    val currentStreakLength: Int = 0,
    val observedRedProb: Float = 0.4667f,
    val observedBlackProb: Float = 0.4667f,
    val observedLuckyProb: Float = 0.0667f,
    val roundsSinceLucky: Int = 0,
    val recommendedColor: DoubleColor = DoubleColor.RED,
    val suggestedStakeFraction: Float = 0.02f, // 2% bankroll rule
    val statisticalAdvice: String = "Sectors are independent. The house edge is fixed at 6.67% on all colors."
) {
    companion object {
        fun analyze(history: List<DoubleRoll>): DoubleAnalysis {
            if (history.isEmpty()) {
                return DoubleAnalysis()
            }

            val total = history.size
            val red = history.count { it.color == DoubleColor.RED }
            val black = history.count { it.color == DoubleColor.BLACK }
            val lucky = history.count { it.color == DoubleColor.LUCKY }

            // Calculate current streak from newest roll (first in list or last in list)
            var streakLen = 0
            var streakCol: DoubleColor? = null
            for (roll in history) {
                if (streakCol == null) {
                    streakCol = roll.color
                    streakLen = 1
                } else if (roll.color == streakCol) {
                    streakLen++
                } else {
                    break
                }
            }

            var sinceLucky = 0
            for (roll in history) {
                if (roll.color == DoubleColor.LUCKY) break
                sinceLucky++
            }

            // Advice based on mathematical reality
            val advice = when {
                streakLen >= 4 -> "Observed $streakLen consecutive ${streakCol?.title}. Note: Past spins DO NOT alter subsequent spin probability (Gambler\'s Fallacy). Theoretical odds remain 46.67%."
                sinceLucky > 20 -> "Lucky (x14) hasn\'t appeared in $sinceLucky rounds. Each spin has a 6.67% chance regardless of past absence."
                else -> "Active historical tracking. Maintain conservative bankroll sizing (1-3% of total NGN balance)."
            }

            // Balanced recommendation
            val recommendation = if (red < black) DoubleColor.RED else DoubleColor.BLACK

            return DoubleAnalysis(
                rollHistory = history,
                redCount = red,
                blackCount = black,
                luckyCount = lucky,
                currentStreakColor = streakCol,
                currentStreakLength = streakLen,
                observedRedProb = red.toFloat() / total.toFloat(),
                observedBlackProb = black.toFloat() / total.toFloat(),
                observedLuckyProb = lucky.toFloat() / total.toFloat(),
                roundsSinceLucky = sinceLucky,
                recommendedColor = recommendation,
                suggestedStakeFraction = 0.02f,
                statisticalAdvice = advice
            )
        }
    }
}
