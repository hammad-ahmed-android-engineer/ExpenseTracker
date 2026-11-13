package com.example.expensetracker.domain.usecase

import com.example.expensetracker.core.common.endOfMonthMillis
import com.example.expensetracker.core.common.formatCurrency
import com.example.expensetracker.core.common.formatDate
import com.example.expensetracker.core.common.startOfMonthMillis
import com.example.expensetracker.domain.model.FinancialInsight
import com.example.expensetracker.domain.model.InsightType
import com.example.expensetracker.domain.model.TransactionType
import com.example.expensetracker.domain.repository.BudgetRepository
import com.example.expensetracker.domain.repository.TransactionRepository
import com.example.expensetracker.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import kotlin.math.abs

class GenerateFinancialInsightsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val budgetRepository: BudgetRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<List<FinancialInsight>> {
        val now = Calendar.getInstance()
        val currentYear = now.get(Calendar.YEAR)
        val currentMonth = now.get(Calendar.MONTH)

        val curMonthStart = currentMonth.startOfMonthMillis(currentYear)
        val curMonthEnd = currentMonth.endOfMonthMillis(currentYear)

        val prevCal = (now.clone() as Calendar).apply { add(Calendar.MONTH, -1) }
        val prevMonthStart = prevCal.get(Calendar.MONTH).startOfMonthMillis(prevCal.get(Calendar.YEAR))
        val prevMonthEnd = prevCal.get(Calendar.MONTH).endOfMonthMillis(prevCal.get(Calendar.YEAR))

        return combine(
            transactionRepository.getTransactionsByDateRange(curMonthStart, curMonthEnd),
            transactionRepository.getTotalAmountByTypeAndDateRange(TransactionType.EXPENSE, prevMonthStart, prevMonthEnd),
            transactionRepository.getCategorySpendingByDateRange(curMonthStart, curMonthEnd),
            budgetRepository.getAllBudgets(),
            userPreferencesRepository.userPreferencesFlow
        ) { curTransactions, prevExpense, categorySpending, budgets, prefs ->
            val currency = prefs.defaultCurrency
            val insights = mutableListOf<FinancialInsight>()

            val expenses = curTransactions.filter { it.type == TransactionType.EXPENSE }
            val incomes = curTransactions.filter { it.type == TransactionType.INCOME || it.type == TransactionType.REFUND }
            val curExpense = expenses.sumOf { it.amount }
            val curIncome = incomes.sumOf { it.amount }

            if (prevExpense > 0) {
                val diffPct = ((curExpense - prevExpense).toFloat() / prevExpense.toFloat()) * 100f
                if (diffPct < 0f) {
                    insights.add(
                        FinancialInsight(
                            id = "mom_decrease",
                            title = "Spending Down This Month",
                            message = "You spent ${"%.1f".format(Locale.US, abs(diffPct))}% less than last month.",
                            type = InsightType.POSITIVE,
                            percentageChange = diffPct
                        )
                    )
                } else if (diffPct > 10f) {
                    insights.add(
                        FinancialInsight(
                            id = "mom_increase",
                            title = "Spending Trending Higher",
                            message = "Your expenses are ${"%.1f".format(Locale.US, diffPct)}% higher compared to last month.",
                            type = InsightType.WARNING,
                            percentageChange = diffPct
                        )
                    )
                }
            }

            if (curIncome > 0) {
                val net = curIncome - curExpense
                val savingsRate = ((net.toFloat() / curIncome.toFloat()) * 100f).coerceIn(0f, 100f)
                if (savingsRate >= 20f) {
                    insights.add(
                        FinancialInsight(
                            id = "savings_rate_good",
                            title = "Healthy Savings Rate",
                            message = "Your current savings rate is ${"%.1f".format(Locale.US, savingsRate)}% this month.",
                            type = InsightType.POSITIVE
                        )
                    )
                } else if (savingsRate < 10f && curExpense > 0) {
                    insights.add(
                        FinancialInsight(
                            id = "savings_rate_low",
                            title = "Tight Savings Margin",
                            message = "Your savings rate is ${"%.1f".format(Locale.US, savingsRate)}%. Reviewing discretionary categories could help.",
                            type = InsightType.WARNING
                        )
                    )
                }
            }

            categorySpending.maxByOrNull { it.totalAmount }?.let { topCat ->
                val share = if (curExpense > 0) (topCat.totalAmount.toFloat() / curExpense) * 100f else 0f
                insights.add(
                    FinancialInsight(
                        id = "top_category",
                        title = "Top Spending Category",
                        message = "${topCat.categoryName} leads at ${topCat.totalAmount.formatCurrency(currency)} (${"%.0f".format(Locale.US, share)}% of expenses).",
                        type = InsightType.INFO
                    )
                )
            }

            expenses.maxByOrNull { it.amount }?.let { peakTx ->
                insights.add(
                    FinancialInsight(
                        id = "peak_tx",
                        title = "Largest Expense",
                        message = "${peakTx.title} (${peakTx.amount.formatCurrency(currency)}) on ${peakTx.date.formatDate("MMM dd")}.",
                        type = InsightType.INFO
                    )
                )
            }

            val dayOfMonth = now.get(Calendar.DAY_OF_MONTH).coerceAtLeast(1)
            val avgDaily = curExpense / dayOfMonth
            if (avgDaily > 0) {
                insights.add(
                    FinancialInsight(
                        id = "avg_daily_spending",
                        title = "Daily Burn Rate",
                        message = "You average ${avgDaily.formatCurrency(currency)} per day this month.",
                        type = InsightType.INFO
                    )
                )
            }

            val exceededBudgets = budgets.count { it.isExceeded }
            if (exceededBudgets > 0) {
                insights.add(
                    FinancialInsight(
                        id = "budget_exceeded",
                        title = "Budget Alert",
                        message = "$exceededBudgets budget${if (exceededBudgets > 1) "s have" else " has"} exceeded the target limit.",
                        type = InsightType.WARNING
                    )
                )
            }

            if (insights.isEmpty()) {
                insights.add(
                    FinancialInsight(
                        id = "default_welcome",
                        title = "Offline Financial Intelligence",
                        message = "Record transactions or seed sample data in Settings to unlock spending trends and insights.",
                        type = InsightType.INFO
                    )
                )
            }

            insights
        }
    }
}
