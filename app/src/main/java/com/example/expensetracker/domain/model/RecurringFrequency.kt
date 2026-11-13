package com.example.expensetracker.domain.model

enum class RecurringFrequency(val displayName: String) {
    DAILY("Daily"),
    WEEKLY("Weekly"),
    BIWEEKLY("Biweekly"),
    MONTHLY("Monthly"),
    QUARTERLY("Quarterly"),
    YEARLY("Yearly");

    companion object {
        fun fromString(freq: String): RecurringFrequency {
            return entries.find { it.name.equals(freq, ignoreCase = true) } ?: MONTHLY
        }
    }
}