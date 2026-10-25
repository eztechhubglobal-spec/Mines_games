package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AnalysisDao {
    @Query("SELECT * FROM analysis_records ORDER BY timestamp DESC")
    fun getAllRecords(): Flow<List<AnalysisRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: AnalysisRecordEntity): Long

    @Query("DELETE FROM analysis_records")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM analysis_records")
    suspend fun getCount(): Int
}
