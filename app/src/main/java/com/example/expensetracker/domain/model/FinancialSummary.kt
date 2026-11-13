package com.example.expensetracker.domain.model

data class CategorySpendingSummary(
    val categoryId: Long,
    val categoryName: String,
    val categoryColor: String,
    val categoryIcon: String,
    val totalAmount: Long,
    val percentage: Float
)

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
    val totalSpentBudget: Long = 0L
) {
    val remainingBalance: Long get() = monthlyIncome - monthlyExpense
    val savingsRatePercentage: Float get() = if (monthlyIncome > 0) ((netSavings.toFloat() / monthlyIncome.toFloat()) * 100).coerceIn(0f, 100f) else 0f
}