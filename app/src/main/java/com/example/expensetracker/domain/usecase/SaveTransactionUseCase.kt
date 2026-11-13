package com.example.expensetracker.domain.usecase

import com.example.expensetracker.domain.model.Transaction
import com.example.expensetracker.domain.model.TransactionType
import com.example.expensetracker.domain.repository.TransactionRepository
import javax.inject.Inject

class SaveTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction): Result<Long> {
        if (transaction.amount <= 0L) {
            return Result.failure(IllegalArgumentException("Amount must be greater than zero"))
        }
        if (transaction.type == TransactionType.TRANSFER) {
            if (transaction.destinationAccountId == null || transaction.destinationAccountId == transaction.accountId) {
                return Result.failure(IllegalArgumentException("Select a different destination account for transfer"))
            }
        }
        return runCatching {
            if (transaction.id == 0L) {
                transactionRepository.addTransaction(transaction)
            } else {
                transactionRepository.updateTransaction(transaction)
                transaction.id
            }
        }
    }
}

