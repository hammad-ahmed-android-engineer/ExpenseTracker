package com.example.expensetracker.presentation.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.expensetracker.R
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppTextButton
import com.example.expensetracker.domain.model.DashboardWidgetType
import com.example.expensetracker.presentation.components.EmptyStateView
import com.example.expensetracker.presentation.dashboard.DashboardUiState

fun LazyListScope.dashboardWidgetItems(
    widgets: List<DashboardWidgetType>,
    state: DashboardUiState,
    currencyCode: String,
    onNavigateToAddTransaction: () -> Unit,
    onNavigateToEditTransaction: (Long) -> Unit,
    onNavigateToTransactions: () -> Unit,
    onNavigateToReports: () -> Unit,
    onNavigateToBudgets: () -> Unit,
    onNavigateToAccounts: () -> Unit,
    onNavigateToCategories: () -> Unit,
    onNavigateToSavings: () -> Unit,
    onNavigateToRecurring: () -> Unit,
    onNavigateToCalendar: () -> Unit,
    onNavigateToInsights: () -> Unit
) {
    widgets.forEach { widget ->
        when (widget) {
            DashboardWidgetType.BALANCE -> item(key = widget.key) {
                BalanceHeroCard(
                    totalBalance = state.summary.totalBalance,
                    monthlyIncome = state.summary.monthlyIncome,
                    monthlyExpense = state.summary.monthlyExpense,
                    netSavings = state.summary.netSavings,
                    expenseDeltaPct = state.summary.previousMonthExpenseDeltaPercentage,
                    currencyCode = currencyCode
                )
            }
            DashboardWidgetType.QUICK_ACTIONS -> item(key = widget.key) {
                QuickActionsStrip(
                    onAddTransaction = onNavigateToAddTransaction,
                    onAccounts = onNavigateToAccounts,
                    onCategories = onNavigateToCategories,
                    onSavings = onNavigateToSavings,
                    onRecurring = onNavigateToRecurring,
                    onCalendar = onNavigateToCalendar,
                    onInsights = onNavigateToInsights
                )
            }
            DashboardWidgetType.ACCOUNTS -> if (state.accounts.isNotEmpty()) {
                item(key = widget.key) {
                    AccountsPreviewStrip(
                        accounts = state.accounts,
                        onAccountClick = onNavigateToAccounts
                    )
                }
            }
            DashboardWidgetType.BUDGET -> item(key = widget.key) {
                BudgetHealthCard(
                    totalSpent = state.summary.totalSpentBudget,
                    totalBudget = state.summary.totalMonthlyBudget,
                    currencyCode = currencyCode,
                    onClick = onNavigateToBudgets
                )
            }
            DashboardWidgetType.SPENDING -> item(key = widget.key) {
                SpendingOverviewCard(
                    categories = state.summary.categorySpendingList,
                    totalExpense = state.summary.monthlyExpense,
                    highestCategoryName = state.summary.highestSpendingCategoryName,
                    currencyCode = currencyCode,
                    onViewReports = onNavigateToReports
                )
            }
            DashboardWidgetType.INSIGHTS -> if (state.preferences.isInsightsEnabled && state.insights.isNotEmpty()) {
                item(key = widget.key) {
                    InsightsPreviewCard(
                        insight = state.insights.first(),
                        onClick = onNavigateToInsights
                    )
                }
            }
            DashboardWidgetType.TRANSACTIONS -> {
                item(key = "${widget.key}_header") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppTypography.TitleMedium(
                            text = stringResource(R.string.recent_transactions),
                            fontWeight = FontWeight.SemiBold
                        )
                        AppTextButton(onClick = onNavigateToTransactions) {
                            AppTypography.LabelLarge(
                                text = stringResource(R.string.see_all),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
                if (state.recentTransactions.isEmpty()) {
                    item(key = "${widget.key}_empty") {
                        EmptyStateView(
                            title = stringResource(R.string.no_transactions),
                            description = stringResource(R.string.no_transactions_message)
                        )
                    }
                } else {
                    items(state.recentTransactions.take(4), key = { it.id }) { tx ->
                        RecentTransactionItem(
                            tx = tx,
                            currencyCode = currencyCode,
                            onClick = { onNavigateToEditTransaction(tx.id) }
                        )
                    }
                }
            }
        }
    }
}
