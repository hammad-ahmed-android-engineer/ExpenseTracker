package com.example.expensetracker.domain.model.savings

data class SavingsContribution(
    val id: Long = 0,
    val goalId: Long,
    val amount: Long,
    val type: String = "DEPOSIT",
    val date: Long = System.currentTimeMillis(),
    val note: String? = null
)
