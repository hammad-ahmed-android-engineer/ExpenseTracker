package com.example.expensetracker.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("accountId"),
        Index("categoryId"),
        Index("date"),
        Index("type")
    ]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val amount: Long, // Minor units (e.g. cents)
    val type: String, // EXPENSE, INCOME, TRANSFER, REFUND, ADJUSTMENT
    val categoryId: Long,
    val subcategoryId: Long? = null,
    val accountId: Long,
    val destinationAccountId: Long? = null,
    val date: Long, // Epoch millis
    val paymentMethod: String = "CASH",
    val note: String? = null,
    val location: String? = null,
    val isRecurring: Boolean = false,
    val recurringId: Long? = null,
    val attachmentPath: String? = null,
    val originalAmount: Long? = null,
    val originalCurrency: String? = null,
    val exchangeRate: Double? = null,
    val createdAt: Long = System.currentTimeMillis()
)