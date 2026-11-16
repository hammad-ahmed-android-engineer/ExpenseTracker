package com.example.expensetracker.data.local.entity.recurring

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recurring_transactions")
data class RecurringTransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val amount: Long,
    val type: String,
    val categoryId: Long,
    val accountId: Long,
    val destinationAccountId: Long? = null,
    val frequency: String,
    val intervalDays: Int = 1,
    val startDate: Long,
    val endDate: Long? = null,
    val lastExecutedDate: Long? = null,
    val nextDueDate: Long,
    val isEnabled: Boolean = true,
    val note: String? = null
)
