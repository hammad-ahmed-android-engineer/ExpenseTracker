package com.example.expensetracker.domain.repository

import com.example.expensetracker.domain.model.RecurringTransaction
import kotlinx.coroutines.flow.Flow

interface RecurringTransactionRepository {
    fun getAllRecurring(): Flow<List<RecurringTransaction>>
    suspend fun getRecurringById(id: Long): RecurringTransaction?
    suspend fun addRecurring(recurring: RecurringTransaction): Long
    suspend fun updateRecurring(recurring: RecurringTransaction)
    suspend fun deleteRecurring(recurring: RecurringTransaction)
    suspend fun processDueRecurringTransactions(): Int // returns count of newly created transactions
}