package com.buildndeploy.herflow.presentation.dashboard

import android.os.Build
import androidx.annotation.RequiresApi
import com.buildndeploy.herflow.domain.model.CycleEntry
import com.buildndeploy.herflow.domain.model.FeatureCard
import java.time.LocalDate

sealed interface DashboardIntent {
    data object Refresh : DashboardIntent
}

data class DashboardState @RequiresApi(Build.VERSION_CODES.O) constructor(
    val isLoading: Boolean = true,
    val today: LocalDate = LocalDate.now(),
    val nextPeriodDate: LocalDate = LocalDate.now(),
    val cycleLengthDays: Int = 28,
    val entries: List<CycleEntry> = emptyList(),
    val featureCards: List<FeatureCard> = emptyList(),
    val error: String? = null
)
