package com.buildndeploy.herflow.domain.logic

import com.buildndeploy.herflow.domain.model.CervicalMucusType

enum class FertilityScoreLevel {
    LOW,
    MEDIUM,
    HIGH,
    PEAK
}

object CervicalMucusFertilityScorer {
    fun score(cycleDay: Int, mucusType: CervicalMucusType?): FertilityScoreLevel {
        if (cycleDay <= 0) return FertilityScoreLevel.LOW

        val cycleDayScore = when (cycleDay) {
            in 1..7 -> 0
            in 8..10 -> 1
            in 11..16 -> 2
            else -> 1
        }

        val mucusScore = when (mucusType) {
            CervicalMucusType.DRY -> 0
            CervicalMucusType.STICKY -> 1
            CervicalMucusType.CREAMY -> 2
            CervicalMucusType.WATERY -> 3
            CervicalMucusType.EGG_WHITE -> 4
            null -> 0
        }

        return (cycleDayScore + mucusScore).toFertilityLevel()
    }

    private fun Int.toFertilityLevel(): FertilityScoreLevel = when (this) {
        in Int.MIN_VALUE..1 -> FertilityScoreLevel.LOW
        in 2..3 -> FertilityScoreLevel.MEDIUM
        in 4..5 -> FertilityScoreLevel.HIGH
        else -> FertilityScoreLevel.PEAK
    }
}
