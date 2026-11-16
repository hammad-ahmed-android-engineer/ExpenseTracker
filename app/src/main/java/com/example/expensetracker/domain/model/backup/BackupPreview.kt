package com.example.expensetracker.domain.model.backup

data class BackupPreview(
    val exportedAt: Long,
    val appVersion: String,
    val accountCount: Int,
    val categoryCount: Int,
    val transactionCount: Int,
    val budgetCount: Int,
    val savingsGoalCount: Int,
    val recurringCount: Int
)
