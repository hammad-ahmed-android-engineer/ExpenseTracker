package com.example.expensetracker.domain.usecase

import com.example.expensetracker.domain.model.RecurringTransaction
import com.example.expensetracker.domain.repository.RecurringTransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecurringTransactionsUseCase @Inject constructor(
    private val recurringRepository: RecurringTransactionRepository
) {
    operator fun invoke(): Flow<List<RecurringTransaction>> = recurringRepository.getAllRecurring()
}

