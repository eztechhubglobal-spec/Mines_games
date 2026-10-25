package com.example.model

enum class TileStatus {
    UNOPENED,
    SAFE_GEM,
    FLAGGED_MINE,
    SUGGESTED_SAFE,
    HIGH_RISK_MINE
}

data class TileInfo(
    val index: Int,
    val row: Int,
    val col: Int,
    val status: TileStatus = TileStatus.UNOPENED,
    val safeProbability: Float = 0f,
    val isRecommended: Boolean = false
)

data class MinesState(
    val tiles: List<TileInfo> = defaultGrid(),
    val mineCount: Int = 3,
    val stakeNgn: Double = 1000.0,
    val openedSafeCount: Int = 0,
    val currentStepSafeProb: Float = (25f - 3f) / 25f,
    val cumulativeSurvivalProb: Float = 1.0f,
    val currentMultiplier: Double = 1.0,
    val nextMultiplier: Double = 1.09,
    val potentialPayoutNgn: Double = 1000.0,
    val riskLevel: RiskLevel = RiskLevel.LOW,
    val suggestedTileIndices: List<Int> = listOf(0, 4, 12, 20, 24),
    val mathematicalProof: String = "Prior odds: 22 safe / 25 tiles = 88.00%"
) {
    companion object {
        fun defaultGrid(): List<TileInfo> {
            return (0 until 25).map { idx ->
                TileInfo(
                    index = idx,
                    row = idx / 5,
                    col = idx % 5,
                    status = TileStatus.UNOPENED,
                    safeProbability = 0.88f,
                    isRecommended = idx in listOf(0, 4, 12, 20, 24)
                )
            }
        }

        fun calculateCombinatorics(
            mineCount: Int,
            openedSafeIndices: Set<Int>,
            flaggedMineIndices: Set<Int>,
            stakeNgn: Double
        ): MinesState {
            val totalTiles = 25
            val totalMines = mineCount.coerceIn(1, 24)
            val totalSafe = totalTiles - totalMines
            val openedCount = openedSafeIndices.size.coerceAtMost(totalSafe)

            val remainingTiles = totalTiles - openedCount
            val remainingSafe = (totalSafe - openedCount).coerceAtLeast(0)

            val nextStepProb = if (remainingTiles > 0) {
                remainingSafe.toFloat() / remainingTiles.toFloat()
            } else 0f

            // Cumulative survival probability = product of safe choices
            var cumulativeProb = 1.0
            for (step in 0 until openedCount) {
                val stepSafe = (totalSafe - step).toDouble()
                val stepTotal = (totalTiles - step).toDouble()
                cumulativeProb *= (stepSafe / stepTotal)
            }

            // Casino standard 1Win/Spribe payout formula with ~2% house margin
            val houseEdgeMargin = 0.98
            val currentMult = if (openedCount == 0) 1.0 else {
                (houseEdgeMargin / cumulativeProb.coerceAtLeast(0.00001)).coerceAtLeast(1.0)
            }

            // Next step multiplier
            val nextCumulativeProb = cumulativeProb * nextStepProb.toDouble().coerceAtLeast(0.00001)
            val nextMult = if (remainingSafe == 0) currentMult else {
                (houseEdgeMargin / nextCumulativeProb.coerceAtLeast(0.00001)).coerceAtLeast(currentMult)
            }

            val risk = RiskLevel.fromSurvivalRate(
                if (openedCount == 0) nextStepProb else cumulativeProb.toFloat()
            )

            // Select recommended indices among unopened tiles (heuristic: symmetric dispersal)
            val unopenedIndices = (0 until 25).filter {
                it !in openedSafeIndices && it !in flaggedMineIndices
            }

            // Symmetrical / disperse pattern preference for strategic play
            val preferredOrder = listOf(12, 0, 4, 20, 24, 6, 8, 16, 18, 2, 10, 14, 22, 1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23)
            val recommended = preferredOrder.filter { it in unopenedIndices }.take(3)

            val tiles = (0 until 25).map { idx ->
                val row = idx / 5
                val col = idx % 5
                when {
                    idx in openedSafeIndices -> TileInfo(
                        index = idx, row = row, col = col,
                        status = TileStatus.SAFE_GEM,
                        safeProbability = 1.0f
                    )
                    idx in flaggedMineIndices -> TileInfo(
                        index = idx, row = row, col = col,
                        status = TileStatus.FLAGGED_MINE,
                        safeProbability = 0.0f
                    )
                    idx in recommended -> TileInfo(
                        index = idx, row = row, col = col,
                        status = TileStatus.SUGGESTED_SAFE,
                        safeProbability = nextStepProb,
                        isRecommended = true
                    )
                    else -> TileInfo(
                        index = idx, row = row, col = col,
                        status = TileStatus.UNOPENED,
                        safeProbability = nextStepProb
                    )
                }
            }

            val summary = "Tiles: $remainingTiles left | Safe: $remainingSafe | Step Odds: ${String.format("%.2f", nextStepProb * 100)}% | Survival: ${String.format("%.2f", cumulativeProb * 100)}%"

            return MinesState(
                tiles = tiles,
                mineCount = totalMines,
                stakeNgn = stakeNgn,
                openedSafeCount = openedCount,
                currentStepSafeProb = nextStepProb,
                cumulativeSurvivalProb = cumulativeProb.toFloat(),
                currentMultiplier = currentMult,
                nextMultiplier = nextMult,
                potentialPayoutNgn = stakeNgn * currentMult,
                riskLevel = risk,
                suggestedTileIndices = recommended,
                mathematicalProof = summary
            )
        }
    }
}
