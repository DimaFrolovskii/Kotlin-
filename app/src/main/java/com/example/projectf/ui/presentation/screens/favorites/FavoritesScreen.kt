package com.example.projectf.ui.presentation.screens.favorites


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.projectf.data.local.entity.FoodEntity
import com.example.projectf.ui.presentation.screens.favorites.uiEvent.FavoritesUiEvent
import com.example.projectf.ui.presentation.screens.favorites.uiState.FavoritesUiState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    state: FavoritesUiState,
    onEvent: (FavoritesUiEvent) -> Unit,
    onNavigateToDetails: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Избранные продукты") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Calorie Progress
            if (state.dailyGoal > 0) {
                CalorieSummary(
                    totalCalories = state.totalCalories,
                    dailyGoal = state.dailyGoal
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                // ТРЕБОВАНИЕ: Улучшенный экран пустого списка (Empty State)
                if (!state.isLoading && state.favoriteFoods.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "В избранном пока пусто",
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Добавляйте продукты через поиск или экран деталей, чтобы они появились здесь.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = state.favoriteFoods,
                        key = { food -> food.id }
                    ) { food ->
                        FavoriteFoodCard(
                            food = food,
                            onClick = { onNavigateToDetails(food.id) },
                            onRemoveClick = { onEvent(FavoritesUiEvent.OnRemoveFavorite(food.id)) },
                            onAddClick = { onEvent(FavoritesUiEvent.OnConsumeFood(food)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CalorieSummary(totalCalories: Int, dailyGoal: Int) {
    val progress = if (dailyGoal > 0) totalCalories.toFloat() / dailyGoal else 0f
    val color = if (totalCalories > dailyGoal) Color.Red else MaterialTheme.colorScheme.primary

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "Сегодня: $totalCalories ккал",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Цель: $dailyGoal ккал",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { progress.coerceAtMost(1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = color,
                trackColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)
            )
        }
    }
}

@Composable
fun FavoriteFoodCard(
    food: FoodEntity,
    onClick: () -> Unit,
    onRemoveClick: () -> Unit,
    onAddClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = food.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "${food.calories} ккал на 100г",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Row {
                IconButton(onClick = onAddClick) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Добавить в потребление",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = onRemoveClick) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Удалить из избранного",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}