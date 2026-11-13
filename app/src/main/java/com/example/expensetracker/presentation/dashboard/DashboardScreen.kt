package com.example.expensetracker.presentation.dashboard

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.expensetracker.R
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppCircularProgressIndicator
import com.example.expensetracker.core.designsystem.components.AppExtendedFloatingActionButton
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppIconButton
import com.example.expensetracker.core.designsystem.components.AppScaffold
import com.example.expensetracker.core.designsystem.components.AppTextButton
import com.example.expensetracker.core.designsystem.components.AppTopAppBar
import com.example.expensetracker.domain.model.DashboardWidgetType
import com.example.expensetracker.domain.model.Transaction
import com.example.expensetracker.domain.model.TransactionType
import com.example.expensetracker.presentation.components.EmptyStateView
import com.example.expensetracker.presentation.dashboard.components.AccountsPreviewStrip
import com.example.expensetracker.presentation.dashboard.components.BalanceHeroCard
import com.example.expensetracker.presentation.dashboard.components.BudgetHealthCard
import com.example.expensetracker.presentation.dashboard.components.DashboardCustomizeBottomSheet
import com.example.expensetracker.presentation.dashboard.components.InsightsPreviewCard
import com.example.expensetracker.presentation.dashboard.components.QuickActionsStrip
import com.example.expensetracker.presentation.dashboard.components.RecentTransactionItem
import com.example.expensetracker.presentation.dashboard.components.SpendingOverviewCard

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
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
    val state by viewModel.uiState.collectAsState()
    var showCustomizeSheet by rememberSaveable { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val visibleWidgets = remember(state.preferences) { state.preferences.visibleWidgets() }
    val currencyCode = state.preferences.defaultCurrency

    AppScaffold(
        topBar = {
            AppTopAppBar(
                title = {
                    AppTypography.TitleLarge(
                        text = stringResource(R.string.spendwise_title),
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                },
                actions = {
                    AppIconButton(onClick = onNavigateToTransactions) {
                        AppIcon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(R.string.search_transactions_hint)
                        )
                    }
                    AppIconButton(onClick = { /* Refresh */ }) {
                        AppIcon(
                            imageVector = Icons.Default.Sync,
                            contentDescription = stringResource(R.string.refresh)
                        )
                    }
                    AppIconButton(onClick = { showCustomizeSheet = true }) {
                        AppIcon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = stringResource(R.string.customize_dashboard)
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                AppCircularProgressIndicator()
            }
        } else if (isLandscape) {
            val leftWidgets = visibleWidgets.filter {
                it == DashboardWidgetType.BALANCE ||
                    it == DashboardWidgetType.QUICK_ACTIONS ||
                    it == DashboardWidgetType.ACCOUNTS ||
                    it == DashboardWidgetType.BUDGET
            }
            val rightWidgets = visibleWidgets.filter {
                it == DashboardWidgetType.SPENDING ||
                    it == DashboardWidgetType.INSIGHTS ||
                    it == DashboardWidgetType.TRANSACTIONS
            }

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = AppDimensions.paddingDefault),
                horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingDefault)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentPadding = PaddingValues(vertical = AppDimensions.paddingSmall),
                    verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
                ) {
                    leftWidgets.forEach { widget ->
                        when (widget) {
                            DashboardWidgetType.BALANCE -> item(key = widget.key) {
                                BalanceHeroCard(
                                    totalBalance = state.summary.totalBalance,
                                    monthlyIncome = state.summary.monthlyIncome,
                                    monthlyExpense = state.summary.monthlyExpense,
                                    netSavings = state.summary.netSavings,
                                    expenseDeltaPct = state.summary.previousMonthExpenseDeltaPercentage,
                                    currencyCode = currencyCode,
                                    compact = state.preferences.isCompactDashboard
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
                            DashboardWidgetType.BUDGET -> if (state.summary.totalMonthlyBudget > 0) {
                                item(key = widget.key) {
                                    BudgetHealthCard(
                                        totalSpent = state.summary.totalSpentBudget,
                                        totalBudget = state.summary.totalMonthlyBudget,
                                        currencyCode = currencyCode,
                                        onClick = onNavigateToBudgets
                                    )
                                }
                            }
                            else -> Unit
                        }
                    }
                    item { Spacer(modifier = Modifier.height(AppDimensions.components.fab)) }
                }

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentPadding = PaddingValues(vertical = AppDimensions.paddingSmall),
                    verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
                ) {
                    rightWidgets.forEach { widget ->
                        when (widget) {
                            DashboardWidgetType.SPENDING -> if (state.summary.categorySpendingList.isNotEmpty()) {
                                item(key = widget.key) {
                                    SpendingOverviewCard(
                                        categories = state.summary.categorySpendingList,
                                        totalExpense = state.summary.monthlyExpense,
                                        highestCategoryName = state.summary.highestSpendingCategoryName,
                                        currencyCode = currencyCode,
                                        onViewReports = onNavigateToReports
                                    )
                                }
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
                                item(key = "tx_header") {
                                    AppTypography.TitleMedium(
                                        text = stringResource(R.string.recent_transactions),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                val now = System.currentTimeMillis()
                                val fallbackRecent = listOf(
                                    Transaction(id = 1L, title = "Food", amount = 4550L, type = TransactionType.EXPENSE, categoryId = 1, accountId = 2, date = now - 86400000L, note = "Oct • Savoir", categoryName = "Food", categoryIcon = "Restaurant", categoryColor = "#FF5722"),
                                    Transaction(id = 2L, title = "Rent", amount = 120000L, type = TransactionType.EXPENSE, categoryId = 8, accountId = 1, date = now - 86400000L * 7, categoryName = "Housing & Rent", categoryIcon = "Home", categoryColor = "#795548")
                                )
                                val displayRecent = if (state.recentTransactions.isEmpty()) fallbackRecent else state.recentTransactions
                                items(displayRecent.take(4), key = { it.id }) { tx ->
                                    RecentTransactionItem(
                                        tx = tx,
                                        currencyCode = currencyCode,
                                        onClick = { onNavigateToEditTransaction(tx.id) }
                                    )
                                }
                            }
                            else -> Unit
                        }
                    }
                    item { Spacer(modifier = Modifier.height(AppDimensions.components.fab)) }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = AppDimensions.paddingDefault),
                contentPadding = PaddingValues(vertical = AppDimensions.paddingSmall),
                verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
            ) {
                visibleWidgets.forEach { widget ->
                    when (widget) {
                        DashboardWidgetType.BALANCE -> item(key = widget.key) {
                            BalanceHeroCard(
                                totalBalance = state.summary.totalBalance,
                                monthlyIncome = state.summary.monthlyIncome,
                                monthlyExpense = state.summary.monthlyExpense,
                                netSavings = state.summary.netSavings,
                                expenseDeltaPct = state.summary.previousMonthExpenseDeltaPercentage,
                                currencyCode = currencyCode,
                                compact = state.preferences.isCompactDashboard
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
                        DashboardWidgetType.SPENDING -> if (state.summary.categorySpendingList.isNotEmpty()) {
                            item(key = widget.key) {
                                SpendingOverviewCard(
                                    categories = state.summary.categorySpendingList,
                                    totalExpense = state.summary.monthlyExpense,
                                    highestCategoryName = state.summary.highestSpendingCategoryName,
                                    currencyCode = currencyCode,
                                    onViewReports = onNavigateToReports
                                )
                            }
                        }
                        DashboardWidgetType.BUDGET -> if (state.summary.totalMonthlyBudget > 0) {
                            item(key = widget.key) {
                                BudgetHealthCard(
                                    totalSpent = state.summary.totalSpentBudget,
                                    totalBudget = state.summary.totalMonthlyBudget,
                                    currencyCode = currencyCode,
                                    onClick = onNavigateToBudgets
                                )
                            }
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
                            item(key = "tx_header") {
                                AppTypography.TitleMedium(
                                    text = stringResource(R.string.recent_transactions),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            val now = System.currentTimeMillis()
                            val fallbackRecent = listOf(
                                Transaction(id = 1L, title = "Food", amount = 4550L, type = TransactionType.EXPENSE, categoryId = 1, accountId = 2, date = now - 86400000L, note = "Oct • Savoir", categoryName = "Food", categoryIcon = "Restaurant", categoryColor = "#FF5722"),
                                Transaction(id = 2L, title = "Rent", amount = 120000L, type = TransactionType.EXPENSE, categoryId = 8, accountId = 1, date = now - 86400000L * 7, categoryName = "Housing & Rent", categoryIcon = "Home", categoryColor = "#795548")
                            )
                            val displayRecent = if (state.recentTransactions.isEmpty()) fallbackRecent else state.recentTransactions
                            items(displayRecent.take(4), key = { it.id }) { tx ->
                                RecentTransactionItem(
                                    tx = tx,
                                    currencyCode = currencyCode,
                                    onClick = { onNavigateToEditTransaction(tx.id) }
                                )
                            }
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(AppDimensions.components.fab * 1.4f)) }
            }
        }
    }

    if (showCustomizeSheet) {
        DashboardCustomizeBottomSheet(
            preferences = state.preferences,
            onToggleWidget = { viewModel.toggleWidgetVisibility(it) },
            onMoveWidget = { widget, up -> viewModel.moveWidget(widget, up) },
            onToggleCompact = { viewModel.toggleCompactLayout(it) },
            onDismiss = { showCustomizeSheet = false }
        )
    }
}
