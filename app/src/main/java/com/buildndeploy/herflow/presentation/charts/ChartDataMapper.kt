package com.buildndeploy.herflow.presentation.charts

import com.buildndeploy.herflow.domain.logic.BbtReading
import com.buildndeploy.herflow.domain.logic.CycleDayTrend
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

object ChartDataMapper {
    fun bbtLineData(readings: List<BbtReading>): LineData {
        val entries = readings.sortedBy { it.date }.mapIndexed { index, reading ->
            Entry(index.toFloat(), reading.temperatureCelsius.toFloat())
        }

        val dataSet = LineDataSet(entries, "BBT").apply {
            setDrawCircles(true)
            setDrawValues(false)
            lineWidth = 2f
        }
        return LineData(dataSet)
    }

    fun moodTrendBarData(trends: List<CycleDayTrend>): BarData {
        val entries = trends.map { trend ->
            val score = trend.moodCounts.values.sum().toFloat()
            BarEntry(trend.cycleDay.toFloat(), score)
        }

        val dataSet = BarDataSet(entries, "Mood Logs by Cycle Day").apply {
            setDrawValues(false)
        }
        return BarData(dataSet)
    }

    fun cycleLengthLineData(cycleLengths: List<Int>): LineData {
        val entries = cycleLengths.mapIndexed { index, value ->
            Entry(index.toFloat(), value.toFloat())
        }

        val dataSet = LineDataSet(entries, "Cycle Length").apply {
            setDrawValues(false)
            lineWidth = 2f
        }
        return LineData(dataSet)
    }
}
