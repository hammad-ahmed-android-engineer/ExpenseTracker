package com.example.expensetracker.data.local.model

data class DailySpending(
    val date: Long,
    val totalExpense: Long,
    val totalIncome: Long
)
