package com.buildndeploy.herflow.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "cycle_entries")
data class CycleEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val flowLevel: Int,
    val painLevel: Int,
    val mood: String,
    val notes: String?
)
