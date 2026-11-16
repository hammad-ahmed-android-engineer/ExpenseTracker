package com.example.expensetracker.domain.model.budget

data class Budget(
    val id: Long = 0,
    val name: String,
    val amountLimit: Long,
    val spentAmount: Long = 0L,
    val periodType: BudgetPeriodType,
    val startDate: Long,
    val endDate: Long,
    val isRolloverEnabled: Boolean = false,
    val notifyThreshold: Int = 80,
    val categoryLimits: List<CategoryLimit> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
) {
    val remainingAmount: Long get() = amountLimit - spentAmount
    val progressPercentage: Float get() = if (amountLimit > 0) (spentAmount.toFloat() / amountLimit.toFloat()).coerceIn(0f, 2f) else 0f
    val isExceeded: Boolean get() = spentAmount > amountLimit
    val isWarning: Boolean get() = (progressPercentage * 100) >= notifyThreshold && !isExceeded
}
