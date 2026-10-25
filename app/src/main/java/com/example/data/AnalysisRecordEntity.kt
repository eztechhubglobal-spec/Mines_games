package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "analysis_records")
data class AnalysisRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val gameType: String,
    val timestamp: Long = System.currentTimeMillis(),
    val stakeNgn: Double,
    val multiplierResult: Double,
    val payoutNgn: Double,
    val safePicksCount: Int,
    val riskRating: String,
    val outcomeType: String, // "CASHOUT", "MINE_HIT", "DOUBLE_WIN", "DOUBLE_LOSS"
    val notes: String
)
