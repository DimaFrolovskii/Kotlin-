package com.example.projectf.ui.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectf.data.SettingsDataStore
import com.example.projectf.ui.presentation.screens.settings.uiEvent.SettingsUiEvent
import com.example.projectf.ui.presentation.screens.settings.uiState.SettingsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            settingsDataStore.dailyCaloriesFlow.collect { calories ->
                _state.update {
                    it.copy(
                        dailyCalories = calories,
                        inputCalories = calories.toString()
                    )
                }
            }
        }
    }

    fun onEvent(event: SettingsUiEvent) {
        when (event) {
            is SettingsUiEvent.OnInputChanged -> {
                if (event.text.all { it.isDigit() }) {
                    _state.update { it.copy(inputCalories = event.text) }
                }
            }
            is SettingsUiEvent.OnSaveClicked -> {
                val caloriesToSave = _state.value.inputCalories.toIntOrNull() ?: 2000
                viewModelScope.launch {
                    settingsDataStore.saveDailyCalories(caloriesToSave)
                }
            }
        }
    }
}