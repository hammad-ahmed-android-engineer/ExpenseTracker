package com.example.expensetracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "savings_goals")
data class SavingsGoalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val targetAmount: Long,
    val currentAmount: Long = 0L,
    val targetDate: Long,
    val iconName: String = "Savings",
    val colorHex: String = "#4CAF50",
    val description: String? = null,
    val linkedAccountId: Long? = null,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)