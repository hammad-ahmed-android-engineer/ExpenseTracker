package com.example.expensetracker.domain.usecase

import com.example.expensetracker.domain.model.Transaction
import com.example.expensetracker.domain.repository.TransactionRepository
import javax.inject.Inject

class DeleteTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction) {
        transactionRepository.deleteTransaction(transaction)
    }
}

