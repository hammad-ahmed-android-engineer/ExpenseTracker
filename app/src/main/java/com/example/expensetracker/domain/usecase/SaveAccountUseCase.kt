package com.example.expensetracker.domain.usecase

import com.example.expensetracker.domain.model.Account
import com.example.expensetracker.domain.repository.AccountRepository
import javax.inject.Inject

class SaveAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(account: Account): Long {
        return if (account.id == 0L) {
            accountRepository.addAccount(account)
        } else {
            accountRepository.updateAccount(account)
            account.id
        }
    }

    suspend fun archiveAccount(accountId: Long, archive: Boolean) {
        accountRepository.archiveAccount(accountId, archive)
    }
}
