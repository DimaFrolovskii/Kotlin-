package com.example.projectf.ui.presentation.screens.search.uiEvent

sealed class SearchUiEvent {
    data class OnQueryChanged(val query: String) : SearchUiEvent()
    object OnSearchClicked : SearchUiEvent()
    data class OnToggleFavorite(val foodId: String, val isFavorite: Boolean) : SearchUiEvent()
    data class OnConsumeClick(val food: com.example.projectf.data.local.entity.FoodEntity) : SearchUiEvent()
    object OnRetry : SearchUiEvent()
}