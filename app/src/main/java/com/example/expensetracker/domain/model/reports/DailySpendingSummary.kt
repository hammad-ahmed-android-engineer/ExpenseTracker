package com.example.expensetracker.domain.model.reports

data class DailySpendingSummary(
    val date: Long,
    val totalExpense: Long,
    val totalIncome: Long
)
