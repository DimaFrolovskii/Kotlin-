package com.example.projectf.ui.presentation.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect // ДОБАВИЛИ ИМПОРТ
import androidx.compose.runtime.remember // ДОБАВИЛИ ИМПОРТ
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester // ДОБАВИЛИ ИМПОРТ
import androidx.compose.ui.focus.focusRequester // ДОБАВИЛИ ИМПОРТ
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.projectf.ui.presentation.screens.settings.uiEvent.SettingsUiEvent
import com.example.projectf.ui.presentation.screens.settings.uiState.SettingsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    state: SettingsUiState,
    onEvent: (SettingsUiEvent) -> Unit
) {
    val focusManager = LocalFocusManager.current

    // --- НАЧАЛО ДОБАВЛЕНИЯ ДЛЯ КЛАВИАТУРЫ ---
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        if (state.inputCalories.isNotEmpty()) {
            focusRequester.requestFocus()
        }
    }
    // --- КОНЕЦ ДОБАВЛЕНИЯ ДЛЯ КЛАВИАТУРЫ ---

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Настройки профиля") },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Ваша дневная цель:",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${state.dailyCalories} ккал",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            OutlinedTextField(
                value = state.inputCalories,
                onValueChange = { onEvent(SettingsUiEvent.OnInputChanged(it)) },
                label = { Text("Изменить норму калорий") },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester), // ДОБАВИЛИ ТОЛЬКО ЭТУ СТРОЧКУ СЮДА
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onEvent(SettingsUiEvent.OnSaveClicked)
                        focusManager.clearFocus()
                    }
                )
            )
            Button(
                onClick = { onEvent(SettingsUiEvent.OnSaveClicked) },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Сохранить изменения", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}