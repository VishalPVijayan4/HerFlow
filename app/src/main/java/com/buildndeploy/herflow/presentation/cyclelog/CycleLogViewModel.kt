package com.buildndeploy.herflow.presentation.cyclelog

import androidx.lifecycle.viewModelScope
import com.buildndeploy.herflow.presentation.mvi.BaseMviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltViewModel
class CycleLogViewModel @Inject constructor() :
    BaseMviViewModel<CycleLogIntent, CycleLogState, CycleLogEffect>(CycleLogState()) {

    override suspend fun handleIntent(intent: CycleLogIntent) {
        when (intent) {
            is CycleLogIntent.UpdateStartDate -> {
                updateState { it.copy(startDate = intent.date, validationError = null) }
            }

            is CycleLogIntent.UpdateEndDate -> {
                updateState { it.copy(endDate = intent.date, validationError = null) }
            }

            is CycleLogIntent.UpdateFlow -> {
                updateState { it.copy(flowLevel = intent.flowLevel) }
            }

            is CycleLogIntent.UpdateMood -> {
                updateState { it.copy(moodType = intent.moodType) }
            }

            is CycleLogIntent.ToggleSymptom -> {
                updateState { state ->
                    val next = state.symptoms.toMutableSet().apply {
                        if (contains(intent.symptomType)) remove(intent.symptomType) else add(intent.symptomType)
                    }
                    state.copy(symptoms = next)
                }
            }

            is CycleLogIntent.UpdateCervicalMucus -> {
                updateState { it.copy(cervicalMucusType = intent.type) }
            }

            is CycleLogIntent.UpdateNotes -> {
                updateState { it.copy(notes = intent.notes) }
            }

            CycleLogIntent.Save -> saveEntry()
        }
    }

    private fun saveEntry() {
        val startDate = state.value.startDate
        if (startDate == null) {
            updateState { it.copy(validationError = "Start date is required") }
            return
        }

        val endDate = state.value.endDate
        if (endDate != null && endDate.isBefore(startDate)) {
            updateState { it.copy(validationError = "End date cannot be before start date") }
            return
        }

        updateState { it.copy(isSaving = true, validationError = null) }
        viewModelScope.launch {
            delay(150)
            updateState { it.copy(isSaving = false) }
            postEffect(CycleLogEffect.Saved)
        }
    }
}
