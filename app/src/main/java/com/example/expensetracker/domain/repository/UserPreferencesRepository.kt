package com.example.expensetracker.domain.repository

import com.example.expensetracker.domain.model.ColorPalette
import com.example.expensetracker.domain.model.ThemeMode
import com.example.expensetracker.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val userPreferencesFlow: Flow<UserPreferences>
    suspend fun updateThemeMode(themeMode: ThemeMode)
    suspend fun updateColorPalette(palette: ColorPalette)
    suspend fun updateDynamicColor(enabled: Boolean)
    suspend fun updateDefaultCurrency(currency: String)
    suspend fun updateBudgetAlert(enabled: Boolean)
    suspend fun updateRecurringReminder(enabled: Boolean)
    suspend fun updateBudgetWarningThreshold(threshold: Int)
    suspend fun updateSampleDataSeeded(seeded: Boolean)
    suspend fun updateDashboardWidgetOrder(order: String)
    suspend fun updateHiddenDashboardWidgets(hidden: String)
    suspend fun updateCompactDashboard(compact: Boolean)
    suspend fun updateInsightsEnabled(enabled: Boolean)
}

