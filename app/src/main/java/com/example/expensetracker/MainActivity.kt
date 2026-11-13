package com.example.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.expensetracker.core.designsystem.theme.ExpenseTrackerTheme
import com.example.expensetracker.core.navigation.MainAppNavigation
import com.example.expensetracker.domain.model.UserPreferences
import com.example.expensetracker.domain.repository.UserPreferencesRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val userPrefs by userPreferencesRepository.userPreferencesFlow.collectAsState(
                initial = UserPreferences()
            )

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
