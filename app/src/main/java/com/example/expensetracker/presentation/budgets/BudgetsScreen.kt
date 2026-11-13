package com.example.expensetracker.presentation.budgets

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import com.example.expensetracker.R
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppCard
import com.example.expensetracker.core.designsystem.components.AppCircularProgressIndicator
import com.example.expensetracker.core.designsystem.components.AppFloatingActionButton
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppIconButton
import com.example.expensetracker.core.designsystem.components.AppScaffold
import com.example.expensetracker.core.designsystem.components.AppTopAppBar
import com.example.expensetracker.domain.model.Budget
import com.example.expensetracker.presentation.budgets.components.AddBudgetDialog
import com.example.expensetracker.presentation.budgets.components.BudgetCardItem
import com.example.expensetracker.presentation.components.ConfirmationDialog
import com.example.expensetracker.presentation.components.EmptyStateView

@Composable
fun BudgetsScreen(
    viewModel: BudgetViewModel,
    onNavigateToSavings: () -> Unit,
    onNavigateToRecurring: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var pendingDeleteBudget by remember { mutableStateOf<Budget?>(null) }
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    AppScaffold(
        topBar = {
            AppTopAppBar(
                title = { AppTypography.TitleLarge(text = stringResource(R.string.budget_management)) },
                actions = {
                    AppIconButton(onClick = onNavigateToSavings) {
                        AppIcon(
                            imageVector = Icons.Default.Savings,
                            contentDescription = stringResource(R.string.nav_savings)
                        )
                    }
                    AppIconButton(onClick = onNavigateToRecurring) {
                        AppIcon(
                            imageVector = Icons.Default.Repeat,
                            contentDescription = stringResource(R.string.nav_recurring)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            AppFloatingActionButton(onClick = { showAddDialog = true }) {
                AppIcon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.create_budget)
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = AppDimensions.paddingDefault)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = AppDimensions.paddingSmall),
                horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
            ) {
                AppCard(
                    onClick = onNavigateToSavings,
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.padding(AppDimensions.paddingMedium),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
                    ) {
                        AppIcon(
                            imageVector = Icons.Default.Savings,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Column {
                            AppTypography.TitleSmall(text = stringResource(R.string.nav_savings))
                            AppTypography.Caption(text = "Track goals")
                        }
                    }
                }
                AppCard(
                    onClick = onNavigateToRecurring,
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.padding(AppDimensions.paddingMedium),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
                    ) {
                        AppIcon(
                            imageVector = Icons.Default.Repeat,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Column {
                            AppTypography.TitleSmall(text = stringResource(R.string.nav_recurring))
                            AppTypography.Caption(text = "Auto bills")
                        }
                    }
                }
            }

            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    AppCircularProgressIndicator()
                }
            } else if (state.budgets.isEmpty()) {
                EmptyStateView(
                    title = stringResource(R.string.no_budgets_title),
                    description = stringResource(R.string.no_budgets_desc),
                    actionText = stringResource(R.string.create_budget),
                    onActionClick = { showAddDialog = true }
                )
            } else if (isLandscape) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = AppDimensions.paddingSmall),
                    horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingDefault),
                    verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingDefault)
                ) {
                    items(state.budgets, key = { it.id }) { budget ->
                        BudgetCardItem(
                            budget = budget,
                            currencyCode = state.currencyCode,
                            onDelete = { pendingDeleteBudget = budget }
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = AppDimensions.paddingSmall),
                    verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
                ) {
                    items(state.budgets, key = { it.id }) { budget ->
                        BudgetCardItem(
                            budget = budget,
                            currencyCode = state.currencyCode,
                            onDelete = { pendingDeleteBudget = budget }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(AppDimensions.components.fab * 1.4f)) }
                }
            }
        }
    }

    if (showAddDialog) {
        AddBudgetDialog(
            categories = state.expenseCategories,
            onDismiss = { showAddDialog = false },
            onConfirm = { name, limit, period, threshold, rollover, cat ->
                viewModel.addBudget(name, limit, period, threshold, rollover, cat)
                showAddDialog = false
            }
        )
    }

    pendingDeleteBudget?.let { budget ->
        ConfirmationDialog(
            title = "Delete Budget",
            message = "Remove \"${budget.name}\"? Historical transactions will not be affected.",
            onConfirm = {
                viewModel.deleteBudget(budget)
                pendingDeleteBudget = null
            },
            onDismiss = { pendingDeleteBudget = null }
        )
    }
}
