package com.example.expensetracker.domain.usecase

import com.example.expensetracker.core.common.endOfMonthMillis
import com.example.expensetracker.core.common.startOfMonthMillis
import com.example.expensetracker.domain.model.CategorySpendingSummary
import com.example.expensetracker.domain.model.FinancialSummary
import com.example.expensetracker.domain.model.TransactionType
import com.example.expensetracker.domain.repository.AccountRepository
import com.example.expensetracker.domain.repository.BudgetRepository
import com.example.expensetracker.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.util.Calendar
import javax.inject.Inject

class GetDashboardSummaryUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository,
    private val budgetRepository: BudgetRepository
) {
    operator fun invoke(): Flow<FinancialSummary> {
        val now = Calendar.getInstance()
        val currentYear = now.get(Calendar.YEAR)
        val currentMonth = now.get(Calendar.MONTH)

        val currentMonthStart = currentMonth.startOfMonthMillis(currentYear)
        val currentMonthEnd = currentMonth.endOfMonthMillis(currentYear)

        val prevCal = (now.clone() as Calendar).apply { add(Calendar.MONTH, -1) }
        val prevYear = prevCal.get(Calendar.YEAR)
        val prevMonth = prevCal.get(Calendar.MONTH)

        val prevMonthStart = prevMonth.startOfMonthMillis(prevYear)
        val prevMonthEnd = prevMonth.endOfMonthMillis(prevYear)

        val currentMonthTotals = combine(
            accountRepository.getTotalBalance(),
            transactionRepository.getTotalAmountByTypeAndDateRange(
                TransactionType.INCOME,
                currentMonthStart,
                currentMonthEnd
            ),
            transactionRepository.getTotalAmountByTypeAndDateRange(
                TransactionType.EXPENSE,
                currentMonthStart,
                currentMonthEnd
            )
        ) { totalBalance, curIncome, curExpense ->
            Triple(totalBalance, curIncome, curExpense)
        }

        val previousMonthTotals = combine(
            transactionRepository.getTotalAmountByTypeAndDateRange(
                TransactionType.INCOME,
                prevMonthStart,
                prevMonthEnd
            ),
            transactionRepository.getTotalAmountByTypeAndDateRange(
                TransactionType.EXPENSE,
                prevMonthStart,
                prevMonthEnd
            )
        ) { prevIncome, prevExpense ->
            prevIncome to prevExpense
        }

        return combine(
            currentMonthTotals,
            previousMonthTotals,
            transactionRepository.getCategorySpendingByDateRange(currentMonthStart, currentMonthEnd),
            budgetRepository.getAllBudgets()
        ) { (totalBalance, curIncome, curExpense), (prevIncome, prevExpense), categorySpendings, budgets ->
            val netSavings = curIncome - curExpense

            val incomeDelta = if (prevIncome > 0) {
                ((curIncome - prevIncome).toFloat() / prevIncome.toFloat()) * 100f
            } else 0f

            val expenseDelta = if (prevExpense > 0) {
                ((curExpense - prevExpense).toFloat() / prevExpense.toFloat()) * 100f
            } else 0f

            val categorySummaries = categorySpendings.map { cs ->
                val pct = if (curExpense > 0) {
                    (cs.totalAmount.toFloat() / curExpense.toFloat()) * 100f
                } else 0f
                CategorySpendingSummary(
                    categoryId = cs.categoryId,
                    categoryName = cs.categoryName,
                    categoryColor = cs.categoryColor,
                    categoryIcon = cs.categoryIcon,
                    totalAmount = cs.totalAmount,
                    percentage = pct
                )
            }

            val highestCat = categorySummaries.maxByOrNull { it.totalAmount }?.categoryName
            val totalBudget = budgets.sumOf { it.amountLimit }
            val totalBudgetSpent = budgets.sumOf { it.spentAmount }

            FinancialSummary(
                totalBalance = totalBalance,
                monthlyIncome = curIncome,
                monthlyExpense = curExpense,
                netSavings = netSavings,
                previousMonthIncomeDeltaPercentage = incomeDelta,
                previousMonthExpenseDeltaPercentage = expenseDelta,
                categorySpendingList = categorySummaries,
                highestSpendingCategoryName = highestCat,
                totalMonthlyBudget = totalBudget,
                totalSpentBudget = totalBudgetSpent
            )
        }
    }
}
