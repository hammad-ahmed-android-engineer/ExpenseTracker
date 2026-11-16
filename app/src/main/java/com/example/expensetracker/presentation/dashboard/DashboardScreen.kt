package com.example.expensetracker.presentation.dashboard

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Tune
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppIconButton
import com.example.expensetracker.core.designsystem.components.AppScaffold
import com.example.expensetracker.core.designsystem.components.AppTopAppBar
import com.example.expensetracker.domain.model.DashboardWidgetType
import com.example.expensetracker.presentation.dashboard.components.DashboardCustomizeBottomSheet
import com.example.expensetracker.presentation.dashboard.components.dashboardWidgetItems

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
    val state by viewModel.uiState.collectAsStateWithLifecycle()
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
                    dashboardWidgetItems(
                        widgets = leftWidgets,
                        state = state,
                        currencyCode = currencyCode,
                        onNavigateToAddTransaction = onNavigateToAddTransaction,
                        onNavigateToEditTransaction = onNavigateToEditTransaction,
                        onNavigateToTransactions = onNavigateToTransactions,
                        onNavigateToReports = onNavigateToReports,
                        onNavigateToBudgets = onNavigateToBudgets,
                        onNavigateToAccounts = onNavigateToAccounts,
                        onNavigateToCategories = onNavigateToCategories,
                        onNavigateToSavings = onNavigateToSavings,
                        onNavigateToRecurring = onNavigateToRecurring,
                        onNavigateToCalendar = onNavigateToCalendar,
                        onNavigateToInsights = onNavigateToInsights
                    )
                    item { Spacer(modifier = Modifier.height(AppDimensions.components.fab)) }
                }

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentPadding = PaddingValues(vertical = AppDimensions.paddingSmall),
                    verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
                ) {
                    dashboardWidgetItems(
                        widgets = rightWidgets,
                        state = state,
                        currencyCode = currencyCode,
                        onNavigateToAddTransaction = onNavigateToAddTransaction,
                        onNavigateToEditTransaction = onNavigateToEditTransaction,
                        onNavigateToTransactions = onNavigateToTransactions,
                        onNavigateToReports = onNavigateToReports,
                        onNavigateToBudgets = onNavigateToBudgets,
                        onNavigateToAccounts = onNavigateToAccounts,
                        onNavigateToCategories = onNavigateToCategories,
                        onNavigateToSavings = onNavigateToSavings,
                        onNavigateToRecurring = onNavigateToRecurring,
                        onNavigateToCalendar = onNavigateToCalendar,
                        onNavigateToInsights = onNavigateToInsights
                    )
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
                dashboardWidgetItems(
                    widgets = visibleWidgets,
                    state = state,
                    currencyCode = currencyCode,
                    onNavigateToAddTransaction = onNavigateToAddTransaction,
                    onNavigateToEditTransaction = onNavigateToEditTransaction,
                    onNavigateToTransactions = onNavigateToTransactions,
                    onNavigateToReports = onNavigateToReports,
                    onNavigateToBudgets = onNavigateToBudgets,
                    onNavigateToAccounts = onNavigateToAccounts,
                    onNavigateToCategories = onNavigateToCategories,
                    onNavigateToSavings = onNavigateToSavings,
                    onNavigateToRecurring = onNavigateToRecurring,
                    onNavigateToCalendar = onNavigateToCalendar,
                    onNavigateToInsights = onNavigateToInsights
                )
                item { Spacer(modifier = Modifier.height(AppDimensions.components.fab * 1.4f)) }
            }
        }
    }

    if (showCustomizeSheet) {
        DashboardCustomizeBottomSheet(
            preferences = state.preferences,
            onToggleWidget = { viewModel.toggleWidgetVisibility(it) },
            onMoveWidget = { widget, up -> viewModel.moveWidget(widget, up) },
            onDismiss = { showCustomizeSheet = false }
        )
    }
}
