package com.example.expensetracker.domain.usecase

import com.example.expensetracker.domain.model.Transaction
import com.example.expensetracker.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTransactionsByDateRangeUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(startDate: Long, endDate: Long): Flow<List<Transaction>> =
        transactionRepository.getTransactionsByDateRange(startDate, endDate)
}

