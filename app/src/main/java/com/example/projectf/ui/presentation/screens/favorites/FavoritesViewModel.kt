package com.example.projectf.ui.presentation.screens.favorites

import com.example.projectf.ui.presentation.screens.favorites.uiEvent.FavoritesUiEvent
import com.example.projectf.ui.presentation.screens.favorites.uiState.FavoritesUiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectf.data.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: FoodRepository
) : ViewModel() {

    private val _state = MutableStateFlow(FavoritesUiState())
    val state: StateFlow<FavoritesUiState> = _state.asStateFlow()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            repository.getFavoriteFoods().collect { localFavorites ->
                _state.update {
                    it.copy(
                        favoriteFoods = localFavorites,
                        isLoading = false
                    )
                }
            }
        }
    }
    fun onEvent(event: FavoritesUiEvent) {
        when (event) {
            is FavoritesUiEvent.OnRemoveFavorite -> {
                viewModelScope.launch {
                    // Передаем false, так как пользователь удаляет из избранного
                    repository.toggleFavorite(event.foodId, false)
                }
            }
        }
    }
}