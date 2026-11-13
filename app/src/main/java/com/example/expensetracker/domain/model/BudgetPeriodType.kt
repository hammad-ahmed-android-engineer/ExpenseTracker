package com.example.expensetracker.domain.model

enum class BudgetPeriodType(val displayName: String) {
    WEEKLY("Weekly"),
    MONTHLY("Monthly"),
    YEARLY("Yearly"),
    CUSTOM("Custom Range");

    companion object {
        fun fromString(type: String): BudgetPeriodType {
            return entries.find { it.name.equals(type, ignoreCase = true) } ?: MONTHLY
        }
    }
}