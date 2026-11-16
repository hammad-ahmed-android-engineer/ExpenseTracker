package com.example.expensetracker.domain.model.reports

data class ReportsAnalyticsData(
    val totalIncome: Long,
    val totalExpense: Long,
    val netSavings: Long,
    val categories: List<CategorySpendingSummary>,
    val dailySummaries: List<DailySpendingSummary>
)
