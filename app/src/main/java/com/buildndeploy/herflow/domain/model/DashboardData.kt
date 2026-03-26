package com.buildndeploy.herflow.domain.model

import java.time.LocalDate

data class DashboardData(
    val today: LocalDate,
    val nextPeriodDate: LocalDate,
    val cycleLengthDays: Int,
    val latestEntries: List<CycleEntry>,
    val features: List<FeatureCard>
)
