package com.buildndeploy.herflow.domain.logic

import com.buildndeploy.herflow.domain.model.CervicalMucusType
import com.buildndeploy.herflow.domain.model.MoodType
import com.buildndeploy.herflow.domain.model.SymptomType
import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AdvancedFertilityLogicTest {

    @Test
    fun `cervical mucus scorer boosts around fertile window`() {
        val score = CervicalMucusFertilityScorer.score(
            cycleDay = 13,
            mucusType = CervicalMucusType.EGG_WHITE
        )

        assertEquals(FertilityScoreLevel.PEAK, score)
    }

    @Test
    fun `fertility score engine combines day mucus and ovulation context`() {
        val score = FertilityScoreEngine.calculate(
            cycleDay = 14,
            mucusType = CervicalMucusType.WATERY,
            bbtResult = BbtOvulationResult(
                ovulationDate = LocalDate.of(2026, 3, 14),
                dipDate = LocalDate.of(2026, 3, 14),
                confidence = 0.85,
                reason = "dip+rise"
            ),
            predictedOvulationDate = LocalDate.of(2026, 3, 14),
            currentDate = LocalDate.of(2026, 3, 14)
        )

        assertTrue(score == FertilityScoreLevel.HIGH || score == FertilityScoreLevel.PEAK)
    }

    @Test
    fun `trend calculator groups logs and finds dominant mood`() {
        val logs = listOf(
            DailyMoodSymptomLog(
                date = LocalDate.of(2026, 3, 2),
                cycleDay = 2,
                mood = MoodType.LOW,
                symptoms = listOf(SymptomType.CRAMPS)
            ),
            DailyMoodSymptomLog(
                date = LocalDate.of(2026, 4, 1),
                cycleDay = 2,
                mood = MoodType.LOW,
                symptoms = listOf(SymptomType.FATIGUE)
            ),
            DailyMoodSymptomLog(
                date = LocalDate.of(2026, 4, 2),
                cycleDay = 3,
                mood = MoodType.CALM,
                symptoms = listOf(SymptomType.BLOATING)
            )
        )

        val trends = MoodSymptomTrendCalculator.calculateTrends(logs)

        assertEquals(2, trends.size)
        assertEquals(MoodType.LOW, trends.first { it.cycleDay == 2 }.dominantMood)
    }

    @Test
    fun `phase insights detects ovulatory around estimated ovulation`() {
        val insight = CycleInsightsEngine.phaseInsights(cycleDay = 14, averageCycleLength = 28)
        assertEquals(CyclePhase.OVULATORY, insight.phase)
        assertTrue(insight.tips.isNotEmpty())
    }
}
