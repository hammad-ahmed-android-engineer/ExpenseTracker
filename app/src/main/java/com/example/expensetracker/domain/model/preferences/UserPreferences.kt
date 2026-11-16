package com.example.expensetracker.domain.model.preferences

data class UserPreferences(
    val themeMode: ThemeMode = ThemeMode.DARK,
    val colorPalette: ColorPalette = ColorPalette.LAVENDER,
    val isDynamicColorEnabled: Boolean = false,
    val defaultCurrency: String = "USD",
    val isBudgetAlertEnabled: Boolean = true,
    val isRecurringReminderEnabled: Boolean = true,
    val budgetWarningThreshold: Int = 80,
    val isSampleDataSeeded: Boolean = false,
    val isCompactDashboard: Boolean = false,
    val isInsightsEnabled: Boolean = true,
    val dashboardWidgetOrder: String = DashboardWidgetType.defaultOrder.joinToString(",") { it.key },
    val hiddenDashboardWidgets: String = "ACCOUNTS,BUDGET,INSIGHTS"
) {
    fun visibleWidgets(): List<DashboardWidgetType> {
        val hidden = hiddenDashboardWidgets.split(",").map { it.trim() }.filter { it.isNotEmpty() }.toSet()
        return DashboardWidgetType.parseOrder(dashboardWidgetOrder).filterNot { it.key in hidden }
    }

    fun orderedWidgets(): List<DashboardWidgetType> {
        val parsed = DashboardWidgetType.parseOrder(dashboardWidgetOrder)
        val missing = DashboardWidgetType.entries.filterNot { it in parsed }
        return parsed + missing
    }
}
