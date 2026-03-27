package com.buildndeploy.herflow.domain.repository

import com.buildndeploy.herflow.domain.model.DashboardData
import kotlinx.coroutines.flow.Flow

interface CycleRepository {
    fun observeDashboard(): Flow<DashboardData>
    suspend fun refreshSeedDataIfNeeded()
}
