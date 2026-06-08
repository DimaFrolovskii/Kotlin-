package com.example.projectf.ui.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String? = null, val icon: ImageVector? = null) {
    object Search : Screen("search_screen", "Поиск", Icons.Default.Search)
    object Favorites : Screen("favorites_screen", "Избранное", Icons.Default.Favorite)
    object Settings : Screen("settings_screen", "Настройки", Icons.Default.Settings)
    object Details : Screen("details_screen/{foodId}") {
        fun createRoute(foodId: String) = "details_screen/$foodId"
    }
}