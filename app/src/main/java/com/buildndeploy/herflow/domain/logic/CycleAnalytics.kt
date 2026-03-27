package com.buildndeploy.herflow.domain.logic

import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.math.abs

object CycleAnalytics {
    fun cycleLength(previousStart: LocalDate, currentStart: LocalDate): Int {
        require(!currentStart.isBefore(previousStart)) { "currentStart must be on or after previousStart" }
        return ChronoUnit.DAYS.between(previousStart, currentStart).toInt()
    }

    fun rollingAverageCycleLength(cycleLengths: List<Int>, windowSize: Int = 6): Int {
        require(windowSize > 0) { "windowSize must be > 0" }
        val recent = cycleLengths.takeLast(windowSize).filter { it > 0 }
        if (recent.isEmpty()) return 28
        return recent.average().toInt()
    }

    fun isIrregularCycle(currentLength: Int, averageLength: Int, thresholdDays: Int = 5): Boolean {
        require(currentLength > 0) { "currentLength must be > 0" }
        require(averageLength > 0) { "averageLength must be > 0" }
        return abs(currentLength - averageLength) > thresholdDays
    }

    fun cycleLengthsFromStarts(starts: List<LocalDate>): List<Int> {
        val sortedStarts = starts.sorted()
        if (sortedStarts.size < 2) return emptyList()
        return sortedStarts.zipWithNext { prev, current -> cycleLength(prev, current) }
    }
}
