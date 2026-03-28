package com.buildndeploy.herflow.domain.logic

import com.buildndeploy.herflow.domain.model.CervicalMucusType
import java.time.LocalDate
import kotlin.math.abs

object FertilityScoreEngine {
    fun calculate(
        cycleDay: Int,
        mucusType: CervicalMucusType?,
        bbtResult: BbtOvulationResult?,
        predictedOvulationDate: LocalDate?,
        currentDate: LocalDate
    ): FertilityScoreLevel {
        var score = when (CervicalMucusFertilityScorer.score(cycleDay, mucusType)) {
            FertilityScoreLevel.LOW -> 1
            FertilityScoreLevel.MEDIUM -> 2
            FertilityScoreLevel.HIGH -> 3
            FertilityScoreLevel.PEAK -> 4
        }

        if (predictedOvulationDate != null) {
            val daysFromOvulation = abs(predictedOvulationDate.toEpochDay() - currentDate.toEpochDay())
            when {
                daysFromOvulation <= 1 -> score += 2
                daysFromOvulation <= 3 -> score += 1
            }
        }

        if (bbtResult?.ovulationDate == currentDate || bbtResult?.ovulationDate == currentDate.minusDays(1)) {
            score += 2
        }

        return when {
            score <= 2 -> FertilityScoreLevel.LOW
            score <= 4 -> FertilityScoreLevel.MEDIUM
            score <= 6 -> FertilityScoreLevel.HIGH
            else -> FertilityScoreLevel.PEAK
        }
    }
}
