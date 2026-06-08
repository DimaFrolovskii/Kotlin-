package com.example.projectf.ui.presentation.screens.favorites

import com.example.projectf.data.SettingsDataStore
import com.example.projectf.ui.presentation.screens.favorites.uiEvent.FavoritesUiEvent
import com.example.projectf.ui.presentation.screens.favorites.uiState.FavoritesUiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectf.data.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: FoodRepository,
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {

    private val _state = MutableStateFlow(FavoritesUiState())
    val state: StateFlow<FavoritesUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                repository.getFavoriteFoods(),
                repository.getTodayTotalCalories(),
                settingsDataStore.dailyCaloriesFlow
            ) { favorites, todayCalories, dailyGoal ->
                FavoritesUiState(
                    favoriteFoods = favorites,
                    totalCalories = todayCalories ?: 0,
                    dailyGoal = dailyGoal,
                    isLoading = false
                )
            }.collect { newState ->
                _state.value = newState
            }
        }
    }

    fun onEvent(event: FavoritesUiEvent) {
        when (event) {
            is FavoritesUiEvent.OnRemoveFavorite -> {
                viewModelScope.launch {
                    repository.toggleFavorite(event.foodId, false)
                }
            }
            is FavoritesUiEvent.OnConsumeFood -> {
                viewModelScope.launch {
                    repository.consumeFood(event.food)
                }
            }
        }
    }
}