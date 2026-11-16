package com.example.expensetracker.domain.model.recurring

enum class RecurringFrequency(val displayName: String, val defaultIntervalDays: Int) {
    DAILY("Daily", 1),
    WEEKLY("Weekly", 7),
    BIWEEKLY("Bi-weekly", 14),
    MONTHLY("Monthly", 30),
    QUARTERLY("Quarterly", 90),
    YEARLY("Yearly", 365);

    companion object {
        fun fromString(freq: String): RecurringFrequency {
            return entries.find { it.name.equals(freq, ignoreCase = true) } ?: MONTHLY
        }
    }
}
