package com.example.expensetracker.domain.usecase

import com.example.expensetracker.domain.model.RecurringTransaction
import com.example.expensetracker.domain.repository.RecurringTransactionRepository
import javax.inject.Inject

class DeleteRecurringTransactionUseCase @Inject constructor(
    private val recurringRepository: RecurringTransactionRepository
) {
    suspend operator fun invoke(recurring: RecurringTransaction) =
        recurringRepository.deleteRecurring(recurring)
}

