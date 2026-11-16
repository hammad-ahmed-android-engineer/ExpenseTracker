package com.example.expensetracker.domain.repository

import com.example.expensetracker.domain.model.CategorySpendingSummary
import com.example.expensetracker.domain.model.DailySpendingSummary
import com.example.expensetracker.domain.model.Transaction
import com.example.expensetracker.domain.model.TransactionFilter
import com.example.expensetracker.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getAllTransactions(): Flow<List<Transaction>>
    fun getTransactionsByDateRange(startDate: Long, endDate: Long): Flow<List<Transaction>>
    fun searchTransactions(filter: TransactionFilter): Flow<List<Transaction>>
    suspend fun getTransactionById(id: Long): Transaction?
    suspend fun addTransaction(transaction: Transaction): Long
    suspend fun updateTransaction(transaction: Transaction)
    suspend fun deleteTransaction(transaction: Transaction)
    fun getTotalAmountByTypeAndDateRange(type: TransactionType, startDate: Long, endDate: Long): Flow<Long>
    fun getCategorySpendingByDateRange(startDate: Long, endDate: Long): Flow<List<CategorySpendingSummary>>
    fun getDailySpendingByDateRange(startDate: Long, endDate: Long): Flow<List<DailySpendingSummary>>
    suspend fun getTransactionCountForCategory(categoryId: Long): Int
    suspend fun getTransactionCountForAccount(accountId: Long): Int
}
