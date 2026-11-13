package com.example.expensetracker.domain.usecase

import com.example.expensetracker.domain.repository.AccountRepository
import javax.inject.Inject

class TransferMoneyUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(
        sourceAccountId: Long,
        destinationAccountId: Long,
        amount: Long,
        note: String?
    ): Result<Long> {
        if (sourceAccountId == destinationAccountId) {
            return Result.failure(IllegalArgumentException("Source and destination accounts must be different"))
        }
        if (amount <= 0L) {
            return Result.failure(IllegalArgumentException("Transfer amount must be greater than zero"))
        }
        return runCatching {
            accountRepository.transferMoney(
                sourceAccountId = sourceAccountId,
                destinationAccountId = destinationAccountId,
                amount = amount,
                date = System.currentTimeMillis(),
                note = note?.trim()?.ifEmpty { null }
            )
        }
    }
}

