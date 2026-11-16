package com.example.expensetracker.domain.model.account

data class Account(
    val id: Long = 0,
    val name: String,
    val type: AccountType,
    val initialBalance: Long,
    val currentBalance: Long,
    val currency: String = "USD",
    val colorHex: String = "#2196F3",
    val iconName: String = "AccountBalance",
    val isArchived: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
