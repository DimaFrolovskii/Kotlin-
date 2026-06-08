package com.example.projectf.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "settings_prefs")
@Singleton
class SettingsDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        val DAILY_CALORIES_KEY = intPreferencesKey("daily_calories")
    }
    val dailyCaloriesFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[DAILY_CALORIES_KEY] ?: 2000
    }
    suspend fun saveDailyCalories(calories: Int) {
        context.dataStore.edit { preferences ->
            preferences[DAILY_CALORIES_KEY] = calories
        }
    }
}