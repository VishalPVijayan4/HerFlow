package com.buildndeploy.herflow.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.buildndeploy.herflow.domain.model.CervicalMucusType
import com.buildndeploy.herflow.domain.model.FlowLevel
import com.buildndeploy.herflow.domain.model.MoodType
import com.buildndeploy.herflow.domain.model.SymptomType
import java.time.LocalDate

@Entity(tableName = "cycle_entries")
data class CycleEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val flowLevel: FlowLevel,
    val painLevel: Int,
    val mood: MoodType,
    val symptoms: List<SymptomType>,
    val cervicalMucus: CervicalMucusType?,
    val notes: String?
)
