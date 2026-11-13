package com.example.expensetracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budgets")
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val amountLimit: Long,
    val periodType: String, // MONTHLY, WEEKLY, YEARLY, CUSTOM
    val startDate: Long,
    val endDate: Long,
    val isRolloverEnabled: Boolean = false,
    val notifyThreshold: Int = 80, // Percentage
    val createdAt: Long = System.currentTimeMillis()
)