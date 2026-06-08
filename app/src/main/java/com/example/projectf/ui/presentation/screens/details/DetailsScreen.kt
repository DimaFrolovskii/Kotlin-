package com.example.projectf.ui.presentation.screens.details

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.projectf.ui.presentation.screens.details.uiEvent.DetailsUiEvent
import com.example.projectf.ui.presentation.screens.details.uiState.DetailsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    foodId: String,
    state: DetailsUiState,
    onEvent: (DetailsUiEvent) -> Unit,
    onBackPressed: () -> Unit
) {
    // Запускаем инициализацию при первом показе экрана
    LaunchedEffect(foodId) {
        onEvent(DetailsUiEvent.Init(foodId))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.food?.name ?: "Детали продукта") },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    state.food?.let { food ->
                        IconButton(onClick = { onEvent(DetailsUiEvent.OnToggleFavorite) }) {
                            Icon(
                                imageVector = if (food.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "В избранное",
                                tint = if (food.isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.TopCenter
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(top = 32.dp))
            }

            state.error?.let { errorText ->
                Text(
                    text = errorText,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 32.dp)
                )
            }

            state.food?.let { food ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Карточка калорий
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Энергетическая ценность", style = MaterialTheme.typography.labelLarge)
                            Text(
                                text = "${food.calories} ккал",
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text("на 100 грамм продукта", style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    Text("Макронутриенты (БЖУ)", style = MaterialTheme.typography.titleMedium)

                    // Элементы БЖУ
                    NutrientRow(name = "Белки", value = food.protein, color = Color(0xFF4CAF50))
                    NutrientRow(name = "Жиры", value = food.fat, color = Color(0xFFFFC107))
                    NutrientRow(name = "Углеводы", value = food.carbs, color = Color(0xFF2196F3))
                }
            }
        }
    }
}

@Composable
fun NutrientRow(name: String, value: Double, color: Color) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = name, style = MaterialTheme.typography.bodyLarge)
            Text(text = "$value г", style = MaterialTheme.typography.bodyLarge)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
        progress = { (value / 100.0).coerceIn(0.0, 1.0).toFloat() },
        modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
        color = color,
        trackColor = color.copy(alpha = 0.2f),
        strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
        )
    }
}