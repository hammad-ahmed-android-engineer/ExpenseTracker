package com.example.expensetracker.data.local.dao

import androidx.room.*
import com.example.expensetracker.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

data class CategorySpending(
    val categoryId: Long,
    val categoryName: String,
    val categoryColor: String,
    val categoryIcon: String,
    val totalAmount: Long
)

data class DailySpending(
    val date: Long,
    val totalExpense: Long,
    val totalIncome: Long
)

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity): Int

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity): Int

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteTransactionById(id: Long): Int

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: Long): TransactionEntity?

    @Query("SELECT * FROM transactions ORDER BY date DESC, id DESC")
    fun getAllTransactionsFlow(): Flow<List<TransactionEntity>>

    @Query("""
        SELECT * FROM transactions 
        WHERE date >= :startDate AND date <= :endDate 
        ORDER BY date DESC, id DESC
    """)
    fun getTransactionsByDateRangeFlow(startDate: Long, endDate: Long): Flow<List<TransactionEntity>>

    @Query("""
        SELECT COALESCE(SUM(amount), 0) FROM transactions 
        WHERE type = :type AND date >= :startDate AND date <= :endDate
    """)
    fun getTotalAmountByTypeAndDateRangeFlow(type: String, startDate: Long, endDate: Long): Flow<Long>

    @Query("""
        SELECT COALESCE(SUM(amount), 0) FROM transactions 
        WHERE type = :type AND date >= :startDate AND date <= :endDate
    """)
    suspend fun getTotalAmountByTypeAndDateRange(type: String, startDate: Long, endDate: Long): Long

    @Query("""
        SELECT COALESCE(SUM(amount), 0) FROM transactions 
        WHERE type = :type AND categoryId = :categoryId AND date >= :startDate AND date <= :endDate
    """)
    suspend fun getCategoryAmountByTypeAndDateRange(
        type: String,
        categoryId: Long,
        startDate: Long,
        endDate: Long
    ): Long

    @Query("""
        SELECT c.id AS categoryId, c.name AS categoryName, c.colorHex AS categoryColor, c.iconName AS categoryIcon, 
               COALESCE(SUM(t.amount), 0) AS totalAmount
        FROM transactions t
        INNER JOIN categories c ON t.categoryId = c.id
        WHERE t.type = 'EXPENSE' AND t.date >= :startDate AND t.date <= :endDate
        GROUP BY c.id
        ORDER BY totalAmount DESC
    """)
    fun getCategorySpendingByDateRangeFlow(startDate: Long, endDate: Long): Flow<List<CategorySpending>>

    @Query("""
        SELECT t.date AS date,
               SUM(CASE WHEN t.type = 'EXPENSE' THEN t.amount ELSE 0 END) AS totalExpense,
               SUM(CASE WHEN t.type = 'INCOME' THEN t.amount ELSE 0 END) AS totalIncome
        FROM transactions t
        WHERE t.date >= :startDate AND t.date <= :endDate
        GROUP BY t.date
        ORDER BY t.date ASC
    """)
    fun getDailySpendingByDateRangeFlow(startDate: Long, endDate: Long): Flow<List<DailySpending>>

    @Query("""
        SELECT * FROM transactions
        WHERE (:query IS NULL OR title LIKE '%' || :query || '%' OR note LIKE '%' || :query || '%')
        AND (:type IS NULL OR type = :type)
        AND (:accountId IS NULL OR accountId = :accountId)
        AND (:categoryId IS NULL OR categoryId = :categoryId)
        AND (:startDate IS NULL OR date >= :startDate)
        AND (:endDate IS NULL OR date <= :endDate)
        ORDER BY date DESC
    """)
    fun searchTransactionsFlow(
        query: String?,
        type: String?,
        accountId: Long?,
        categoryId: Long?,
        startDate: Long?,
        endDate: Long?
    ): Flow<List<TransactionEntity>>

    @Query("SELECT COUNT(*) FROM transactions WHERE categoryId = :categoryId")
    suspend fun getTransactionCountForCategory(categoryId: Long): Int

    @Query("SELECT COUNT(*) FROM transactions WHERE accountId = :accountId OR destinationAccountId = :accountId")
    suspend fun getTransactionCountForAccount(accountId: Long): Int
}