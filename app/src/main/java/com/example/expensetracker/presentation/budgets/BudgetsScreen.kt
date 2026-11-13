package com.example.expensetracker.presentation.budgets

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import com.example.expensetracker.R
import com.example.expensetracker.core.common.toIsoDate
import com.example.expensetracker.core.common.toMinorUnits
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.clickableDebounced
import com.example.expensetracker.core.designsystem.components.AppAlertDialog
import com.example.expensetracker.core.designsystem.components.AppBottomSheet
import com.example.expensetracker.core.designsystem.components.AppButton
import com.example.expensetracker.core.designsystem.components.AppCard
import com.example.expensetracker.core.designsystem.components.AppCircularProgressIndicator
import com.example.expensetracker.core.designsystem.components.AppDatePickerDialog
import com.example.expensetracker.core.designsystem.components.AppFilterChip
import com.example.expensetracker.core.designsystem.components.AppFloatingActionButton
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppIconButton
import com.example.expensetracker.core.designsystem.components.AppOutlinedTextField
import com.example.expensetracker.core.designsystem.components.AppScaffold
import com.example.expensetracker.core.designsystem.components.AppSlider
import com.example.expensetracker.core.designsystem.components.AppSwitch
import com.example.expensetracker.core.designsystem.components.AppTopAppBar
import com.example.expensetracker.domain.model.Budget
import com.example.expensetracker.domain.model.BudgetPeriodType
import com.example.expensetracker.domain.model.SavingsGoal
import com.example.expensetracker.presentation.budgets.components.BudgetCardItem
import com.example.expensetracker.presentation.savings.components.SavingsGoalCardItem

