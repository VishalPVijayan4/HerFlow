package com.buildndeploy.herflow.domain.usecase

import com.buildndeploy.herflow.domain.model.DashboardData
import com.buildndeploy.herflow.domain.repository.CycleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDashboardUseCase @Inject constructor(
    private val repository: CycleRepository
) {
    operator fun invoke(): Flow<DashboardData> = repository.observeDashboard()
}
