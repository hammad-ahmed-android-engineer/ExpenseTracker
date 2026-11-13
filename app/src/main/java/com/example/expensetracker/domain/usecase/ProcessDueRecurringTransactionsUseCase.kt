package com.example.expensetracker.domain.usecase

import com.example.expensetracker.domain.repository.RecurringTransactionRepository
import javax.inject.Inject

class ProcessDueRecurringTransactionsUseCase @Inject constructor(
    private val recurringRepository: RecurringTransactionRepository
) {
    suspend operator fun invoke(): Int =
        recurringRepository.processDueRecurringTransactions()
}
