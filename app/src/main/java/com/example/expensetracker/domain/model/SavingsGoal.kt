package com.example.expensetracker.domain.model

data class SavingsContribution(
    val id: Long = 0,
    val goalId: Long,
    val amount: Long,
    val type: String, // DEPOSIT, WITHDRAWAL
    val date: Long,
    val note: String? = null
)

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
    val remainingAmount: Long get() = (targetAmount - currentAmount).coerceAtLeast(0L)
    val progressPercentage: Float get() = if (targetAmount > 0) (currentAmount.toFloat() / targetAmount.toFloat()).coerceIn(0f, 1f) else 0f

    fun calculateMonthlyRequiredSavings(currentTimeMillis: Long = System.currentTimeMillis()): Long {
        if (isCompleted || currentAmount >= targetAmount) return 0L
        val diffMillis = targetDate - currentTimeMillis
        if (diffMillis <= 0) return remainingAmount
        val daysLeft = diffMillis / (1000 * 60 * 60 * 24)
        val monthsLeft = (daysLeft / 30.0).coerceAtLeast(1.0)
        return (remainingAmount / monthsLeft).toLong()
    }
}