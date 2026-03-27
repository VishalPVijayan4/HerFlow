package com.buildndeploy.herflow.domain.logic

import java.time.LocalDate

data class CyclePrediction(
    val nextPeriodDate: LocalDate,
    val ovulationDate: LocalDate,
    val fertileWindowStart: LocalDate,
    val fertileWindowEnd: LocalDate,
    val pmsWindowStart: LocalDate,
    val pmsWindowEnd: LocalDate
)

class CyclePredictionEngine {
    fun predict(lastPeriodStart: LocalDate, averageCycleLength: Int): CyclePrediction {
        require(averageCycleLength > 0) { "averageCycleLength must be > 0" }

        val nextPeriod = lastPeriodStart.plusDays(averageCycleLength.toLong())
        val ovulation = nextPeriod.minusDays(14)
        val fertileStart = ovulation.minusDays(4)
        val fertileEnd = ovulation.plusDays(4)
        val pmsStart = nextPeriod.minusDays(7)

        return CyclePrediction(
            nextPeriodDate = nextPeriod,
            ovulationDate = ovulation,
            fertileWindowStart = fertileStart,
            fertileWindowEnd = fertileEnd,
            pmsWindowStart = pmsStart,
            pmsWindowEnd = nextPeriod.minusDays(1)
        )
    }
}
