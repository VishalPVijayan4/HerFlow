package com.buildndeploy.herflow.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CycleDao {
    @Query("SELECT * FROM cycle_entries ORDER BY startDate DESC")
    fun observeEntries(): Flow<List<CycleEntryEntity>>

    @Query("SELECT COUNT(*) FROM cycle_entries")
    suspend fun countEntries(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entries: List<CycleEntryEntity>)
}
