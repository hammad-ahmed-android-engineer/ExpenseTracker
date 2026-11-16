package com.example.expensetracker.domain.model.preferences

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
