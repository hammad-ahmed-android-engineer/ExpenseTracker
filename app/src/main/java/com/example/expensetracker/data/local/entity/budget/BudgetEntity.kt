package com.example.expensetracker.data.local.entity.budget

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budgets")
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val amountLimit: Long,
    val periodType: String,
    val startDate: Long,
    val endDate: Long,
    val isRolloverEnabled: Boolean = false,
    val notifyThreshold: Int = 80,
    val createdAt: Long = System.currentTimeMillis()
)
