package com.example.projectf.ui.presentation.screens.favorites.uiEvent
sealed class FavoritesUiEvent {
    data class OnRemoveFavorite(val foodId: String) : FavoritesUiEvent()
}