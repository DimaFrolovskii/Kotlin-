package com.example.projectf.ui.presentation.screens.details.uiState

import com.example.projectf.data.local.entity.FoodEntity

data class DetailsUiState(
    val isLoading: Boolean = false,
    val food: FoodEntity? = null,
    val error: String? = null
)