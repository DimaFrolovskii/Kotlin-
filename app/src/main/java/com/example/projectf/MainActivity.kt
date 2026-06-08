package com.example.projectf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.projectf.ui.presentation.navigation.MainNavigationGraph
import com.example.projectf.ui.theme.ProjectFTheme
import dagger.hilt.android.AndroidEntryPoint

// ИСПРАВЛЕНО: @AndroidEntryPoint обязателен для Activity, использующей Hilt
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjectFTheme {
                MainNavigationGraph()
            }
        }
    }
}