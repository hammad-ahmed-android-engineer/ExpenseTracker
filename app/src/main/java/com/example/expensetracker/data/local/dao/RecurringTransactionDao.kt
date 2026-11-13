package com.example.expensetracker.data.local.dao

import androidx.room.*
import com.example.expensetracker.data.local.entity.RecurringTransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecurringTransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecurring(recurring: RecurringTransactionEntity): Long

    @Update
    suspend fun updateRecurring(recurring: RecurringTransactionEntity): Int

    @Delete
    suspend fun deleteRecurring(recurring: RecurringTransactionEntity): Int

    @Query("SELECT * FROM recurring_transactions WHERE id = :id")
    suspend fun getRecurringById(id: Long): RecurringTransactionEntity?

    @Query("SELECT * FROM recurring_transactions ORDER BY nextDueDate ASC")
    fun getAllRecurringFlow(): Flow<List<RecurringTransactionEntity>>

    @Query("SELECT * FROM recurring_transactions WHERE isEnabled = 1 AND nextDueDate <= :currentTime")
    suspend fun getDueRecurringTransactions(currentTime: Long): List<RecurringTransactionEntity>

    @Query("UPDATE recurring_transactions SET lastExecutedDate = :executedTime, nextDueDate = :nextDue WHERE id = :id")
    suspend fun updateExecutionDates(id: Long, executedTime: Long, nextDue: Long): Int

    @Query("DELETE FROM recurring_transactions")
    suspend fun deleteAllRecurring(): Int
}