package com.example.expensetracker.data.local.entity.account

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val type: String,
    val initialBalance: Long,
    val currentBalance: Long,
    val currency: String = "USD",
    val colorHex: String = "#2196F3",
    val iconName: String = "AccountBalance",
    val isArchived: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
