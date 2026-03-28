package com.buildndeploy.herflow.domain.logic

enum class CyclePhase {
    MENSTRUAL,
    FOLLICULAR,
    OVULATORY,
    LUTEAL
}

data class PhaseInsight(
    val phase: CyclePhase,
    val tips: List<String>
)

object CycleInsightsEngine {
    fun detectPhase(cycleDay: Int, averageCycleLength: Int): CyclePhase {
        if (cycleDay <= 5) return CyclePhase.MENSTRUAL

        val ovulationDay = (averageCycleLength - 14).coerceAtLeast(10)
        return when {
            cycleDay in 6 until (ovulationDay - 2) -> CyclePhase.FOLLICULAR
            cycleDay in (ovulationDay - 2)..(ovulationDay + 1) -> CyclePhase.OVULATORY
            else -> CyclePhase.LUTEAL
        }
    }

    fun phaseInsights(cycleDay: Int, averageCycleLength: Int): PhaseInsight {
        val phase = detectPhase(cycleDay, averageCycleLength)
        val tips = when (phase) {
            CyclePhase.MENSTRUAL -> listOf(
                "Prioritize iron-rich foods and hydration.",
                "Choose gentle movement like walking or stretching."
            )

            CyclePhase.FOLLICULAR -> listOf(
                "Energy may rise—good time for planning and training.",
                "Include protein and fiber for stable energy."
            )

            CyclePhase.OVULATORY -> listOf(
                "Track cervical mucus and hydration closely.",
                "Maintain sleep routine to support hormonal balance."
            )

            CyclePhase.LUTEAL -> listOf(
                "Support PMS with magnesium-rich foods.",
                "Lower caffeine and prioritize recovery days."
            )
        }
        return PhaseInsight(phase = phase, tips = tips)
    }
}
