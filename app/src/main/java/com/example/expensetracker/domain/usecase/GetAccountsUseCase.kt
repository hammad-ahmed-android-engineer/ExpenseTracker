package com.example.expensetracker.domain.usecase

import com.example.expensetracker.domain.model.Account
import com.example.expensetracker.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAccountsUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    operator fun invoke(): Flow<List<Account>> = accountRepository.getAllAccounts()

    fun getActiveAccounts(): Flow<List<Account>> = accountRepository.getAllActiveAccounts()

    fun getTotalBalance(): Flow<Long> = accountRepository.getTotalBalance()

    suspend fun getAccountById(id: Long): Account? = accountRepository.getAccountById(id)
}
