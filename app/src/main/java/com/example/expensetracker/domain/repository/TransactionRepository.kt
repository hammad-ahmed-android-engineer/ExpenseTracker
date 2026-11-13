package com.example.expensetracker.domain.repository

import com.example.expensetracker.domain.model.CategorySpendingSummary
import com.example.expensetracker.domain.model.DailySpendingSummary
import com.example.expensetracker.domain.model.Transaction
import com.example.expensetracker.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow

enum class TransactionSortOrder(val displayName: String) {
    NEWEST_FIRST("Newest"),
    OLDEST_FIRST("Oldest"),
    HIGHEST_AMOUNT("Highest"),
    LOWEST_AMOUNT("Lowest")
}

data class TransactionFilter(
    val query: String? = null,
    val type: TransactionType? = null,
    val accountId: Long? = null,
    val categoryId: Long? = null,
    val startDate: Long? = null,
    val endDate: Long? = null,
    val paymentMethod: String? = null,
    val tag: String? = null,
    val sortOrder: TransactionSortOrder = TransactionSortOrder.NEWEST_FIRST
)

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
