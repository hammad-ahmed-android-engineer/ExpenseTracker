package com.example.expensetracker.domain.model

data class RecurringTransaction(
    val id: Long = 0,
    val title: String,
    val amount: Long,
    val type: TransactionType,
    val categoryId: Long,
    val categoryName: String = "",
    val categoryIcon: String = "Category",
    val categoryColor: String = "#4CAF50",
    val accountId: Long,
    val accountName: String = "",
    val destinationAccountId: Long? = null,
    val destinationAccountName: String? = null,
    val frequency: RecurringFrequency,
    val intervalDays: Int = 1,
    val startDate: Long,
    val endDate: Long? = null,
    val lastExecutedDate: Long? = null,
    val nextDueDate: Long,
    val isEnabled: Boolean = true,
    val note: String? = null
)