package com.example.expensetracker.data.repository

import com.example.expensetracker.core.common.startOfDayMillis
import com.example.expensetracker.data.local.dao.AccountDao
import com.example.expensetracker.data.local.dao.CategoryDao
import com.example.expensetracker.data.local.dao.TagDao
import com.example.expensetracker.data.local.dao.TransactionDao
import com.example.expensetracker.data.local.entity.TagEntity
import com.example.expensetracker.data.local.entity.TransactionEntity
import com.example.expensetracker.data.local.entity.TransactionTagCrossRefEntity
import com.example.expensetracker.data.mapper.toDomain
import com.example.expensetracker.data.mapper.toEntity
import com.example.expensetracker.domain.model.CategorySpendingSummary
import com.example.expensetracker.domain.model.DailySpendingSummary
import com.example.expensetracker.domain.model.Transaction
import com.example.expensetracker.domain.model.TransactionType
import com.example.expensetracker.domain.repository.TransactionFilter
import com.example.expensetracker.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao,
    private val accountDao: AccountDao,
    private val categoryDao: CategoryDao,
    private val tagDao: TagDao
) : TransactionRepository {

    override fun getAllTransactions(): Flow<List<Transaction>> =
        transactionDao.getAllTransactionsFlow().map { entities ->
            entities.map { entityToDomain(it) }
        }

    override fun getTransactionsByDateRange(startDate: Long, endDate: Long): Flow<List<Transaction>> =
        transactionDao.getTransactionsByDateRangeFlow(startDate, endDate).map { entities ->
            entities.map { entityToDomain(it) }
        }

    override fun searchTransactions(filter: TransactionFilter): Flow<List<Transaction>> =
        transactionDao.searchTransactionsFlow(
            query = if (filter.query.isNullOrBlank()) null else filter.query,
            type = filter.type?.name,
            accountId = filter.accountId,
            categoryId = filter.categoryId,
            startDate = filter.startDate,
            endDate = filter.endDate
        ).map { entities ->
            entities.map { entityToDomain(it) }
        }

    override suspend fun getTransactionById(id: Long): Transaction? {
        val entity = transactionDao.getTransactionById(id) ?: return null
        return entityToDomain(entity)
    }

    override suspend fun addTransaction(transaction: Transaction): Long {
        val entity = transaction.toEntity()
        val id = transactionDao.insertTransaction(entity)

        for (tagName in transaction.tags) {
            val existingTag = tagDao.getTagByName(tagName)
            val tagId = existingTag?.id ?: tagDao.insertTag(TagEntity(name = tagName))
            if (tagId > 0) {
                tagDao.insertTransactionTagCrossRef(
                    TransactionTagCrossRefEntity(transactionId = id, tagId = tagId)
                )
            }
        }

        applyBalanceEffect(transaction)
        return id
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        val old = transactionDao.getTransactionById(transaction.id)
        if (old != null) {
            revertBalanceEffect(old)
        }
        transactionDao.updateTransaction(transaction.toEntity())
        applyBalanceEffect(transaction)

        tagDao.deleteTagsForTransaction(transaction.id)
        for (tagName in transaction.tags) {
            val existingTag = tagDao.getTagByName(tagName)
            val tagId = existingTag?.id ?: tagDao.insertTag(TagEntity(name = tagName))
            if (tagId > 0) {
                tagDao.insertTransactionTagCrossRef(
                    TransactionTagCrossRefEntity(transactionId = transaction.id, tagId = tagId)
                )
            }
        }
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        val old = transactionDao.getTransactionById(transaction.id)
        if (old != null) {
            revertBalanceEffect(old)
        }
        tagDao.deleteTagsForTransaction(transaction.id)
        transactionDao.deleteTransaction(transaction.toEntity())
    }

    private suspend fun revertBalanceEffect(entity: TransactionEntity) {
        when (TransactionType.fromString(entity.type)) {
            TransactionType.EXPENSE -> accountDao.updateAccountBalance(entity.accountId, entity.amount)
            TransactionType.INCOME, TransactionType.REFUND ->
                accountDao.updateAccountBalance(entity.accountId, -entity.amount)
            TransactionType.TRANSFER -> {
                accountDao.updateAccountBalance(entity.accountId, entity.amount)
                entity.destinationAccountId?.let { destId ->
                    accountDao.updateAccountBalance(destId, -entity.amount)
                }
            }
            TransactionType.ADJUSTMENT -> accountDao.updateAccountBalance(entity.accountId, -entity.amount)
        }
    }

    private suspend fun applyBalanceEffect(transaction: Transaction) {
        when (transaction.type) {
            TransactionType.EXPENSE -> accountDao.updateAccountBalance(transaction.accountId, -transaction.amount)
            TransactionType.INCOME, TransactionType.REFUND ->
                accountDao.updateAccountBalance(transaction.accountId, transaction.amount)
            TransactionType.TRANSFER -> {
                accountDao.updateAccountBalance(transaction.accountId, -transaction.amount)
                transaction.destinationAccountId?.let { destId ->
                    accountDao.updateAccountBalance(destId, transaction.amount)
                }
            }
            TransactionType.ADJUSTMENT -> accountDao.updateAccountBalance(transaction.accountId, transaction.amount)
        }
    }

    override fun getTotalAmountByTypeAndDateRange(
        type: TransactionType,
        startDate: Long,
        endDate: Long
    ): Flow<Long> =
        transactionDao.getTotalAmountByTypeAndDateRangeFlow(type.name, startDate, endDate)

    override fun getCategorySpendingByDateRange(
        startDate: Long,
        endDate: Long
    ): Flow<List<CategorySpendingSummary>> =
        transactionDao.getCategorySpendingByDateRangeFlow(startDate, endDate).map { list ->
            val total = list.sumOf { it.totalAmount }.coerceAtLeast(1L)
            list.map { item ->
                CategorySpendingSummary(
                    categoryId = item.categoryId,
                    categoryName = item.categoryName,
                    categoryColor = item.categoryColor,
                    categoryIcon = item.categoryIcon,
                    totalAmount = item.totalAmount,
                    percentage = (item.totalAmount.toFloat() / total.toFloat()) * 100f
                )
            }
        }

    override fun getDailySpendingByDateRange(
        startDate: Long,
        endDate: Long
    ): Flow<List<DailySpendingSummary>> =
        transactionDao.getTransactionsByDateRangeFlow(startDate, endDate).map { txs ->
            txs.groupBy { it.date.startOfDayMillis() }
                .entries
                .sortedBy { it.key }
                .map { (dayStart, dayTxs) ->
                    val expense = dayTxs
                        .filter { it.type == TransactionType.EXPENSE.name }
                        .sumOf { it.amount }
                    val income = dayTxs
                        .filter { it.type == TransactionType.INCOME.name || it.type == TransactionType.REFUND.name }
                        .sumOf { it.amount }
                    DailySpendingSummary(
                        date = dayStart,
                        totalExpense = expense,
                        totalIncome = income
                    )
                }
        }

    override suspend fun getTransactionCountForCategory(categoryId: Long): Int =
        transactionDao.getTransactionCountForCategory(categoryId)

    override suspend fun getTransactionCountForAccount(accountId: Long): Int =
        transactionDao.getTransactionCountForAccount(accountId)

    private suspend fun entityToDomain(entity: TransactionEntity): Transaction {
        val category = categoryDao.getCategoryById(entity.categoryId)
        val account = accountDao.getAccountById(entity.accountId)
        val destAccount = entity.destinationAccountId?.let { accountDao.getAccountById(it) }
        val tags = tagDao.getTagsForTransaction(entity.id).map { it.name }
        return entity.toDomain(category, account, destAccount, tags)
    }
}
