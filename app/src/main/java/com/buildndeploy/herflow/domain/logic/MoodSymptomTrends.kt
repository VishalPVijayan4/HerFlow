package com.buildndeploy.herflow.domain.logic

import com.buildndeploy.herflow.domain.model.MoodType
import com.buildndeploy.herflow.domain.model.SymptomType
import java.time.LocalDate

data class DailyMoodSymptomLog(
    val date: LocalDate,
    val cycleDay: Int,
    val mood: MoodType,
    val symptoms: List<SymptomType>
)

data class CycleDayTrend(
    val cycleDay: Int,
    val moodCounts: Map<MoodType, Int>,
    val symptomCounts: Map<SymptomType, Int>,
    val dominantMood: MoodType?,
    val topSymptoms: List<SymptomType>
)

object MoodSymptomTrendCalculator {
    fun groupByCycleDay(logs: List<DailyMoodSymptomLog>): Map<Int, List<DailyMoodSymptomLog>> =
        logs
            .filter { it.cycleDay > 0 }
            .groupBy { it.cycleDay }
            .toSortedMap()

    fun calculateTrends(logs: List<DailyMoodSymptomLog>): List<CycleDayTrend> {
        return groupByCycleDay(logs).map { (cycleDay, dayLogs) ->
            val moodCounts = dayLogs.groupingBy { it.mood }.eachCount()
            val symptomCounts = dayLogs
                .flatMap { it.symptoms }
                .groupingBy { it }
                .eachCount()

            CycleDayTrend(
                cycleDay = cycleDay,
                moodCounts = moodCounts,
                symptomCounts = symptomCounts,
                dominantMood = moodCounts.maxByOrNull { it.value }?.key,
                topSymptoms = symptomCounts.entries
                    .sortedByDescending { it.value }
                    .take(3)
                    .map { it.key }
            )
        }
    }
}
