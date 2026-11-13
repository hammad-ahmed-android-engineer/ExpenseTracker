package com.example.expensetracker.domain.repository

import com.example.expensetracker.domain.model.Account
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    fun getAllActiveAccounts(): Flow<List<Account>>
    fun getAllAccounts(): Flow<List<Account>>
    suspend fun getAccountById(id: Long): Account?
    suspend fun addAccount(account: Account): Long
    suspend fun updateAccount(account: Account)
    suspend fun deleteAccount(account: Account)
    suspend fun archiveAccount(accountId: Long, archive: Boolean)
    fun getTotalBalance(): Flow<Long>
    suspend fun transferMoney(
        sourceAccountId: Long,
        destinationAccountId: Long,
        amount: Long,
        date: Long,
        note: String?
    ): Long
}