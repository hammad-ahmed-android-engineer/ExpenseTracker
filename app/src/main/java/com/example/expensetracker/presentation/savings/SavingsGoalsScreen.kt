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
import com.example.expensetracker.core.designsystem.components.AppCircularProgressIndicator
import com.example.expensetracker.core.designsystem.components.AppFloatingActionButton
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppIconButton
import com.example.expensetracker.core.designsystem.components.AppScaffold
import com.example.expensetracker.core.designsystem.components.AppTopAppBar
import com.example.expensetracker.domain.model.SavingsGoal
import com.example.expensetracker.presentation.components.ConfirmationDialog
import com.example.expensetracker.presentation.components.EmptyStateView
import com.example.expensetracker.presentation.savings.components.AddContributionDialog
import com.example.expensetracker.presentation.savings.components.AddSavingsGoalDialog
import com.example.expensetracker.presentation.savings.components.SavingsGoalCardItem

@Composable
fun SavingsGoalsScreen(
    viewModel: SavingsViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedGoalForDeposit by remember { mutableStateOf<SavingsGoal?>(null) }
    var pendingDeleteGoal by remember { mutableStateOf<SavingsGoal?>(null) }
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

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
        floatingActionButton = {
            AppFloatingActionButton(onClick = { showAddDialog = true }) {
                AppIcon(imageVector = Icons.Default.Add, contentDescription = stringResource(R.string.add_goal))
            }
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
                        onDepositClick = { selectedGoalForDeposit = goal },
                        onDelete = { pendingDeleteGoal = goal }
                    )
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
                items(state.goals, key = { it.id }) { goal ->
                    SavingsGoalCardItem(
                        goal = goal,
                        currencyCode = state.currencyCode,
                        onDepositClick = { selectedGoalForDeposit = goal },
                        onDelete = { pendingDeleteGoal = goal }
                    )
                }
                item { Spacer(modifier = Modifier.height(AppDimensions.components.fab * 1.4f)) }
            }
        }
    }

    if (showAddDialog) {
        AddSavingsGoalDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { title, target, desc ->
                viewModel.addGoal(title, target, desc)
                showAddDialog = false
            }
        )
    }

    selectedGoalForDeposit?.let { goal ->
        AddContributionDialog(
            goalTitle = goal.title,
            onDismiss = { selectedGoalForDeposit = null },
            onConfirm = { amount, isDeposit ->
                viewModel.addContribution(goal.id, amount, isDeposit)
                selectedGoalForDeposit = null
            }
        )
    }

    pendingDeleteGoal?.let { goal ->
        ConfirmationDialog(
            title = "Delete Savings Goal",
            message = "Delete \"${goal.title}\" and its contribution history?",
            onConfirm = {
                viewModel.deleteGoal(goal)
                pendingDeleteGoal = null
            },
            onDismiss = { pendingDeleteGoal = null }
        )
    }
}
