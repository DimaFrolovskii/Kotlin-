package com.example.projectf.ui.presentation.screens.search.uiState

import com.example.projectf.data.local.entity.FoodEntity

data class SearchUiState(
    val isLoading: Boolean = false,
    val foods: List<FoodEntity> = emptyList(),
    val error: String? = null,
    val searchQuery: String = "",
    val dailyCalories: Int = 2000,
    val consumedCalories: Int = 0
)