package com.example.expensetracker.domain.usecase

import com.example.expensetracker.domain.model.CategorySpendingSummary
import com.example.expensetracker.domain.model.DailySpendingSummary
import com.example.expensetracker.domain.model.TransactionType
import com.example.expensetracker.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

data class ReportsAnalyticsData(
    val totalIncome: Long = 0L,
    val totalExpense: Long = 0L,
    val netSavings: Long = 0L,
    val categorySpendings: List<CategorySpendingSummary> = emptyList(),
    val dailySpendings: List<DailySpendingSummary> = emptyList(),
    val transactions: List<com.example.expensetracker.domain.model.Transaction> = emptyList()
)

class GetReportsAnalyticsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(startDate: Long, endDate: Long): Flow<ReportsAnalyticsData> = combine(
        transactionRepository.getTotalAmountByTypeAndDateRange(TransactionType.INCOME, startDate, endDate),
        transactionRepository.getTotalAmountByTypeAndDateRange(TransactionType.EXPENSE, startDate, endDate),
        transactionRepository.getCategorySpendingByDateRange(startDate, endDate),
        transactionRepository.getDailySpendingByDateRange(startDate, endDate),
        transactionRepository.getTransactionsByDateRange(startDate, endDate)
    ) { income, expense, categories, dailies, txs ->
        val totalExp = expense.coerceAtLeast(1L)
        val categoriesWithPct = categories.map { item ->
            item.copy(percentage = if (expense > 0) (item.totalAmount.toFloat() / totalExp.toFloat()) * 100f else 0f)
        }
        ReportsAnalyticsData(
            totalIncome = income,
            totalExpense = expense,
            netSavings = income - expense,
            categorySpendings = categoriesWithPct,
            dailySpendings = dailies,
            transactions = txs
        )
    }
}

