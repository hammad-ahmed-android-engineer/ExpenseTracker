package com.example.expensetracker.data.repository

import com.example.expensetracker.data.local.dao.AccountDao
import com.example.expensetracker.data.local.dao.CategoryDao
import com.example.expensetracker.data.local.dao.RecurringTransactionDao
import com.example.expensetracker.data.local.dao.TransactionDao
import com.example.expensetracker.data.local.entity.TransactionEntity
import com.example.expensetracker.data.mapper.toDomain
import com.example.expensetracker.data.mapper.toEntity
import com.example.expensetracker.domain.model.RecurringFrequency
import com.example.expensetracker.domain.model.RecurringTransaction
import com.example.expensetracker.domain.model.TransactionType
import com.example.expensetracker.domain.repository.RecurringTransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecurringTransactionRepositoryImpl @Inject constructor(
    private val recurringDao: RecurringTransactionDao,
    private val transactionDao: TransactionDao,
    private val accountDao: AccountDao,
    private val categoryDao: CategoryDao
) : RecurringTransactionRepository {

    override fun getAllRecurring(): Flow<List<RecurringTransaction>> {
        return recurringDao.getAllRecurringFlow().map { list ->
            list.map { entity ->
                val category = categoryDao.getCategoryById(entity.categoryId)
                val account = accountDao.getAccountById(entity.accountId)
                val destAccount = entity.destinationAccountId?.let { accountDao.getAccountById(it) }
                entity.toDomain(category, account, destAccount)
            }
        }
    }

    override suspend fun getRecurringById(id: Long): RecurringTransaction? {
        val entity = recurringDao.getRecurringById(id) ?: return null
        val category = categoryDao.getCategoryById(entity.categoryId)
        val account = accountDao.getAccountById(entity.accountId)
        val destAccount = entity.destinationAccountId?.let { accountDao.getAccountById(it) }
        return entity.toDomain(category, account, destAccount)
    }

    override suspend fun addRecurring(recurring: RecurringTransaction): Long {
        return recurringDao.insertRecurring(recurring.toEntity())
    }

    override suspend fun updateRecurring(recurring: RecurringTransaction) {
        recurringDao.updateRecurring(recurring.toEntity())
    }

    override suspend fun deleteRecurring(recurring: RecurringTransaction) {
        recurringDao.deleteRecurring(recurring.toEntity())
    }

    override suspend fun processDueRecurringTransactions(): Int {
        val now = System.currentTimeMillis()
        val dueList = recurringDao.getDueRecurringTransactions(now)
        var count = 0

        for (recurring in dueList) {
            // Create transaction record
            val txEntity = TransactionEntity(
                title = recurring.title,
                amount = recurring.amount,
                type = recurring.type,
                categoryId = recurring.categoryId,
                accountId = recurring.accountId,
                destinationAccountId = recurring.destinationAccountId,
                date = recurring.nextDueDate,
                note = recurring.note ?: "Auto-generated recurring transaction",
                isRecurring = true,
                recurringId = recurring.id
            )
            transactionDao.insertTransaction(txEntity)

            // Update Account balance
            val txType = TransactionType.fromString(recurring.type)
            when (txType) {
                TransactionType.EXPENSE -> accountDao.updateAccountBalance(recurring.accountId, -recurring.amount)
                TransactionType.INCOME, TransactionType.REFUND -> accountDao.updateAccountBalance(recurring.accountId, recurring.amount)
                TransactionType.TRANSFER -> {
                    accountDao.updateAccountBalance(recurring.accountId, -recurring.amount)
                    recurring.destinationAccountId?.let { destId ->
                        accountDao.updateAccountBalance(destId, recurring.amount)
                    }
                }
                TransactionType.ADJUSTMENT -> accountDao.updateAccountBalance(recurring.accountId, recurring.amount)
            }

            // Calculate next due date
            val nextDue = calculateNextDueDate(recurring.nextDueDate, RecurringFrequency.fromString(recurring.frequency), recurring.intervalDays)
            recurringDao.updateExecutionDates(recurring.id, now, nextDue)
            count++
        }
        return count
    }

    private fun calculateNextDueDate(currentDueDate: Long, frequency: RecurringFrequency, interval: Int): Long {
        val cal = Calendar.getInstance().apply { timeInMillis = currentDueDate }
        when (frequency) {
            RecurringFrequency.DAILY -> cal.add(Calendar.DAY_OF_YEAR, interval)
            RecurringFrequency.WEEKLY -> cal.add(Calendar.WEEK_OF_YEAR, interval)
            RecurringFrequency.BIWEEKLY -> cal.add(Calendar.WEEK_OF_YEAR, 2 * interval)
            RecurringFrequency.MONTHLY -> cal.add(Calendar.MONTH, interval)
            RecurringFrequency.QUARTERLY -> cal.add(Calendar.MONTH, 3 * interval)
            RecurringFrequency.YEARLY -> cal.add(Calendar.YEAR, interval)
        }
        return cal.timeInMillis
    }
}