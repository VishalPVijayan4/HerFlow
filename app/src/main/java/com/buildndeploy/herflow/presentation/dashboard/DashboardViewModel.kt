package com.buildndeploy.herflow.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buildndeploy.herflow.domain.usecase.GetDashboardUseCase
import com.buildndeploy.herflow.domain.repository.CycleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getDashboardUseCase: GetDashboardUseCase,
    private val repository: CycleRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    init {
        onIntent(DashboardIntent.Refresh)
        observeDashboard()
    }

    fun onIntent(intent: DashboardIntent) {
        when (intent) {
            DashboardIntent.Refresh -> refresh()
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            runCatching { repository.refreshSeedDataIfNeeded() }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    private fun observeDashboard() {
        viewModelScope.launch {
            getDashboardUseCase()
                .catch { throwable ->
                    _state.update {
                        it.copy(isLoading = false, error = throwable.message ?: "Unknown error")
                    }
                }
                .collect { data ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            today = data.today,
                            nextPeriodDate = data.nextPeriodDate,
                            cycleLengthDays = data.cycleLengthDays,
                            entries = data.latestEntries,
                            featureCards = data.features,
                            error = null
                        )
                    }
                }
        }
    }
}
