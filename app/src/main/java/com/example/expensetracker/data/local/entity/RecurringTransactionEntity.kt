package com.example.expensetracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recurring_transactions")
data class RecurringTransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val amount: Long,
    val type: String, // EXPENSE, INCOME, TRANSFER
    val categoryId: Long,
    val accountId: Long,
    val destinationAccountId: Long? = null,
    val frequency: String, // DAILY, WEEKLY, BIWEEKLY, MONTHLY, QUARTERLY, YEARLY
    val intervalDays: Int = 1,
    val startDate: Long,
    val endDate: Long? = null,
    val lastExecutedDate: Long? = null,
    val nextDueDate: Long,
    val isEnabled: Boolean = true,
    val note: String? = null
)