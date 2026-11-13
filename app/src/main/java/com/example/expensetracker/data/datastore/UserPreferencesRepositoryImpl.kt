package com.example.expensetracker.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.expensetracker.domain.model.ColorPalette
import com.example.expensetracker.domain.model.DashboardWidgetType
import com.example.expensetracker.domain.model.ThemeMode
import com.example.expensetracker.domain.model.UserPreferences
import com.example.expensetracker.domain.repository.UserPreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferencesRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : UserPreferencesRepository {

    private object PreferenceKeys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val COLOR_PALETTE = stringPreferencesKey("color_palette")
        val DYNAMIC_COLOR = booleanPreferencesKey("dynamic_color")
        val DEFAULT_CURRENCY = stringPreferencesKey("default_currency")
        val BUDGET_ALERT = booleanPreferencesKey("budget_alert")
        val RECURRING_REMINDER = booleanPreferencesKey("recurring_reminder")
        val BUDGET_THRESHOLD = intPreferencesKey("budget_threshold")
        val SAMPLE_DATA_SEEDED = booleanPreferencesKey("sample_data_seeded")
        val COMPACT_DASHBOARD = booleanPreferencesKey("compact_dashboard")
        val INSIGHTS_ENABLED = booleanPreferencesKey("insights_enabled")
        val DASHBOARD_WIDGET_ORDER = stringPreferencesKey("dashboard_widget_order")
        val HIDDEN_DASHBOARD_WIDGETS = stringPreferencesKey("hidden_dashboard_widgets")
    }

    private val defaultOrder = DashboardWidgetType.defaultOrder.joinToString(",") { it.key }

    override val userPreferencesFlow: Flow<UserPreferences> = context.dataStore.data.map { preferences ->
        val themeStr = preferences[PreferenceKeys.THEME_MODE] ?: ThemeMode.DARK.name
        val colorStr = preferences[PreferenceKeys.COLOR_PALETTE] ?: ColorPalette.LAVENDER.name

        UserPreferences(
            themeMode = runCatching { ThemeMode.valueOf(themeStr) }.getOrDefault(ThemeMode.DARK),
            colorPalette = ColorPalette.fromString(colorStr),
            isDynamicColorEnabled = preferences[PreferenceKeys.DYNAMIC_COLOR] ?: false,
            defaultCurrency = preferences[PreferenceKeys.DEFAULT_CURRENCY] ?: "USD",
            isBudgetAlertEnabled = preferences[PreferenceKeys.BUDGET_ALERT] ?: true,
            isRecurringReminderEnabled = preferences[PreferenceKeys.RECURRING_REMINDER] ?: true,
            budgetWarningThreshold = preferences[PreferenceKeys.BUDGET_THRESHOLD] ?: 80,
            isSampleDataSeeded = preferences[PreferenceKeys.SAMPLE_DATA_SEEDED] ?: false,
            isCompactDashboard = preferences[PreferenceKeys.COMPACT_DASHBOARD] ?: false,
            isInsightsEnabled = preferences[PreferenceKeys.INSIGHTS_ENABLED] ?: true,
            dashboardWidgetOrder = preferences[PreferenceKeys.DASHBOARD_WIDGET_ORDER] ?: defaultOrder,
            hiddenDashboardWidgets = preferences[PreferenceKeys.HIDDEN_DASHBOARD_WIDGETS] ?: ""
        )
    }

    override suspend fun updateThemeMode(themeMode: ThemeMode) {
        context.dataStore.edit { it[PreferenceKeys.THEME_MODE] = themeMode.name }
    }

    override suspend fun updateColorPalette(palette: ColorPalette) {
        context.dataStore.edit { it[PreferenceKeys.COLOR_PALETTE] = palette.name }
    }

    override suspend fun updateDynamicColor(enabled: Boolean) {
        context.dataStore.edit { it[PreferenceKeys.DYNAMIC_COLOR] = enabled }
    }

    override suspend fun updateDefaultCurrency(currency: String) {
        context.dataStore.edit { it[PreferenceKeys.DEFAULT_CURRENCY] = currency }
    }

    override suspend fun updateBudgetAlert(enabled: Boolean) {
        context.dataStore.edit { it[PreferenceKeys.BUDGET_ALERT] = enabled }
    }

    override suspend fun updateRecurringReminder(enabled: Boolean) {
        context.dataStore.edit { it[PreferenceKeys.RECURRING_REMINDER] = enabled }
    }

    override suspend fun updateBudgetWarningThreshold(threshold: Int) {
        context.dataStore.edit { it[PreferenceKeys.BUDGET_THRESHOLD] = threshold }
    }

    override suspend fun updateSampleDataSeeded(seeded: Boolean) {
        context.dataStore.edit { it[PreferenceKeys.SAMPLE_DATA_SEEDED] = seeded }
    }

    override suspend fun updateDashboardWidgetOrder(order: String) {
        context.dataStore.edit { it[PreferenceKeys.DASHBOARD_WIDGET_ORDER] = order }
    }

    override suspend fun updateHiddenDashboardWidgets(hidden: String) {
        context.dataStore.edit { it[PreferenceKeys.HIDDEN_DASHBOARD_WIDGETS] = hidden }
    }

    override suspend fun updateCompactDashboard(compact: Boolean) {
        context.dataStore.edit { it[PreferenceKeys.COMPACT_DASHBOARD] = compact }
    }

    override suspend fun updateInsightsEnabled(enabled: Boolean) {
        context.dataStore.edit { it[PreferenceKeys.INSIGHTS_ENABLED] = enabled }
    }
}

