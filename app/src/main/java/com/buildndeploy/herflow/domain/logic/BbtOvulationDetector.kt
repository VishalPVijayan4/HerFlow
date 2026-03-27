package com.buildndeploy.herflow.domain.logic

import java.time.LocalDate

data class BbtReading(
    val date: LocalDate,
    val temperatureCelsius: Double
)

data class BbtOvulationResult(
    val ovulationDate: LocalDate?,
    val dipDate: LocalDate?,
    val confidence: Double,
    val reason: String
)

object BbtOvulationDetector {
    fun detectOvulation(readings: List<BbtReading>, riseThreshold: Double = 0.2): BbtOvulationResult {
        if (readings.size < 6) {
            return BbtOvulationResult(null, null, 0.0, "At least 6 readings are required")
        }

        val sorted = readings.sortedBy { it.date }
        for (index in 2 until sorted.lastIndex - 2) {
            val dip = sorted[index]
            val baseline = sorted.subList(index - 2, index).map { it.temperatureCelsius }.average()
            val riseWindow = sorted.subList(index + 1, index + 4).map { it.temperatureCelsius }

            val hasDip = dip.temperatureCelsius < baseline
            val hasSustainedRise = riseWindow.all { it >= baseline + riseThreshold }

            if (hasDip && hasSustainedRise) {
                return BbtOvulationResult(
                    ovulationDate = dip.date,
                    dipDate = dip.date,
                    confidence = 0.85,
                    reason = "Dip followed by 3-day sustained rise detected"
                )
            }
        }

        return BbtOvulationResult(null, null, 0.0, "No dip + sustained rise pattern found")
    }
}
