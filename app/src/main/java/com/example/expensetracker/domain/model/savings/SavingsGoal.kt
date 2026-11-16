package com.example.expensetracker.domain.model.savings

data class SavingsGoal(
    val id: Long = 0,
    val title: String,
    val targetAmount: Long,
    val currentAmount: Long = 0L,
    val targetDate: Long,
    val iconName: String = "Savings",
    val colorHex: String = "#4CAF50",
    val description: String? = null,
    val linkedAccountId: Long? = null,
    val isCompleted: Boolean = false,
    val contributions: List<SavingsContribution> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
) {
    val progressPercentage: Float get() = if (targetAmount > 0) (currentAmount.toFloat() / targetAmount.toFloat()).coerceIn(0f, 1f) else 0f
    val remainingAmount: Long get() = (targetAmount - currentAmount).coerceAtLeast(0L)
    val isReached: Boolean get() = currentAmount >= targetAmount
}
