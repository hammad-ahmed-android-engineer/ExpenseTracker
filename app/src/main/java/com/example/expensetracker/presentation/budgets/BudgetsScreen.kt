package com.example.expensetracker.presentation.budgets

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.expensetracker.R
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppAlertDialog
import com.example.expensetracker.core.designsystem.components.AppCircularProgressIndicator
import com.example.expensetracker.core.designsystem.components.AppFloatingActionButton
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppIconButton
import com.example.expensetracker.core.designsystem.components.AppScaffold
import com.example.expensetracker.core.designsystem.components.AppSnackbarHost
import com.example.expensetracker.core.designsystem.components.AppTopAppBar
import com.example.expensetracker.domain.model.Budget
import com.example.expensetracker.domain.model.BudgetPeriodType
import com.example.expensetracker.domain.model.SavingsGoal
import com.example.expensetracker.presentation.budgets.components.AddGoalFundsDialog
import com.example.expensetracker.presentation.budgets.components.BudgetCardItem
import com.example.expensetracker.presentation.budgets.components.BudgetQuickActionSheet
import com.example.expensetracker.presentation.budgets.components.BudgetSectionHeader
import com.example.expensetracker.presentation.budgets.components.SetBudgetBottomSheet
import com.example.expensetracker.presentation.budgets.components.SetNewGoalActionCard
import com.example.expensetracker.presentation.budgets.components.SetSavingsGoalBottomSheet
import com.example.expensetracker.presentation.savings.components.SavingsGoalCardItem

