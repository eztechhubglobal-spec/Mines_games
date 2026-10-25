package com.example

import com.example.model.DoubleAnalysis
import com.example.model.DoubleColor
import com.example.model.DoubleRoll
import com.example.model.MinesState
import com.example.util.ProvablyFairUtil
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testMinesCombinatoricsInitial() {
        val state = MinesState.calculateCombinatorics(
            mineCount = 3,
            openedSafeIndices = emptySet(),
            flaggedMineIndices = emptySet(),
            stakeNgn = 1000.0
        )
        // 22 safe out of 25 = 0.88
        assertEquals(0.88f, state.currentStepSafeProb, 0.001f)
        assertEquals(1.0f, state.cumulativeSurvivalProb, 0.001f)
        assertEquals(1.0, state.currentMultiplier, 0.01)
    }

    @Test
    fun testMinesCombinatoricsAfterOnePick() {
        val state = MinesState.calculateCombinatorics(
            mineCount = 3,
            openedSafeIndices = setOf(12),
            flaggedMineIndices = emptySet(),
            stakeNgn = 1000.0
        )
        // 21 safe remaining out of 24 = 0.875
        assertEquals(0.875f, state.currentStepSafeProb, 0.001f)
        // Cumulative survival is 0.88
        assertEquals(0.88f, state.cumulativeSurvivalProb, 0.001f)
        // Multiplier should exceed 1.0
        assertTrue(state.currentMultiplier > 1.05)
    }

    @Test
    fun testDoubleColorProbabilities() {
        assertEquals(7f / 15f, DoubleColor.RED.theoreticalProb, 0.001f)
        assertEquals(7f / 15f, DoubleColor.BLACK.theoreticalProb, 0.001f)
        assertEquals(1f / 15f, DoubleColor.LUCKY.theoreticalProb, 0.001f)
    }

    @Test
    fun testDoubleStreakAnalysis() {
        val rolls = listOf(
            DoubleRoll(color = DoubleColor.RED),
            DoubleRoll(color = DoubleColor.RED),
            DoubleRoll(color = DoubleColor.RED)
        )
        val analysis = DoubleAnalysis.analyze(rolls)
        assertEquals(3, analysis.currentStreakLength)
        assertEquals(DoubleColor.RED, analysis.currentStreakColor)
    }

    @Test
    fun testProvablyFairSha256() {
        // SHA-256("test") = 9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08
        val hash = ProvablyFairUtil.sha256("test")
        assertEquals("9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08", hash)
    }
}
