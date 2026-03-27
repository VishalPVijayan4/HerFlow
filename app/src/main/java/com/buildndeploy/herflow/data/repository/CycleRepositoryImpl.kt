package com.buildndeploy.herflow.data.repository

import com.buildndeploy.herflow.data.local.CycleDao
import com.buildndeploy.herflow.data.local.CycleEntryEntity
import com.buildndeploy.herflow.data.preferences.UserPreferencesDataSource
import com.buildndeploy.herflow.domain.model.CervicalMucusType
import com.buildndeploy.herflow.domain.model.CycleEntry
import com.buildndeploy.herflow.domain.model.DashboardData
import com.buildndeploy.herflow.domain.model.FeatureCard
import com.buildndeploy.herflow.domain.model.FlowLevel
import com.buildndeploy.herflow.domain.model.MoodType
import com.buildndeploy.herflow.domain.model.SymptomType
import com.buildndeploy.herflow.domain.repository.CycleRepository
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class CycleRepositoryImpl @Inject constructor(
    private val cycleDao: CycleDao,
    private val preferences: UserPreferencesDataSource
) : CycleRepository {

    override fun observeDashboard(): Flow<DashboardData> {
        return combine(
            cycleDao.observeEntries(),
            preferences.cycleLengthFlow,
            preferences.onboardingFlow
        ) { entities, cycleLength, onboardingComplete ->
            val latestEntries = entities.take(3).map { it.toDomain() }
            val latestStart = latestEntries.firstOrNull()?.startDate ?: LocalDate.now()
            val nextPeriodDate = latestStart.plusDays(cycleLength.toLong())
            DashboardData(
                today = LocalDate.now(),
                nextPeriodDate = nextPeriodDate,
                cycleLengthDays = cycleLength,
                latestEntries = latestEntries,
                features = buildFeatureCards(latestEntries, onboardingComplete)
            )
        }
    }

    override suspend fun refreshSeedDataIfNeeded() {
        if (cycleDao.countEntries() > 0) return
        cycleDao.insertAll(
            listOf(
                CycleEntryEntity(
                    startDate = LocalDate.now().minusDays(56),
                    endDate = LocalDate.now().minusDays(51),
                    flowLevel = FlowLevel.MEDIUM,
                    painLevel = 2,
                    mood = MoodType.CALM,
                    symptoms = listOf(SymptomType.CRAMPS, SymptomType.FATIGUE),
                    cervicalMucus = CervicalMucusType.CREAMY,
                    notes = "Hydration and yoga helped with cramps"
                ),
                CycleEntryEntity(
                    startDate = LocalDate.now().minusDays(28),
                    endDate = LocalDate.now().minusDays(23),
                    flowLevel = FlowLevel.HEAVY,
                    painLevel = 3,
                    mood = MoodType.LOW,
                    symptoms = listOf(SymptomType.BLOATING, SymptomType.BACK_PAIN),
                    cervicalMucus = CervicalMucusType.STICKY,
                    notes = "Prioritized sleep and iron-rich meals"
                )
            )
        )
        preferences.markOnboardingComplete()
    }

    private fun buildFeatureCards(
        latestEntries: List<CycleEntry>,
        onboardingComplete: Boolean
    ): List<FeatureCard> {
        val latestMood = latestEntries.firstOrNull()?.mood?.name?.replace('_', ' ') ?: "No data yet"
        return listOf(
            FeatureCard(
                title = "Smart Predictions",
                subtitle = "Cycle + ovulation windows",
                detail = "Forecasts adapt to your entries and average cycle length."
            ),
            FeatureCard(
                title = "Symptom Journal",
                subtitle = "Mood, pain, flow, notes",
                detail = "Latest trend: $latestMood"
            ),
            FeatureCard(
                title = "Insights Hub",
                subtitle = "Patterns that matter",
                detail = if (onboardingComplete) {
                    "You are ready to personalize reminders and routines."
                } else {
                    "Complete onboarding to unlock personalized insights."
                }
            )
        )
    }

    private fun CycleEntryEntity.toDomain(): CycleEntry = CycleEntry(
        id = id,
        startDate = startDate,
        endDate = endDate,
        flowLevel = flowLevel,
        painLevel = painLevel,
        mood = mood,
        symptoms = symptoms,
        cervicalMucus = cervicalMucus,
        notes = notes
    )
}
