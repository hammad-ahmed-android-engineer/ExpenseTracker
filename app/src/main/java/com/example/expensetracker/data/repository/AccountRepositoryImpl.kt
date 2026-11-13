package com.example.expensetracker.data.repository

import com.example.expensetracker.data.local.dao.AccountDao
import com.example.expensetracker.data.local.dao.CategoryDao
import com.example.expensetracker.data.local.dao.TransactionDao
import com.example.expensetracker.data.local.entity.TransactionEntity
import com.example.expensetracker.data.mapper.toDomain
import com.example.expensetracker.data.mapper.toEntity
import com.example.expensetracker.domain.model.Account
import com.example.expensetracker.domain.model.TransactionType
import com.example.expensetracker.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepositoryImpl @Inject constructor(
    private val accountDao: AccountDao,
    private val transactionDao: TransactionDao,
    private val categoryDao: CategoryDao
) : AccountRepository {

    override fun getAllActiveAccounts(): Flow<List<Account>> {
        return accountDao.getAllActiveAccountsFlow().map { list -> list.map { it.toDomain() } }
    }

    override fun getAllAccounts(): Flow<List<Account>> {
        return accountDao.getAllAccountsFlow().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getAccountById(id: Long): Account? {
        return accountDao.getAccountById(id)?.toDomain()
    }

    override suspend fun addAccount(account: Account): Long {
        return accountDao.insertAccount(account.toEntity())
    }

    override suspend fun updateAccount(account: Account) {
        accountDao.updateAccount(account.toEntity())
    }

    override suspend fun deleteAccount(account: Account) {
        accountDao.deleteAccount(account.toEntity())
    }

    override suspend fun archiveAccount(accountId: Long, archive: Boolean) {
        val account = accountDao.getAccountById(accountId)
        if (account != null) {
            accountDao.updateAccount(account.copy(isArchived = archive))
        }
    }

    override fun getTotalBalance(): Flow<Long> {
        return accountDao.getTotalBalanceFlow()
    }

    override suspend fun transferMoney(
        sourceAccountId: Long,
        destinationAccountId: Long,
        amount: Long,
        date: Long,
        note: String?
    ): Long {
        val source = accountDao.getAccountById(sourceAccountId) ?: return 0L
        val dest = accountDao.getAccountById(destinationAccountId) ?: return 0L

        // Default transfer category or create dummy category id
        val categories = categoryDao.getCategoryCount()
        val firstCategory = if (categories > 0) 1L else 1L

        val transactionEntity = TransactionEntity(
            title = "Transfer: ${source.name} ➔ ${dest.name}",
            amount = amount,
            type = TransactionType.TRANSFER.name,
            categoryId = firstCategory,
            accountId = sourceAccountId,
            destinationAccountId = destinationAccountId,
            date = date,
            note = note ?: "Account Transfer"
        )

        val txId = transactionDao.insertTransaction(transactionEntity)

        // Update balances
        accountDao.updateAccountBalance(sourceAccountId, -amount)
        accountDao.updateAccountBalance(destinationAccountId, amount)

        return txId
    }
}