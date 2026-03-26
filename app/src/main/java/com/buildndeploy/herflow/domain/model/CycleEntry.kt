package com.buildndeploy.herflow.domain.model

import java.time.LocalDate

data class CycleEntry(
    val id: Long,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val flowLevel: Int,
    val painLevel: Int,
    val mood: String,
    val notes: String?
)
