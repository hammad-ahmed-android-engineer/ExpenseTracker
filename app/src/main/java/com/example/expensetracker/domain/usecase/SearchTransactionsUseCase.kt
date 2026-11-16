package com.example.expensetracker.domain.usecase

import com.example.expensetracker.domain.model.Transaction
import com.example.expensetracker.domain.model.TransactionFilter
import com.example.expensetracker.domain.model.TransactionSortOrder
import com.example.expensetracker.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchTransactionsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(filter: TransactionFilter): Flow<List<Transaction>> =
        transactionRepository.searchTransactions(filter).map { list ->
            val paymentFiltered = if (filter.paymentMethod != null) {
                list.filter { it.paymentMethod.equals(filter.paymentMethod, ignoreCase = true) }
            } else list

            val tagFiltered = if (!filter.tag.isNullOrBlank()) {
                paymentFiltered.filter { tx -> tx.tags.any { it.contains(filter.tag, ignoreCase = true) } }
            } else paymentFiltered

            when (filter.sortOrder) {
                TransactionSortOrder.NEWEST_FIRST -> tagFiltered.sortedByDescending { it.date }
                TransactionSortOrder.OLDEST_FIRST -> tagFiltered.sortedBy { it.date }
                TransactionSortOrder.HIGHEST_AMOUNT -> tagFiltered.sortedByDescending { it.amount }
                TransactionSortOrder.LOWEST_AMOUNT -> tagFiltered.sortedBy { it.amount }
            }
        }
}

