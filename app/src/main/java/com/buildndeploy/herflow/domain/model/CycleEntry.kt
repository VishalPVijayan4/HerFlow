package com.buildndeploy.herflow.domain.model

import java.time.LocalDate

data class CycleEntry(
    val id: Long,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val flowLevel: FlowLevel,
    val painLevel: Int,
    val mood: MoodType,
    val symptoms: List<SymptomType>,
    val cervicalMucus: CervicalMucusType?,
    val notes: String?
)
