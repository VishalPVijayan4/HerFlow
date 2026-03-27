package com.buildndeploy.herflow.presentation.cyclelog

import com.buildndeploy.herflow.domain.model.CervicalMucusType
import com.buildndeploy.herflow.domain.model.FlowLevel
import com.buildndeploy.herflow.domain.model.MoodType
import com.buildndeploy.herflow.domain.model.SymptomType
import com.buildndeploy.herflow.presentation.mvi.MviEffect
import com.buildndeploy.herflow.presentation.mvi.MviIntent
import com.buildndeploy.herflow.presentation.mvi.MviState
import java.time.LocalDate

sealed interface CycleLogIntent : MviIntent {
    data class UpdateStartDate(val date: LocalDate) : CycleLogIntent
    data class UpdateEndDate(val date: LocalDate?) : CycleLogIntent
    data class UpdateFlow(val flowLevel: FlowLevel) : CycleLogIntent
    data class UpdateMood(val moodType: MoodType) : CycleLogIntent
    data class ToggleSymptom(val symptomType: SymptomType) : CycleLogIntent
    data class UpdateCervicalMucus(val type: CervicalMucusType?) : CycleLogIntent
    data class UpdateNotes(val notes: String) : CycleLogIntent
    data object Save : CycleLogIntent
}

data class CycleLogState(
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val flowLevel: FlowLevel = FlowLevel.MEDIUM,
    val moodType: MoodType = MoodType.CALM,
    val symptoms: Set<SymptomType> = emptySet(),
    val cervicalMucusType: CervicalMucusType? = null,
    val notes: String = "",
    val isSaving: Boolean = false,
    val validationError: String? = null
) : MviState

sealed interface CycleLogEffect : MviEffect {
    data object Saved : CycleLogEffect
}
