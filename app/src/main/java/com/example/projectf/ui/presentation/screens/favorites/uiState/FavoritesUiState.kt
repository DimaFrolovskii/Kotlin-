package com.example.projectf.ui.presentation.screens.favorites.uiState

import com.example.projectf.data.local.entity.FoodEntity


data class FavoritesUiState(
    val isLoading: Boolean = false,
    val favoriteFoods: List<FoodEntity> = emptyList()
)