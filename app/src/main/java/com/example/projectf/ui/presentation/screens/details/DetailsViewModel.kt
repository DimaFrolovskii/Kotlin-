package com.example.projectf.ui.presentation.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectf.data.repository.FoodRepository
import com.example.projectf.ui.presentation.screens.details.uiEvent.DetailsUiEvent
import com.example.projectf.ui.presentation.screens.details.uiState.DetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: FoodRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DetailsUiState())
    val state: StateFlow<DetailsUiState> = _state.asStateFlow()

    private var currentFoodId: String? = null

    fun onEvent(event: DetailsUiEvent) {
        when (event) {
            is DetailsUiEvent.Init -> {
                if (currentFoodId == null) {
                    currentFoodId = event.foodId
                    observeFood(event.foodId)
                }
            }
            is DetailsUiEvent.OnToggleFavorite -> {
                toggleFavorite()
            }
        }
    }

    private fun observeFood(foodId: String) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            repository.getFoodById(foodId).collect { localFood ->
                if (localFood != null) {
                    _state.update { it.copy(food = localFood, isLoading = false, error = null) }
                } else {
                    _state.update { it.copy(isLoading = false, error = "Продукт не найден") }
                }
            }
        }
    }

    private fun toggleFavorite() {
        val currentFood = _state.value.food ?: return
        viewModelScope.launch {
            repository.toggleFavorite(currentFood.id, !currentFood.isFavorite)
        }
    }
}