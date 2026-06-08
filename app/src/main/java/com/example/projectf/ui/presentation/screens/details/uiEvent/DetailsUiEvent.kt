package com.example.projectf.ui.presentation.screens.details.uiEvent
sealed class DetailsUiEvent {
    data class Init(val foodId: String) : DetailsUiEvent()
    object OnToggleFavorite : DetailsUiEvent()
}