@Composable
fun BudgetsScreen(
    viewModel: BudgetViewModel,
    onNavigateToSavings: () -> Unit,
    onNavigateToRecurring: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showSetBudgetSheet by rememberSaveable { mutableStateOf(false) }
    var showSetGoalSheet by rememberSaveable { mutableStateOf(false) }
    var pendingDeleteBudgetId by rememberSaveable { mutableStateOf<Long?>(null) }
    var pendingDeleteGoalId by rememberSaveable { mutableStateOf<Long?>(null) }
    var selectedGoalForFundsId by rememberSaveable { mutableStateOf<Long?>(null) }
    val pendingDeleteBudget = pendingDeleteBudgetId?.let { id -> state.budgets.find { it.id == id } }
    val pendingDeleteGoal = pendingDeleteGoalId?.let { id -> state.savingsGoals.find { it.id == id } }
    val selectedGoalForFunds = selectedGoalForFundsId?.let { id -> state.savingsGoals.find { it.id == id } }
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.message) {
        state.message?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearMessage()
        }
    }

    val displayBudgets = if (state.budgets.isEmpty()) {
        listOf(
            Budget(
                name = "Groceries",
                amountLimit = 35000L,
                spentAmount = 31000L,
                periodType = BudgetPeriodType.MONTHLY,
                startDate = 0L,
                endDate = 0L
            ),
            Budget(
                name = "Entertainment",
                amountLimit = 10000L,
                spentAmount = 9000L,
                periodType = BudgetPeriodType.MONTHLY,
                startDate = 0L,
                endDate = 0L
            )
        )
    } else {
        state.budgets
    }

    val displayGoals = if (state.savingsGoals.isEmpty()) {
        listOf(
            SavingsGoal(
                id = 1L,
                title = "New Laptop",
                currentAmount = 80000L,
                targetAmount = 100000L,
                targetDate = System.currentTimeMillis() + 86400000L * 180
            ),
            SavingsGoal(
                id = 2L,
                title = "Emergency Fund",
                currentAmount = 500000L,
                targetAmount = 1000000L,
                targetDate = System.currentTimeMillis() + 86400000L * 365
            )
        )
    } else {
        state.savingsGoals
    }

    AppScaffold(
        topBar = {
            AppTopAppBar(
                title = {
                    AppTypography.TitleLarge(
                        text = stringResource(R.string.budgets_savings_title),
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
        snackbarHost = { AppSnackbarHost(hostState = snackbarHostState) }
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
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = AppDimensions.paddingDefault),
                horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingDefault)
            ) {
                // Left Column: Budgets
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentPadding = PaddingValues(vertical = AppDimensions.paddingSmall),
                    verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
                ) {
                    item(key = "budgets_col_header") {
                        BudgetSectionHeader(
                            title = stringResource(R.string.nav_budgets),
                            actionLabel = stringResource(R.string.set_budget),
                            onActionClick = { showSetBudgetSheet = true }
                        )
                    }
                    items(displayBudgets, key = { it.id.toString() + it.name }) { budget ->
                        BudgetCardItem(
                            budget = budget,
                            currencyCode = state.currencyCode,
                            onDelete = { pendingDeleteBudgetId = budget.id }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(AppDimensions.components.fab * 1.5f)) }
                }

                // Right Column: Savings Goals
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentPadding = PaddingValues(vertical = AppDimensions.paddingSmall),
                    verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
                ) {
                    item(key = "savings_col_header") {
                        BudgetSectionHeader(
                            title = stringResource(R.string.nav_savings),
                            actionLabel = stringResource(R.string.set_goal),
                            onActionClick = { showSetGoalSheet = true },
                            buttonContainerColor = Color(0xFFA5B4FC),
                            buttonContentColor = Color(0xFF1E1B4B)
                        )
                    }
                    items(displayGoals, key = { it.id.toString() + it.title }) { goal ->
                        SavingsGoalCardItem(
                            goal = goal,
                            currencyCode = state.currencyCode,
                            onDepositClick = { selectedGoalForFundsId = goal.id },
                            onDelete = { pendingDeleteGoalId = goal.id }
                        )
                    }

                    // Inline Set Goal Action Card
                    item(key = "set_new_goal_inline_card_landscape") {
                        SetNewGoalActionCard(onClick = { showSetGoalSheet = true })
                    }

                    item { Spacer(modifier = Modifier.height(AppDimensions.components.fab * 1.5f)) }
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
                // Section 1: Budgets Header
                item(key = "budgets_header") {
                    BudgetSectionHeader(
                        title = stringResource(R.string.nav_budgets),
                        actionLabel = stringResource(R.string.set_budget),
                        onActionClick = { showSetBudgetSheet = true },
                        isLargeTitle = true
                    )
                }

                items(displayBudgets, key = { it.id.toString() + it.name }) { budget ->
                    BudgetCardItem(
                        budget = budget,
                        currencyCode = state.currencyCode,
                        onDelete = { pendingDeleteBudgetId = budget.id }
                    )
                }

                // Section 2: Savings Goals Header
                item(key = "savings_header") {
                    Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))
                    BudgetSectionHeader(
                        title = stringResource(R.string.nav_savings),
                        actionLabel = stringResource(R.string.set_goal),
                        onActionClick = { showSetGoalSheet = true },
                        isLargeTitle = true,
                        buttonContainerColor = Color(0xFFA5B4FC),
                        buttonContentColor = Color(0xFF1E1B4B)
                    )
                }

                items(displayGoals, key = { it.id.toString() + it.title }) { goal ->
                    SavingsGoalCardItem(
                        goal = goal,
                        currencyCode = state.currencyCode,
                        onDepositClick = { selectedGoalForFundsId = goal.id },
                        onDelete = { pendingDeleteGoalId = goal.id }
                    )
                }

                // Inline Set Goal Action Card on screen
                // Inline Set Goal Action Card on screen
                item(key = "set_new_goal_inline_card_portrait") {
                    SetNewGoalActionCard(onClick = { showSetGoalSheet = true })
                }

                item { Spacer(modifier = Modifier.height(AppDimensions.components.fab * 1.5f)) }
            }
        }
    }


    if (showSetBudgetSheet) {
        SetBudgetBottomSheet(
            currencyCode = state.currencyCode,
            expenseCategories = state.expenseCategories,
            onSave = { name, limit, periodType, notifyThreshold, isRolloverEnabled, category ->
                viewModel.addBudget(
                    name = name,
                    amountLimitString = limit,
                    periodType = periodType,
                    notifyThreshold = notifyThreshold,
                    isRolloverEnabled = isRolloverEnabled,
                    category = category
                )
                showSetBudgetSheet = false
            },
            onDismiss = { showSetBudgetSheet = false }
        )
    }

    if (showSetGoalSheet) {
        SetSavingsGoalBottomSheet(
            currencyCode = state.currencyCode,
            onSave = { title, targetAmount, targetDate, colorHex, description, initialDeposit ->
                viewModel.addSavingsGoal(
                    title = title,
                    targetAmountString = targetAmount,
                    targetDate = targetDate,
                    colorHex = colorHex,
                    description = description,
                    initialDepositString = initialDeposit
                )
                showSetGoalSheet = false
            },
            onDismiss = { showSetGoalSheet = false }
        )
    }

    selectedGoalForFunds?.let { goal ->
        AddGoalFundsDialog(
            goal = goal,
            onConfirm = { amt -> viewModel.addFundsToGoal(goal.id, amt) },
            onDismiss = { selectedGoalForFundsId = null }
        )
    }

    pendingDeleteBudget?.let { budget ->
        AppAlertDialog(
            onDismissRequest = { pendingDeleteBudgetId = null },
            title = stringResource(R.string.confirm_delete_budget),
            text = { AppTypography.BodyMedium(text = stringResource(R.string.delete_budget_warning)) },
            confirmButtonText = stringResource(R.string.action_delete),
            onConfirm = {
                viewModel.deleteBudget(budget)
                pendingDeleteBudgetId = null
            },
            dismissButtonText = stringResource(R.string.action_cancel),
            onDismiss = { pendingDeleteBudgetId = null },
            isDestructive = true
        )
    }

    pendingDeleteGoal?.let { goal ->
        AppAlertDialog(
            onDismissRequest = { pendingDeleteGoalId = null },
            title = stringResource(R.string.confirm_delete_savings_goal),
            text = {
                AppTypography.BodyMedium(
                    text = stringResource(
                        R.string.delete_savings_goal_warning,
                        goal.title
                    )
                )
            },
            confirmButtonText = stringResource(R.string.action_delete),
            onConfirm = {
                viewModel.deleteSavingsGoal(goal)
                pendingDeleteGoalId = null
            },
            dismissButtonText = stringResource(R.string.action_cancel),
            onDismiss = { pendingDeleteGoalId = null },
            isDestructive = true
        )
    }
}
