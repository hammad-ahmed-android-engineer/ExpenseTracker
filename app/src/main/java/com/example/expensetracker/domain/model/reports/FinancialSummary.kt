package com.example.expensetracker.domain.model.reports

data class FinancialSummary(
    val totalBalance: Long = 0L,
    val monthlyIncome: Long = 0L,
    val monthlyExpense: Long = 0L,
    val netSavings: Long = 0L,
    val previousMonthIncomeDeltaPercentage: Float = 0f,
    val previousMonthExpenseDeltaPercentage: Float = 0f,
    val categorySpendingList: List<CategorySpendingSummary> = emptyList(),
    val highestSpendingCategoryName: String? = null,
    val totalMonthlyBudget: Long = 0L,
    val totalSpentBudget: Long = 0L,
    val overallBudgetProgress: Float = 0f,
    val isBudgetWarning: Boolean = false,
    val isBudgetExceeded: Boolean = false,
    val dailySpendingTrend: List<DailySpendingSummary> = emptyList()
)
