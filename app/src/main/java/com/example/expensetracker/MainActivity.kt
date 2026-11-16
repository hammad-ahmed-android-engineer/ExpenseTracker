package com.example.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.expensetracker.core.designsystem.theme.ExpenseTrackerTheme
import com.example.expensetracker.core.navigation.MainAppNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val mainState by mainViewModel.uiState.collectAsStateWithLifecycle()
            val userPrefs = mainState.preferences

            ExpenseTrackerTheme(
                themeMode = userPrefs.themeMode,
                colorPalette = userPrefs.colorPalette,
                dynamicColor = userPrefs.isDynamicColorEnabled
            ) {
                MainAppNavigation()
            }
        }
    }
}
