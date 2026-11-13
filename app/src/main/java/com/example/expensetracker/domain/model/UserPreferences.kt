package com.example.expensetracker.domain.model

enum class ThemeMode(val displayName: String) {
    LIGHT("Light"),
    DARK("Dark"),
    SYSTEM("System"),
    AMOLED("AMOLED")
}

enum class ColorPalette(val displayName: String, val hexColor: Long) {
    TERRACOTTA("Terracotta", 0xFFE06D53),
    AMBER("Amber", 0xFFE68B41),
    SAND("Sand", 0xFFE5B54E),
    MINT("Mint", 0xFF8ED6B1),
    SKY_BLUE("Sky", 0xFF6EA8D9),
    LAVENDER("Lavender", 0xFFA78BFA),
    OLIVE("Olive", 0xFFA8B150),
    SAGE("Sage", 0xFF74A99C),
    ROSE("Rose", 0xFFD472A5),
    CRIMSON("Crimson", 0xFFDC5775),
    SLATE("Slate", 0xFF5C677D),
    CHARCOAL("Charcoal", 0xFF2B2D42);

    companion object {
        fun fromString(name: String): ColorPalette = runCatching {
            when (name) {
                "OCEAN_BLUE" -> SKY_BLUE
                "EMERALD_GREEN" -> MINT
                "PURPLE_VIOLET" -> LAVENDER
                "SUNSET_ORANGE" -> AMBER
                "ROSE_PINK" -> ROSE
                "MIDNIGHT" -> CHARCOAL
                "TEAL" -> SAGE
                "MONOCHROME" -> SLATE
                "FOREST_GREEN" -> OLIVE
                "CRIMSON_RED" -> CRIMSON
                else -> valueOf(name)
            }
        }.getOrDefault(LAVENDER)
    }
}

enum class DashboardWidgetType(val key: String, val displayName: String) {
    BALANCE("BALANCE", "Net Balance Hero"),
    QUICK_ACTIONS("QUICK_ACTIONS", "Quick Actions"),
    SPENDING("SPENDING", "Spending Breakdown"),
    TRANSACTIONS("TRANSACTIONS", "Recent Activity"),
    ACCOUNTS("ACCOUNTS", "Accounts Strip"),
    BUDGET("BUDGET", "Budget Health"),
    INSIGHTS("INSIGHTS", "Smart Insights");

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