@Composable
fun BudgetsScreen(
    viewModel: BudgetViewModel,
    onNavigateToSavings: () -> Unit,
    onNavigateToRecurring: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var showSetBudgetSheet by rememberSaveable { mutableStateOf(false) }
    var showSetGoalSheet by rememberSaveable { mutableStateOf(false) }
    var showQuickActionSheet by rememberSaveable { mutableStateOf(false) }
    var pendingDeleteBudget by remember { mutableStateOf<Budget?>(null) }
    var pendingDeleteGoal by remember { mutableStateOf<SavingsGoal?>(null) }
    var selectedGoalForFunds by remember { mutableStateOf<SavingsGoal?>(null) }
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    var budgetName by rememberSaveable { mutableStateOf("") }
    var budgetLimit by rememberSaveable { mutableStateOf("") }
    var budgetPeriodType by rememberSaveable { mutableStateOf(BudgetPeriodType.MONTHLY) }
    var budgetNotifyThreshold by rememberSaveable { mutableStateOf(80) }
    var budgetRolloverEnabled by rememberSaveable { mutableStateOf(false) }
    var budgetSelectedCategoryId by rememberSaveable { mutableStateOf<Long?>(null) }

    var goalTitle by rememberSaveable { mutableStateOf("") }
    var goalTargetAmount by rememberSaveable { mutableStateOf("") }
    var goalTargetDate by rememberSaveable { mutableStateOf(System.currentTimeMillis() + 86400000L * 180) }
    var goalSelectedColor by rememberSaveable { mutableStateOf("#4CAF50") }
    var goalDescription by rememberSaveable { mutableStateOf("") }
    var goalInitialDeposit by rememberSaveable { mutableStateOf("") }
    var showGoalDatePicker by rememberSaveable { mutableStateOf(false) }

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
            SavingsGoal(id = 1L, title = "New Laptop", currentAmount = 80000L, targetAmount = 100000L, targetDate = System.currentTimeMillis() + 86400000L * 180),
            SavingsGoal(id = 2L, title = "Emergency Fund", currentAmount = 500000L, targetAmount = 1000000L, targetDate = System.currentTimeMillis() + 86400000L * 365)
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
                },
                actions = {
                    AppIconButton(onClick = { showQuickActionSheet = true }) {
                        AppIcon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add New"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            AppFloatingActionButton(
                onClick = { showQuickActionSheet = true }
            ) {
                AppIcon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add New"
                )
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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AppTypography.TitleMedium(
                                text = stringResource(R.string.nav_budgets),
                                fontWeight = FontWeight.Bold
                            )
                            AppButton(
                                onClick = { showSetBudgetSheet = true },
                                shape = RoundedCornerShape(AppDimensions.cornerLarge),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                    contentColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                AppIcon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(AppDimensions.iconSmall),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(AppDimensions.paddingMicro))
                                AppTypography.LabelMedium(
                                    text = stringResource(R.string.set_budget),
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                    items(displayBudgets, key = { it.id.toString() + it.name }) { budget ->
                        BudgetCardItem(
                            budget = budget,
                            currencyCode = state.currencyCode,
                            onDelete = { pendingDeleteBudget = budget }
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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AppTypography.TitleMedium(
                                text = stringResource(R.string.nav_savings),
                                fontWeight = FontWeight.Bold
                            )
                            AppButton(
                                onClick = { showSetGoalSheet = true },
                                shape = RoundedCornerShape(AppDimensions.cornerLarge),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFA5B4FC),
                                    contentColor = Color(0xFF1E1B4B)
                                )
                            ) {
                                AppIcon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(AppDimensions.iconSmall),
                                    tint = Color(0xFF1E1B4B)
                                )
                                Spacer(modifier = Modifier.width(AppDimensions.paddingMicro))
                                AppTypography.LabelMedium(
                                    text = stringResource(R.string.set_goal),
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E1B4B)
                                )
                            }
                        }
                    }
                    items(displayGoals, key = { it.id.toString() + it.title }) { goal ->
                        SavingsGoalCardItem(
                            goal = goal,
                            currencyCode = state.currencyCode,
                            onDepositClick = { selectedGoalForFunds = goal },
                            onDelete = { pendingDeleteGoal = goal }
                        )
                    }

                    // Inline Set Goal Action Card
                    item(key = "set_new_goal_inline_card_landscape") {
                        AppCard(
                            onClick = { showSetGoalSheet = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(AppDimensions.paddingDefault),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(AppDimensions.components.avatar)
                                            .clip(CircleShape)
                                            .background(Color(0xFFA5B4FC).copy(alpha = 0.25f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        AppIcon(
                                            imageVector = Icons.Default.Savings,
                                            contentDescription = null,
                                            tint = Color(0xFFA5B4FC)
                                        )
                                    }
                                    Column {
                                        AppTypography.TitleSmall(
                                            text = stringResource(R.string.set_new_savings_goal),
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(AppDimensions.paddingMicro))
                                        AppTypography.BodySmall(
                                            text = stringResource(R.string.set_new_savings_goal_desc),
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                AppIcon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Set Goal",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppTypography.TitleLarge(
                            text = stringResource(R.string.nav_budgets),
                            fontWeight = FontWeight.Bold
                        )
                        AppButton(
                            onClick = { showSetBudgetSheet = true },
                            shape = RoundedCornerShape(AppDimensions.cornerLarge),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            AppIcon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.size(AppDimensions.iconSmall),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(AppDimensions.paddingMicro))
                            AppTypography.LabelMedium(
                                text = stringResource(R.string.set_budget),
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                items(displayBudgets, key = { it.id.toString() + it.name }) { budget ->
                    BudgetCardItem(
                        budget = budget,
                        currencyCode = state.currencyCode,
                        onDelete = { pendingDeleteBudget = budget }
                    )
                }

                // Section 2: Savings Goals Header
                item(key = "savings_header") {
                    Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppTypography.TitleLarge(
                            text = stringResource(R.string.nav_savings),
                            fontWeight = FontWeight.Bold
                        )
                        AppButton(
                            onClick = { showSetGoalSheet = true },
                            shape = RoundedCornerShape(AppDimensions.cornerLarge),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFA5B4FC),
                                contentColor = Color(0xFF1E1B4B)
                            )
                        ) {
                            AppIcon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.size(AppDimensions.iconSmall),
                                tint = Color(0xFF1E1B4B)
                            )
                            Spacer(modifier = Modifier.width(AppDimensions.paddingMicro))
                            AppTypography.LabelMedium(
                                text = stringResource(R.string.set_goal),
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E1B4B)
                            )
                        }
                    }
                }

                items(displayGoals, key = { it.id.toString() + it.title }) { goal ->
                    SavingsGoalCardItem(
                        goal = goal,
                        currencyCode = state.currencyCode,
                        onDepositClick = { selectedGoalForFunds = goal },
                        onDelete = { pendingDeleteGoal = goal }
                    )
                }

                // Inline Set Goal Action Card on screen
                item(key = "set_new_goal_inline_card_portrait") {
                    AppCard(
                        onClick = { showSetGoalSheet = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(AppDimensions.paddingDefault),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(AppDimensions.components.avatar)
                                        .clip(CircleShape)
                                        .background(Color(0xFFA5B4FC).copy(alpha = 0.25f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    AppIcon(
                                        imageVector = Icons.Default.Savings,
                                        contentDescription = null,
                                        tint = Color(0xFFA5B4FC)
                                    )
                                }
                                Column {
                                    AppTypography.TitleSmall(
                                        text = stringResource(R.string.set_new_savings_goal),
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(AppDimensions.paddingMicro))
                                    AppTypography.BodySmall(
                                        text = stringResource(R.string.set_new_savings_goal_desc),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            AppIcon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Set Goal",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(AppDimensions.components.fab * 1.5f)) }
            }
        }
    }

    if (showQuickActionSheet) {
        AppBottomSheet(
            onDismissRequest = { showQuickActionSheet = false },
            title = stringResource(R.string.budgets_savings_title)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = AppDimensions.paddingLarge),
                verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingDefault)
            ) {
                // Option 1: Set a Budget
                AppCard(
                    onClick = {
                        showQuickActionSheet = false
                        showSetBudgetSheet = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(AppDimensions.paddingDefault),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(AppDimensions.components.buttonHeight)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                AppIcon(
                                    imageVector = Icons.Default.AccountBalanceWallet,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                            Column {
                                AppTypography.TitleMedium(
                                    text = stringResource(R.string.set_budget),
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(AppDimensions.paddingMicro))
                                AppTypography.BodySmall(
                                    text = stringResource(R.string.set_budget_desc),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        AppIcon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Option 2: Set a Savings Goal
                AppCard(
                    onClick = {
                        showQuickActionSheet = false
                        showSetGoalSheet = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(AppDimensions.paddingDefault),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(AppDimensions.components.buttonHeight)
                                    .clip(CircleShape)
                                    .background(Color(0xFFA5B4FC).copy(alpha = 0.25f)),
                                contentAlignment = Alignment.Center
                            ) {
                                AppIcon(
                                    imageVector = Icons.Default.Savings,
                                    contentDescription = null,
                                    tint = Color(0xFFA5B4FC)
                                )
                            }
                            Column {
                                AppTypography.TitleMedium(
                                    text = stringResource(R.string.set_savings_goal),
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(AppDimensions.paddingMicro))
                                AppTypography.BodySmall(
                                    text = stringResource(R.string.set_goal_desc),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        AppIcon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }

    if (showSetBudgetSheet) {
        val quickPresets = listOf(
            Pair("Groceries", "350.00"),
            Pair("Dining Out", "200.00"),
            Pair("Entertainment", "100.00"),
            Pair("Shopping", "150.00"),
            Pair("Utilities", "120.00"),
            Pair("Transport", "80.00")
        )
        val periods = listOf(BudgetPeriodType.WEEKLY, BudgetPeriodType.MONTHLY, BudgetPeriodType.YEARLY)

        AppBottomSheet(
            onDismissRequest = { showSetBudgetSheet = false },
            title = stringResource(R.string.create_budget)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = AppDimensions.paddingLarge),
                verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
            ) {
                // Quick Presets
                Column(verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMicro)) {
                    AppTypography.LabelSmall(
                        text = stringResource(R.string.quick_presets),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)) {
                        items(quickPresets) { (presetName, presetLimit) ->
                            AppFilterChip(
                                selected = budgetName.equals(presetName, ignoreCase = true),
                                onClick = {
                                    budgetName = presetName
                                    if (budgetLimit.isEmpty()) budgetLimit = presetLimit
                                    val cat = state.expenseCategories.find { it.name.contains(presetName, ignoreCase = true) }
                                    budgetSelectedCategoryId = cat?.id
                                },
                                label = { AppTypography.LabelSmall(text = presetName) }
                            )
                        }
                    }
                }

                // Budget Name
                AppOutlinedTextField(
                    value = budgetName,
                    onValueChange = { budgetName = it },
                    label = { AppTypography.LabelMedium(text = stringResource(R.string.budget_name)) },
                    placeholder = { AppTypography.BodyMedium(text = stringResource(R.string.budget_placeholder)) },
                    leadingIcon = {
                        AppIcon(
                            imageVector = Icons.Default.AccountBalanceWallet,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // Spending Limit
                AppOutlinedTextField(
                    value = budgetLimit,
                    onValueChange = { budgetLimit = it },
                    label = { AppTypography.LabelMedium(text = "${stringResource(R.string.spending_limit)} (${state.currencyCode})") },
                    placeholder = { AppTypography.BodyMedium(text = "350.00") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )

                // Budget Period
                Column(verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMicro)) {
                    AppTypography.LabelSmall(
                        text = stringResource(R.string.budget_period),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
                    ) {
                        periods.forEach { p ->
                            AppFilterChip(
                                selected = budgetPeriodType == p,
                                onClick = { budgetPeriodType = p },
                                label = { AppTypography.LabelSmall(text = p.displayName) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // Category Scope
                if (state.expenseCategories.isNotEmpty()) {
                    Column(verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMicro)) {
                        AppTypography.LabelSmall(
                            text = stringResource(R.string.category_scope_optional),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)) {
                            item {
                                AppFilterChip(
                                    selected = budgetSelectedCategoryId == null,
                                    onClick = { budgetSelectedCategoryId = null },
                                    label = { AppTypography.LabelSmall(text = stringResource(R.string.all_spending)) }
                                )
                            }
                            items(state.expenseCategories, key = { it.id }) { cat ->
                                AppFilterChip(
                                    selected = budgetSelectedCategoryId == cat.id,
                                    onClick = {
                                        budgetSelectedCategoryId = cat.id
                                        if (budgetName.isEmpty()) budgetName = cat.name
                                    },
                                    label = { AppTypography.LabelSmall(text = cat.name) }
                                )
                            }
                        }
                    }
                }

                // Alert Threshold Slider
                Column(verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMicro)) {
                    AppTypography.LabelSmall(
                        text = stringResource(R.string.notify_threshold_label, budgetNotifyThreshold),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    AppSlider(
                        value = budgetNotifyThreshold.toFloat(),
                        onValueChange = { budgetNotifyThreshold = it.toInt() },
                        valueRange = 50f..100f,
                        steps = 9
                    )
                }

                // Rollover Switch
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        AppTypography.TitleSmall(text = stringResource(R.string.enable_rollover))
                        AppTypography.BodySmall(
                            text = stringResource(R.string.rollover_desc),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    AppSwitch(
                        checked = budgetRolloverEnabled,
                        onCheckedChange = { budgetRolloverEnabled = it }
                    )
                }

                Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))

                // Save Button
                AppButton(
                    onClick = {
                        val selectedCat = state.expenseCategories.find { it.id == budgetSelectedCategoryId }
                        viewModel.addBudget(
                            name = budgetName.ifEmpty { "General Budget" },
                            amountLimitString = budgetLimit,
                            periodType = budgetPeriodType,
                            notifyThreshold = budgetNotifyThreshold,
                            isRolloverEnabled = budgetRolloverEnabled,
                            category = selectedCat
                        )
                        budgetName = ""
                        budgetLimit = ""
                        budgetSelectedCategoryId = null
                        showSetBudgetSheet = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = budgetLimit.isNotBlank()
                ) {
                    AppTypography.ButtonText(text = stringResource(R.string.save_budget))
                }
            }
        }
    }

    if (showSetGoalSheet) {
        val presetColors = listOf(
            Pair("#4CAF50", "Green"),
            Pair("#2196F3", "Blue"),
            Pair("#9C27B0", "Purple"),
            Pair("#FF9800", "Orange"),
            Pair("#E91E63", "Pink"),
            Pair("#00BCD4", "Cyan")
        )

        AppBottomSheet(
            onDismissRequest = { showSetGoalSheet = false },
            title = stringResource(R.string.new_savings_goal)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = AppDimensions.paddingLarge),
                verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
            ) {
                // Goal Title
                AppOutlinedTextField(
                    value = goalTitle,
                    onValueChange = { goalTitle = it },
                    label = { AppTypography.LabelMedium(text = stringResource(R.string.goal_title)) },
                    placeholder = { AppTypography.BodyMedium(text = stringResource(R.string.savings_goal_placeholder)) },
                    leadingIcon = {
                        AppIcon(
                            imageVector = Icons.Default.Savings,
                            contentDescription = null,
                            tint = Color(0xFFA5B4FC)
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // Target Amount
                AppOutlinedTextField(
                    value = goalTargetAmount,
                    onValueChange = { goalTargetAmount = it },
                    label = { AppTypography.LabelMedium(text = "${stringResource(R.string.target_amount)} (${state.currencyCode})") },
                    placeholder = { AppTypography.BodyMedium(text = "1000.00") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )

                // Target Date Field
                AppCard(
                    onClick = { showGoalDatePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = AppDimensions.paddingDefault, vertical = AppDimensions.paddingMedium),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            AppTypography.LabelSmall(
                                text = stringResource(R.string.target_date),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            AppTypography.BodyMedium(
                                text = goalTargetDate.toIsoDate(),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        AppIcon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // Color Palette Picker
                Column(verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMicro)) {
                    AppTypography.LabelSmall(
                        text = stringResource(R.string.goal_color),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        presetColors.forEach { (hex, _) ->
                            val color = Color(android.graphics.Color.parseColor(hex))
                            val isSelected = goalSelectedColor == hex
                            Box(
                                modifier = Modifier
                                    .size(AppDimensions.components.colorPickerCircle)
                                    .clip(CircleShape)
                                    .background(color)
                                    .clickableDebounced { goalSelectedColor = hex }
                                    .then(
                                        if (isSelected) {
                                            Modifier.border(
                                                BorderStroke(AppDimensions.borders.thick, MaterialTheme.colorScheme.onSurface),
                                                CircleShape
                                            )
                                        } else Modifier
                                    )
                            )
                        }
                    }
                }

                // Optional Initial Deposit
                AppOutlinedTextField(
                    value = goalInitialDeposit,
                    onValueChange = { goalInitialDeposit = it },
                    label = { AppTypography.LabelMedium(text = stringResource(R.string.initial_deposit_optional)) },
                    placeholder = { AppTypography.BodyMedium(text = "0.00") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )

                // Description Note
                AppOutlinedTextField(
                    value = goalDescription,
                    onValueChange = { goalDescription = it },
                    label = { AppTypography.LabelMedium(text = stringResource(R.string.optional_note)) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))

                // Save Goal Button
                AppButton(
                    onClick = {
                        viewModel.addSavingsGoal(
                            title = goalTitle.ifEmpty { "New Goal" },
                            targetAmountString = goalTargetAmount,
                            targetDate = goalTargetDate,
                            colorHex = goalSelectedColor,
                            description = goalDescription.ifBlank { null },
                            initialDepositString = goalInitialDeposit.ifBlank { null }
                        )
                        goalTitle = ""
                        goalTargetAmount = ""
                        goalInitialDeposit = ""
                        goalDescription = ""
                        showSetGoalSheet = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = goalTargetAmount.isNotBlank()
                ) {
                    AppTypography.ButtonText(text = stringResource(R.string.save_goal))
                }
            }
        }
    }

    if (showGoalDatePicker) {
        AppDatePickerDialog(
            initialSelectedDateMillis = goalTargetDate,
            onDateSelected = { pickedDate ->
                pickedDate?.let { goalTargetDate = it }
                showGoalDatePicker = false
            },
            onDismiss = { showGoalDatePicker = false }
        )
    }

    selectedGoalForFunds?.let { goal ->
        var fundsAmount by rememberSaveable { mutableStateOf("") }
        AppAlertDialog(
            onDismissRequest = { selectedGoalForFunds = null },
            title = stringResource(R.string.add_funds_to_goal, goal.title),
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)) {
                    AppTypography.BodyMedium(text = stringResource(R.string.enter_deposit_amount))
                    AppOutlinedTextField(
                        value = fundsAmount,
                        onValueChange = { fundsAmount = it },
                        label = { AppTypography.LabelMedium(text = stringResource(R.string.amount)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButtonText = stringResource(R.string.action_deposit),
            onConfirm = {
                val amt = fundsAmount.toMinorUnits()
                if (amt > 0L) {
                    viewModel.addFundsToGoal(goal.id, amt)
                }
                selectedGoalForFunds = null
            },
            dismissButtonText = stringResource(R.string.action_cancel),
            onDismiss = { selectedGoalForFunds = null }
        )
    }

    pendingDeleteBudget?.let { budget ->
        AppAlertDialog(
            onDismissRequest = { pendingDeleteBudget = null },
            title = stringResource(R.string.confirm_delete_budget),
            text = { AppTypography.BodyMedium(text = stringResource(R.string.delete_budget_warning)) },
            confirmButtonText = stringResource(R.string.action_delete),
            onConfirm = {
                viewModel.deleteBudget(budget)
                pendingDeleteBudget = null
            },
            dismissButtonText = stringResource(R.string.action_cancel),
            onDismiss = { pendingDeleteBudget = null },
            isDestructive = true
        )
    }

    pendingDeleteGoal?.let { goal ->
        AppAlertDialog(
            onDismissRequest = { pendingDeleteGoal = null },
            title = stringResource(R.string.confirm_delete_savings_goal),
            text = { AppTypography.BodyMedium(text = stringResource(R.string.delete_savings_goal_warning, goal.title)) },
            confirmButtonText = stringResource(R.string.action_delete),
            onConfirm = {
                viewModel.deleteSavingsGoal(goal)
                pendingDeleteGoal = null
            },
            dismissButtonText = stringResource(R.string.action_cancel),
            onDismiss = { pendingDeleteGoal = null },
            isDestructive = true
        )
    }
}
