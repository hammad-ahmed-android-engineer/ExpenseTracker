package com.example.expensetracker.domain.model

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

enum class BackupRestoreMode {
    REPLACE_ALL,
    MERGE_SKIP_DUPLICATES
}

