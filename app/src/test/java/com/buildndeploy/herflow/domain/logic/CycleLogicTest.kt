package com.buildndeploy.herflow.domain.logic

import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CycleLogicTest {

    @Test
    fun `cycle analytics calculates length average and irregularity`() {
        val previous = LocalDate.of(2026, 1, 1)
        val current = LocalDate.of(2026, 1, 30)

        val length = CycleAnalytics.cycleLength(previous, current)
        val average = CycleAnalytics.rollingAverageCycleLength(listOf(28, 29, 30, length))

        assertEquals(29, length)
        assertEquals(29, average)
        assertFalse(CycleAnalytics.isIrregularCycle(currentLength = length, averageLength = average))
        assertTrue(CycleAnalytics.isIrregularCycle(currentLength = 37, averageLength = average))
    }

    @Test
    fun `prediction engine returns expected windows`() {
        val engine = CyclePredictionEngine()
        val prediction = engine.predict(
            lastPeriodStart = LocalDate.of(2026, 3, 1),
            averageCycleLength = 28
        )

        assertEquals(LocalDate.of(2026, 3, 29), prediction.nextPeriodDate)
        assertEquals(LocalDate.of(2026, 3, 15), prediction.ovulationDate)
        assertEquals(LocalDate.of(2026, 3, 11), prediction.fertileWindowStart)
        assertEquals(LocalDate.of(2026, 3, 19), prediction.fertileWindowEnd)
        assertEquals(LocalDate.of(2026, 3, 22), prediction.pmsWindowStart)
        assertEquals(LocalDate.of(2026, 3, 28), prediction.pmsWindowEnd)
    }

    @Test
    fun `bbt detector identifies dip followed by sustained rise`() {
        val base = LocalDate.of(2026, 3, 1)
        val readings = listOf(
            BbtReading(base.plusDays(0), 36.50),
            BbtReading(base.plusDays(1), 36.55),
            BbtReading(base.plusDays(2), 36.40), // dip
            BbtReading(base.plusDays(3), 36.75),
            BbtReading(base.plusDays(4), 36.78),
            BbtReading(base.plusDays(5), 36.80)
        )

        val result = BbtOvulationDetector.detectOvulation(readings)

        assertNotNull(result.ovulationDate)
        assertEquals(base.plusDays(2), result.ovulationDate)
        assertTrue(result.confidence > 0.0)
    }

    @Test
    fun `bbt detector returns null when pattern absent`() {
        val base = LocalDate.of(2026, 3, 1)
        val readings = (0..6).map { day ->
            BbtReading(base.plusDays(day.toLong()), 36.55 + (day * 0.01))
        }

        val result = BbtOvulationDetector.detectOvulation(readings)

        assertNull(result.ovulationDate)
    }
}
