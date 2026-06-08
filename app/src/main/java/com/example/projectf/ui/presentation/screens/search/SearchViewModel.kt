package com.example.projectf.ui.presentation.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectf.data.SettingsDataStore
import com.example.projectf.data.repository.FoodRepository
import com.example.projectf.ui.presentation.screens.search.uiEvent.SearchUiEvent
import com.example.projectf.ui.presentation.screens.search.uiState.SearchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: FoodRepository,
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {

    private val _state = MutableStateFlow(SearchUiState())
    val state: StateFlow<SearchUiState> = _state.asStateFlow()

    init {
        settingsDataStore.dailyCaloriesFlow
            .onEach { calories ->
                _state.update { it.copy(dailyCalories = calories) }
            }
            .launchIn(viewModelScope)

        repository.getTodayTotalCalories()
            .onEach { consumed ->
                _state.update { it.copy(consumedCalories = consumed ?: 0) }
            }
            .launchIn(viewModelScope)
    }

    private var localSearchJob: Job? = null

    fun onEvent(event: SearchUiEvent) {
        when (event) {
            is SearchUiEvent.OnQueryChanged -> {
                _state.update { it.copy(searchQuery = event.query) }
            }
            is SearchUiEvent.OnSearchClicked -> {
                performSearch(_state.value.searchQuery)
            }
            is SearchUiEvent.OnToggleFavorite -> {
                viewModelScope.launch {
                    repository.toggleFavorite(event.foodId, event.isFavorite)
                }
            }
            is SearchUiEvent.OnConsumeClick -> {
                viewModelScope.launch {
                    repository.consumeFood(event.food)
                }
            }
            is SearchUiEvent.OnRetry -> {
                performSearch(_state.value.searchQuery)
            }
        }
    }

    private fun performSearch(query: String) {
        if (query.isBlank()) return

        localSearchJob?.cancel()

        localSearchJob = repository.searchFoodsLocal(query)
            .onEach { localFoods ->
                _state.update { it.copy(foods = localFoods) }
            }
            .launchIn(viewModelScope)

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                repository.fetchAndCacheFoods(query)
                _state.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Ошибка сети. Показаны сохранённые данные."
                    )
                }
            }
        }
    }
}