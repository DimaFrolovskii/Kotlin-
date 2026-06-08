package com.example.projectf.ui.presentation.screens.favorites.uiEvent

import com.example.projectf.data.local.entity.FoodEntity

sealed class FavoritesUiEvent {
    data class OnRemoveFavorite(val foodId: String) : FavoritesUiEvent()
    data class OnConsumeFood(val food: FoodEntity) : FavoritesUiEvent()
}