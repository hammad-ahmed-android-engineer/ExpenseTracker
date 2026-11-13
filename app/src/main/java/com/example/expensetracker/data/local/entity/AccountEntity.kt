package com.example.expensetracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val type: String, // CASH, BANK_ACCOUNT, SAVINGS_ACCOUNT, CREDIT_CARD, DEBIT_CARD, DIGITAL_WALLET, OTHER
    val initialBalance: Long,
    val currentBalance: Long,
    val currency: String = "USD",
    val colorHex: String = "#2196F3",
    val iconName: String = "AccountBalance",
    val isArchived: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)