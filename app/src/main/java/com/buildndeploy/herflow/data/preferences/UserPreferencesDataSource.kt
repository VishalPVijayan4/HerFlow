package com.buildndeploy.herflow.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "herflow_user_prefs")

@Singleton
class UserPreferencesDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val avgCycleLength = intPreferencesKey("avg_cycle_length")
        val isOnboardingComplete = booleanPreferencesKey("onboarding_complete")
    }

    val cycleLengthFlow: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[Keys.avgCycleLength] ?: 28
    }

    val onboardingFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.isOnboardingComplete] ?: false
    }

    suspend fun markOnboardingComplete() {
        context.dataStore.edit { prefs ->
            prefs[Keys.isOnboardingComplete] = true
        }
    }
}
