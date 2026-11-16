package com.example.expensetracker.presentation.savings

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
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
import com.example.expensetracker.presentation.budgets.components.SetNewGoalActionCard
import com.example.expensetracker.presentation.components.EmptyStateView
import com.example.expensetracker.presentation.savings.components.AddSavingsGoalDialog
import com.example.expensetracker.presentation.savings.components.GoalFundsDialog
import com.example.expensetracker.presentation.savings.components.SavingsGoalCardItem

@Composable
fun SavingsGoalsScreen(
    viewModel: SavingsViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showAddDialog by rememberSaveable { mutableStateOf(false) }
    var selectedGoalForDepositId by rememberSaveable { mutableStateOf<Long?>(null) }
    var pendingDeleteGoalId by rememberSaveable { mutableStateOf<Long?>(null) }
    val selectedGoalForDeposit = selectedGoalForDepositId?.let { id -> state.goals.find { it.id == id } }
    val pendingDeleteGoal = pendingDeleteGoalId?.let { id -> state.goals.find { it.id == id } }
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.message) {
        state.message?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearMessage()
        }
    }

    AppScaffold(
        topBar = {
            AppTopAppBar(
                title = { AppTypography.TitleLarge(text = stringResource(R.string.savings_goals_title)) },
                navigationIcon = {
                    AppIconButton(onClick = onNavigateBack) {
                        AppIcon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
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
        } else if (state.goals.isEmpty()) {
            EmptyStateView(
                title = stringResource(R.string.no_savings_title),
                description = stringResource(R.string.no_savings_desc),
                actionText = stringResource(R.string.add_goal),
                onActionClick = { showAddDialog = true },
                modifier = Modifier.padding(padding)
            )
        } else if (isLandscape) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = AppDimensions.paddingDefault),
                contentPadding = PaddingValues(vertical = AppDimensions.paddingSmall),
                horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingDefault),
                verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingDefault)
            ) {
                items(state.goals, key = { it.id }) { goal ->
                    SavingsGoalCardItem(
                        goal = goal,
                        currencyCode = state.currencyCode,
                        onDepositClick = { selectedGoalForDepositId = goal.id },
                        onDelete = { pendingDeleteGoalId = goal.id }
                    )
                }
                item {
                    SetNewGoalActionCard(onClick = { showAddDialog = true })
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
                item(key = "set_new_goal_inline_card") {
                    SetNewGoalActionCard(onClick = { showAddDialog = true })
                }
                items(state.goals, key = { it.id }) { goal ->
                    SavingsGoalCardItem(
                        goal = goal,
                        currencyCode = state.currencyCode,
                        onDepositClick = { selectedGoalForDepositId = goal.id },
                        onDelete = { pendingDeleteGoalId = goal.id }
                    )
                }
                item { Spacer(modifier = Modifier.height(AppDimensions.paddingLarge)) }
            }
        }
    }

    if (showAddDialog) {
        AddSavingsGoalDialog(
            onConfirm = { title, targetAmount, description ->
                viewModel.addGoal(title, targetAmount, description)
            },
            onDismiss = { showAddDialog = false }
        )
    }

    selectedGoalForDeposit?.let { goal ->
        GoalFundsDialog(
            goal = goal,
            onConfirm = { amount, isDeposit ->
                viewModel.addContribution(goal.id, amount, isDeposit)
            },
            onDismiss = { selectedGoalForDepositId = null }
        )
    }

    pendingDeleteGoal?.let { goal ->
        AppAlertDialog(
            onDismissRequest = { pendingDeleteGoalId = null },
            title = stringResource(R.string.confirm_delete_goal),
            text = { AppTypography.BodyMedium(text = stringResource(R.string.delete_goal_warning)) },
            confirmButtonText = stringResource(R.string.action_delete),
            onConfirm = {
                viewModel.deleteGoal(goal)
                pendingDeleteGoalId = null
            },
            dismissButtonText = stringResource(R.string.action_cancel),
            onDismiss = { pendingDeleteGoalId = null },
            isDestructive = true
        )
    }
}
