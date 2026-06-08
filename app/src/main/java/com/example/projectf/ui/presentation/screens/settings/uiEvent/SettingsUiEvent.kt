package com.example.projectf.ui.presentation.screens.settings.uiEvent
sealed class SettingsUiEvent {
    data class OnInputChanged(val text: String) : SettingsUiEvent()
    object OnSaveClicked : SettingsUiEvent()
}