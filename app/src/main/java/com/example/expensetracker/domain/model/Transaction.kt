package com.example.expensetracker.domain.model

data class Transaction(
    val id: Long = 0,
    val title: String,
    val amount: Long, // minor units
    val type: TransactionType,
    val categoryId: Long,
    val categoryName: String = "",
    val categoryIcon: String = "Category",
    val categoryColor: String = "#4CAF50",
    val subcategoryId: Long? = null,
    val subcategoryName: String? = null,
    val accountId: Long,
    val accountName: String = "",
    val destinationAccountId: Long? = null,
    val destinationAccountName: String? = null,
    val date: Long,
    val paymentMethod: String = "CASH",
    val note: String? = null,
    val location: String? = null,
    val isRecurring: Boolean = false,
    val recurringId: Long? = null,
    val attachmentPath: String? = null,
    val tags: List<String> = emptyList(),
    val originalAmount: Long? = null,
    val originalCurrency: String? = null,
    val exchangeRate: Double? = null,
    val createdAt: Long = System.currentTimeMillis()
)