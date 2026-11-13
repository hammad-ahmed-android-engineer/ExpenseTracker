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
import androidx.compose.material.icons.filled.Savings
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
import com.example.expensetracker.R
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppAlertDialog
import com.example.expensetracker.core.designsystem.components.AppCircularProgressIndicator
import com.example.expensetracker.core.designsystem.components.AppFilterChip
import com.example.expensetracker.core.designsystem.components.AppFloatingActionButton
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppIconButton
import com.example.expensetracker.core.designsystem.components.AppOutlinedTextField
import com.example.expensetracker.core.designsystem.components.AppScaffold
import com.example.expensetracker.core.designsystem.components.AppSegmentedSelector
import com.example.expensetracker.core.designsystem.components.AppTopAppBar
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import com.example.expensetracker.domain.model.SavingsGoal
import com.example.expensetracker.presentation.components.EmptyStateView
import com.example.expensetracker.presentation.savings.components.SavingsGoalCardItem

@Composable
fun SavingsGoalsScreen(
    viewModel: SavingsViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var showAddDialog by rememberSaveable { mutableStateOf(false) }
    var addGoalTitle by rememberSaveable { mutableStateOf("") }
    var addGoalTarget by rememberSaveable { mutableStateOf("") }
    var addGoalDesc by rememberSaveable { mutableStateOf("") }

    var selectedGoalForDeposit by remember { mutableStateOf<SavingsGoal?>(null) }
    var depositAmount by rememberSaveable { mutableStateOf("") }
    var depositIsDeposit by rememberSaveable { mutableStateOf(true) }

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
        AppAlertDialog(
            onDismissRequest = {
                showAddDialog = false
                addGoalTitle = ""
                addGoalTarget = ""
                addGoalDesc = ""
            },
            title = stringResource(R.string.new_savings_goal),
            icon = Icons.Default.Savings,
            subtitle = stringResource(R.string.new_savings_goal_subtitle),
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)) {
                    AppOutlinedTextField(
                        value = addGoalTitle,
                        onValueChange = { addGoalTitle = it },
                        label = { AppTypography.LabelMedium(text = stringResource(R.string.goal_title)) },
                        placeholder = { AppTypography.BodyMedium(text = stringResource(R.string.goal_title_placeholder)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    AppOutlinedTextField(
                        value = addGoalTarget,
                        onValueChange = { addGoalTarget = it },
                        label = { AppTypography.LabelMedium(text = stringResource(R.string.target_amount)) },
                        placeholder = { AppTypography.BodyMedium(text = stringResource(R.string.target_amount_placeholder)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )
                    AppOutlinedTextField(
                        value = addGoalDesc,
                        onValueChange = { addGoalDesc = it },
                        label = { AppTypography.LabelMedium(text = stringResource(R.string.goal_desc_label)) },
                        placeholder = { AppTypography.BodyMedium(text = stringResource(R.string.goal_desc_placeholder)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButtonText = stringResource(R.string.action_save),
            confirmEnabled = addGoalTitle.trim().isNotEmpty() && (addGoalTarget.toDoubleOrNull() ?: 0.0) > 0.0,
            onConfirm = {
                viewModel.addGoal(addGoalTitle, addGoalTarget, addGoalDesc.ifBlank { null })
                showAddDialog = false
                addGoalTitle = ""
                addGoalTarget = ""
                addGoalDesc = ""
            },
            dismissButtonText = stringResource(R.string.action_cancel),
            onDismiss = {
                showAddDialog = false
                addGoalTitle = ""
                addGoalTarget = ""
                addGoalDesc = ""
            }
        )
    }

    selectedGoalForDeposit?.let { goal ->
        val depositLabel = stringResource(R.string.deposit)
        val withdrawLabel = stringResource(R.string.withdraw)
        val quickAmounts = listOf("10", "25", "50", "100")

        AppAlertDialog(
            onDismissRequest = {
                selectedGoalForDeposit = null
                depositAmount = ""
                depositIsDeposit = true
            },
            title = stringResource(R.string.funds_for_goal, goal.title),
            icon = Icons.Default.Savings,
            subtitle = stringResource(R.string.funds_for_goal_desc),
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)) {
                    AppSegmentedSelector(
                        options = listOf(true, false),
                        selectedOption = depositIsDeposit,
                        onOptionSelected = { depositIsDeposit = it },
                        labelProvider = { if (it) depositLabel else withdrawLabel }
                    )

                    AppOutlinedTextField(
                        value = depositAmount,
                        onValueChange = { depositAmount = it },
                        label = { AppTypography.LabelMedium(text = stringResource(R.string.amount)) },
                        placeholder = { AppTypography.BodyMedium(text = stringResource(R.string.amount_placeholder)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
                    ) {
                        quickAmounts.forEach { quickVal ->
                            AppFilterChip(
                                selected = depositAmount == quickVal,
                                onClick = { depositAmount = quickVal },
                                label = { AppTypography.LabelSmall(text = "+$$quickVal") },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            },
            confirmButtonText = stringResource(R.string.action_submit),
            confirmEnabled = (depositAmount.toDoubleOrNull() ?: 0.0) > 0.0,
            onConfirm = {
                viewModel.addContribution(goal.id, depositAmount, depositIsDeposit)
                selectedGoalForDeposit = null
                depositAmount = ""
                depositIsDeposit = true
            },
            dismissButtonText = stringResource(R.string.action_cancel),
            onDismiss = {
                selectedGoalForDeposit = null
                depositAmount = ""
                depositIsDeposit = true
            }
        )
    }

    pendingDeleteGoal?.let { goal ->
        AppAlertDialog(
            onDismissRequest = { pendingDeleteGoal = null },
            title = stringResource(R.string.confirm_delete_goal),
            text = { AppTypography.BodyMedium(text = stringResource(R.string.delete_goal_warning)) },
            confirmButtonText = stringResource(R.string.action_delete),
            onConfirm = {
                viewModel.deleteGoal(goal)
                pendingDeleteGoal = null
            },
            dismissButtonText = stringResource(R.string.action_cancel),
            onDismiss = { pendingDeleteGoal = null },
            isDestructive = true
        )
    }
}
