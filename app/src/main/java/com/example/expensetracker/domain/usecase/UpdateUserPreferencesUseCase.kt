package com.example.expensetracker.domain.usecase

import com.example.expensetracker.domain.model.ColorPalette
import com.example.expensetracker.domain.model.ThemeMode
import com.example.expensetracker.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class UpdateUserPreferencesUseCase @Inject constructor(
    private val repository: UserPreferencesRepository
) {
    suspend fun updateThemeMode(themeMode: ThemeMode) = repository.updateThemeMode(themeMode)
    suspend fun updateColorPalette(palette: ColorPalette) = repository.updateColorPalette(palette)
    suspend fun updateDynamicColor(enabled: Boolean) = repository.updateDynamicColor(enabled)
    suspend fun updateDefaultCurrency(currency: String) = repository.updateDefaultCurrency(currency)
    suspend fun updateBudgetAlert(enabled: Boolean) = repository.updateBudgetAlert(enabled)
    suspend fun updateRecurringReminder(enabled: Boolean) = repository.updateRecurringReminder(enabled)
    suspend fun updateBudgetWarningThreshold(threshold: Int) = repository.updateBudgetWarningThreshold(threshold)
    suspend fun updateSampleDataSeeded(seeded: Boolean) = repository.updateSampleDataSeeded(seeded)
    suspend fun updateDashboardWidgetOrder(order: String) = repository.updateDashboardWidgetOrder(order)
    suspend fun updateHiddenDashboardWidgets(hidden: String) = repository.updateHiddenDashboardWidgets(hidden)
    suspend fun updateCompactDashboard(compact: Boolean) = repository.updateCompactDashboard(compact)
    suspend fun updateInsightsEnabled(enabled: Boolean) = repository.updateInsightsEnabled(enabled)
}

