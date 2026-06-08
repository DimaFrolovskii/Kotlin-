package com.example.projectf.ui.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.projectf.ui.presentation.screens.details.DetailsScreen
import com.example.projectf.ui.presentation.screens.details.DetailsViewModel
import com.example.projectf.ui.presentation.screens.favorites.FavoritesScreen
import com.example.projectf.ui.presentation.screens.favorites.FavoritesViewModel
import com.example.projectf.ui.presentation.screens.search.SearchScreen
import com.example.projectf.ui.presentation.screens.search.SearchViewModel
import com.example.projectf.ui.presentation.screens.settings.SettingsScreen
import com.example.projectf.ui.presentation.screens.settings.SettingsViewModel


@Composable
fun MainNavigationGraph() {
    val navController = rememberNavController()
    val bottomNavigationItems = listOf(
        Screen.Search,
        Screen.Favorites,
        Screen.Settings
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            val showBottomBar = bottomNavigationItems.any { it.route == currentRoute }

            if (showBottomBar) {
                NavigationBar {
                    bottomNavigationItems.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon!!, contentDescription = screen.title) },
                            label = { Text(screen.title!!) },
                            selected = currentRoute == screen.route,
                            onClick = {
                                if (currentRoute != screen.route) {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Search.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Search.route) {
                val viewModel: SearchViewModel = hiltViewModel()
                val state by viewModel.state.collectAsStateWithLifecycle()

                SearchScreen(
                    state = state,
                    onEvent = viewModel::onEvent,
                    onNavigateToDetails = { foodId ->
                        navController.navigate(Screen.Details.createRoute(foodId))
                    }
                )
            }
            composable(Screen.Favorites.route) {
                val viewModel: FavoritesViewModel = hiltViewModel()
                val state by viewModel.state.collectAsStateWithLifecycle()

                FavoritesScreen(
                    state = state,
                    onEvent = viewModel::onEvent,
                    onNavigateToDetails = { foodId ->
                        navController.navigate(Screen.Details.createRoute(foodId))
                    }
                )
            }
            composable(Screen.Settings.route) {
                val viewModel: SettingsViewModel = hiltViewModel()
                val state by viewModel.state.collectAsStateWithLifecycle()

                SettingsScreen(
                    state = state,
                    onEvent = viewModel::onEvent
                )
            }
            composable(
                route = Screen.Details.route,
                arguments = listOf(
                    navArgument("foodId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val foodId = backStackEntry.arguments?.getString("foodId") ?: ""

                val viewModel: DetailsViewModel = hiltViewModel()
                val state by viewModel.state.collectAsStateWithLifecycle()

                DetailsScreen(
                    foodId = foodId,
                    state = state,
                    onEvent = viewModel::onEvent,
                    onBackPressed = { navController.popBackStack() }
                )
            }
        }
    }
}