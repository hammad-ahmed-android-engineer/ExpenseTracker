package com.example.expensetracker.domain.usecase

import com.example.expensetracker.domain.model.Account
import com.example.expensetracker.domain.repository.AccountRepository
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(account: Account) {
        accountRepository.deleteAccount(account)
    }
}

