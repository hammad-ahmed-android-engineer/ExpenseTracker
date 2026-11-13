package com.example.expensetracker.domain.usecase

import com.example.expensetracker.domain.model.RecurringTransaction
import com.example.expensetracker.domain.repository.RecurringTransactionRepository
import javax.inject.Inject

class SaveRecurringTransactionUseCase @Inject constructor(
    private val recurringRepository: RecurringTransactionRepository
) {
    suspend operator fun invoke(recurring: RecurringTransaction): Long =
        recurringRepository.addRecurring(recurring)

    suspend fun updateRecurring(recurring: RecurringTransaction) =
        recurringRepository.updateRecurring(recurring)
}

