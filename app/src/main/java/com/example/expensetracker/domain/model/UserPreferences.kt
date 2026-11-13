package com.example.expensetracker.domain.model

enum class ThemeMode(val displayName: String) {
    SYSTEM("System"),
    LIGHT("Light"),
    DARK("Dark"),
    AMOLED("AMOLED")
}

enum class ColorPalette(val displayName: String) {
    OCEAN_BLUE("Ocean Blue"),
    EMERALD_GREEN("Emerald"),
    PURPLE_VIOLET("Amethyst"),
    SUNSET_ORANGE("Sunset"),
    ROSE_PINK("Rose"),
    MIDNIGHT("Midnight"),
    TEAL("Teal"),
    MONOCHROME("Slate"),
    FOREST_GREEN("Forest"),
    CRIMSON_RED("Crimson")
}

enum class DashboardWidgetType(val key: String, val displayName: String) {
    BALANCE("BALANCE", "Net Balance Hero"),
    QUICK_ACTIONS("QUICK_ACTIONS", "Quick Actions"),
    ACCOUNTS("ACCOUNTS", "Accounts Strip"),
    SPENDING("SPENDING", "Spending Breakdown"),
    BUDGET("BUDGET", "Budget Health"),
    INSIGHTS("INSIGHTS", "Smart Insights"),
    TRANSACTIONS("TRANSACTIONS", "Recent Activity");

    companion object {
        val defaultOrder: List<DashboardWidgetType> = entries.toList()

        fun parseOrder(raw: String): List<DashboardWidgetType> {
            val parsed = raw.split(",")
                .mapNotNull { token -> entries.find { it.key == token.trim() } }
                .distinct()
            return parsed.ifEmpty { defaultOrder }
        }
    }
}

data class UserPreferences(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val colorPalette: ColorPalette = ColorPalette.EMERALD_GREEN,
    val isDynamicColorEnabled: Boolean = false,
    val defaultCurrency: String = "USD",
    val isBudgetAlertEnabled: Boolean = true,
    val isRecurringReminderEnabled: Boolean = true,
    val budgetWarningThreshold: Int = 80,
    val isSampleDataSeeded: Boolean = false,
    val isCompactDashboard: Boolean = false,
    val isInsightsEnabled: Boolean = true,
    val dashboardWidgetOrder: String = DashboardWidgetType.defaultOrder.joinToString(",") { it.key },
    val hiddenDashboardWidgets: String = ""
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

