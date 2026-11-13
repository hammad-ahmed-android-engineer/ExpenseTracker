package com.example.expensetracker.presentation.transactions

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
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
import com.example.expensetracker.core.common.formatCurrencyCompact
import com.example.expensetracker.core.common.formatRelativeDate
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppAlertDialog
import com.example.expensetracker.core.designsystem.components.AppCard
import com.example.expensetracker.core.designsystem.components.AppCircularProgressIndicator
import com.example.expensetracker.core.designsystem.components.AppFilterChip
import com.example.expensetracker.core.designsystem.components.AppFloatingActionButton
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppIconButton
import com.example.expensetracker.core.designsystem.components.AppOutlinedTextField
import com.example.expensetracker.core.designsystem.components.AppScaffold
import com.example.expensetracker.core.designsystem.components.AppTopAppBar
import com.example.expensetracker.core.designsystem.theme.semanticColors
import com.example.expensetracker.core.utils.getTimestamp
import com.example.expensetracker.domain.model.Transaction
import com.example.expensetracker.domain.model.TransactionType
import com.example.expensetracker.presentation.components.EmptyStateView
import com.example.expensetracker.presentation.transactions.components.TransactionDetailBottomSheet
import com.example.expensetracker.presentation.transactions.components.TransactionFilterBottomSheet
import com.example.expensetracker.presentation.transactions.components.TransactionListItem

@Composable
fun TransactionsScreen(
    viewModel: TransactionViewModel,
    onNavigateToAddTransaction: () -> Unit,
    onNavigateToEditTransaction: (Long) -> Unit,
    onNavigateToCalendar: () -> Unit,
    onNavigateToCategories: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val colors = semanticColors
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    var searchQuery by rememberSaveable { mutableStateOf("") }
    var showFilterSheet by rememberSaveable { mutableStateOf(false) }
    var selectedDetailTx by remember { mutableStateOf<Transaction?>(null) }
    var pendingDeleteTx by remember { mutableStateOf<Transaction?>(null) }

    val now = System.currentTimeMillis()
    val fallbackTransactions = listOf(
        Transaction(
            id = 101L,
            title = "Salary",
            amount = 435000L,
            type = TransactionType.INCOME,
            categoryId = 17,
            accountId = 1,
            date = getTimestamp("2026-11-10 09:00"),
            note = "Monthly Payroll",
            categoryName = "Salary",
            categoryIcon = "Payments",
            categoryColor = "#2E7D32",
            accountName = "Checking"
        ),
        Transaction(
            id = 102L,
            title = "Freelance Project",
            amount = 85000L,
            type = TransactionType.INCOME,
            categoryId = 18,
            accountId = 1,
            date = getTimestamp("2026-11-08 14:00"),
            note = "Client Work",
            categoryName = "Freelancing",
            categoryIcon = "Work",
            categoryColor = "#1565C0",
            accountName = "Checking"
        ),
        Transaction(
            id = 103L,
            title = "Investment Dividend",
            amount = 15000L,
            type = TransactionType.INCOME,
            categoryId = 20,
            accountId = 3,
            date = getTimestamp("2026-11-04 11:30"),
            note = "Stock dividend",
            categoryName = "Investments",
            categoryIcon = "TrendingUp",
            categoryColor = "#283593",
            accountName = "Savings"
        ),
        Transaction(
            id = 1L,
            title = "Food",
            amount = 4550L,
            type = TransactionType.EXPENSE,
            categoryId = 1,
            accountId = 2,
            date = getTimestamp("2026-10-28 14:30"),
            note = "Oct • Savoir",
            categoryName = "Food",
            categoryIcon = "Restaurant",
            categoryColor = "#FF5722",
            accountName = "Cash"
        ),
        Transaction(
            id = 2L,
            title = "Utilities",
            amount = 4250L,
            type = TransactionType.EXPENSE,
            categoryId = 9,
            accountId = 1,
            date = getTimestamp("2026-11-02 10:15"),
            categoryName = "Utilities",
            categoryIcon = "FlashOn",
            categoryColor = "#FF9800",
            accountName = "Cash"
        ),
        Transaction(
            id = 3L,
            title = "Entertainment",
            amount = 9000L,
            type = TransactionType.EXPENSE,
            categoryId = 5,
            accountId = 1,
            date = getTimestamp("2026-10-19 18:45"),
            note = "Emriction inanitlogs",
            categoryName = "Entertainment",
            categoryIcon = "Movie",
            categoryColor = "#E91E63",
            accountName = "Cash"
        ),
        Transaction(
            id = 4L,
            title = "Transport",
            amount = 4250L,
            type = TransactionType.EXPENSE,
            categoryId = 3,
            accountId = 2,
            date = getTimestamp("2026-11-05 12:20"),
            categoryName = "Transportation",
            categoryIcon = "DirectionsCar",
            categoryColor = "#2196F3",
            accountName = "Cash"
        ),
        Transaction(
            id = 5L,
            title = "Transfers",
            amount = 1050L,
            type = TransactionType.EXPENSE,
            categoryId = 16,
            accountId = 2,
            date = getTimestamp("2026-10-23 16:10"),
            categoryName = "Other Expenses",
            categoryIcon = "SwapHoriz",
            categoryColor = "#6366F1",
            accountName = "Cash"
        ),
        Transaction(
            id = 6L,
            title = "Transfer",
            amount = 30000L,
            type = TransactionType.TRANSFER,
            categoryId = 16,
            accountId = 3,
            date = getTimestamp("2026-11-09 11:30"),
            note = "checking",
            categoryName = "Transfer",
            categoryIcon = "Payments",
            categoryColor = "#10B981",
            accountName = "Savings"
        ),
        Transaction(
            id = 7L,
            title = "Transfer",
            amount = 30000L,
            type = TransactionType.TRANSFER,
            categoryId = 16,
            accountId = 3,
            date = getTimestamp("2026-10-15 20:00"),
            note = "checking",
            categoryName = "Transfer",
            categoryIcon = "SwapHoriz",
            categoryColor = "#F97316",
            accountName = "Savings"
        ),
        Transaction(
            id = 8L,
            title = "Rent",
            amount = 120000L,
            type = TransactionType.EXPENSE,
            categoryId = 8,
            accountId = 1,
            date = getTimestamp("2026-11-12 09:45"),
            categoryName = "Housing & Rent",
            categoryIcon = "Home",
            categoryColor = "#795548",
            accountName = "Bank"
        )
    )

    val isFilterActive = state.filter.type != null ||
            state.filter.categoryId != null ||
            state.filter.accountId != null ||
            state.filter.startDate != null ||
            state.filter.endDate != null ||
            searchQuery.isNotBlank()

    val displayTransactions = when {
        state.transactions.isNotEmpty() -> state.transactions
        isFilterActive && (state.totalIncome > 0L || state.totalExpense > 0L) -> emptyList()
        else -> {
            var list = fallbackTransactions
            if (state.filter.type != null) {
                list = list.filter { it.type == state.filter.type }
            }
            if (searchQuery.isNotBlank()) {
                list = list.filter {
                    it.title.contains(searchQuery, ignoreCase = true) ||
                    (it.note ?: "").contains(searchQuery, ignoreCase = true) ||
                    it.categoryName.contains(searchQuery, ignoreCase = true)
                }
            }
            list
        }
    }

    AppScaffold { padding ->
        if (isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .statusBarsPadding()
                    .padding(horizontal = AppDimensions.paddingDefault),
                horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingDefault)
            ) {
                Column(
                    modifier = Modifier
                        .weight(0.42f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
                ) {
                    AppOutlinedTextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            viewModel.updateSearchQuery(it)
                        },
                        placeholder = { AppTypography.BodyMedium(text = stringResource(R.string.search_transactions_hint)) },
                        leadingIcon = {
                            AppIcon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                AppIconButton(onClick = {
                                    searchQuery = ""
                                    viewModel.updateSearchQuery(null)
                                }) {
                                    AppIcon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = stringResource(R.string.action_clear)
                                    )
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(AppDimensions.cornerPill)
                    )

                    val mainTypes = listOf(
                        TransactionType.INCOME,
                        TransactionType.EXPENSE,
                        TransactionType.TRANSFER
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)) {
                        item {
                            AppFilterChip(
                                selected = state.filter.type == null,
                                onClick = { viewModel.updateTypeFilter(null) },
                                label = { AppTypography.LabelSmall(text = stringResource(R.string.filter_all)) }
                            )
                        }
                        items(mainTypes) { type ->
                            AppFilterChip(
                                selected = state.filter.type == type,
                                onClick = { viewModel.updateTypeFilter(type) },
                                label = { AppTypography.LabelSmall(text = type.displayName) }
                            )
                        }
                    }

                    AppCard(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(AppDimensions.paddingMedium),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                AppTypography.Caption(text = stringResource(R.string.income))
                                AppTypography.TitleSmall(
                                    text = "+${state.totalIncome.formatCurrencyCompact(state.currencyCode)}",
                                    color = colors.income,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                AppTypography.Caption(text = stringResource(R.string.expenses))
                                AppTypography.TitleSmall(
                                    text = "-${state.totalExpense.formatCurrencyCompact(state.currencyCode)}",
                                    color = colors.expense,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Box(modifier = Modifier
                    .weight(0.58f)
                    .fillMaxHeight()) {
                    if (displayTransactions.isEmpty()) {
                        val emptyTitle = when (state.filter.type) {
                            TransactionType.INCOME -> stringResource(R.string.no_income_transactions)
                            TransactionType.EXPENSE -> stringResource(R.string.no_expense_transactions)
                            TransactionType.TRANSFER -> stringResource(R.string.no_transfers)
                            else -> if (searchQuery.isNotBlank()) stringResource(R.string.no_matching_transactions) else stringResource(R.string.no_transactions_yet)
                        }
                        val emptyDesc = when {
                            searchQuery.isNotBlank() -> stringResource(R.string.no_matching_query, searchQuery)
                            state.filter.type != null -> stringResource(R.string.no_type_recorded, state.filter.type?.displayName?.lowercase().orEmpty())
                            else -> stringResource(R.string.start_tracking_desc)
                        }
                        EmptyStateView(
                            title = emptyTitle,
                            description = emptyDesc,
                            actionText = if (isFilterActive) stringResource(R.string.clear_filters) else stringResource(R.string.btn_add_transaction),
                            onActionClick = {
                                if (isFilterActive) {
                                    searchQuery = ""
                                    viewModel.updateSearchQuery(null)
                                    viewModel.clearFilters()
                                } else {
                                    onNavigateToAddTransaction()
                                }
                            },
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(vertical = AppDimensions.paddingSmall),
                            verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
                        ) {
                            items(displayTransactions, key = { it.id }) { tx ->
                                TransactionListItem(
                                    tx = tx,
                                    currencyCode = state.currencyCode,
                                    onClick = { selectedDetailTx = tx },
                                    onEdit = { onNavigateToEditTransaction(tx.id) },
                                    onDelete = { pendingDeleteTx = tx },
                                    onDuplicate = { viewModel.duplicateTransaction(tx) }
                                )
                            }
                        }
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .statusBarsPadding()
            ) {
                Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))
                AppOutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        viewModel.updateSearchQuery(it)
                    },
                    placeholder = { AppTypography.BodyMedium(text = stringResource(R.string.search_transactions_hint)) },
                    leadingIcon = {
                        AppIcon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            AppIconButton(onClick = {
                                searchQuery = ""
                                viewModel.updateSearchQuery(null)
                            }) {
                                AppIcon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = stringResource(R.string.action_clear)
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = AppDimensions.paddingDefault,
                            vertical = AppDimensions.paddingSmall
                        ),
                    shape = RoundedCornerShape(AppDimensions.cornerPill)
                )

                val mainTypes = listOf(
                    TransactionType.INCOME,
                    TransactionType.EXPENSE,
                    TransactionType.TRANSFER
                )
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = AppDimensions.paddingDefault),
                    horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
                ) {
                    item {
                        AppFilterChip(
                            selected = state.filter.type == null,
                            onClick = { viewModel.updateTypeFilter(null) },
                            label = { AppTypography.LabelMedium(text = stringResource(R.string.filter_all)) }
                        )
                    }
                    items(mainTypes) { type ->
                        AppFilterChip(
                            selected = state.filter.type == type,
                            onClick = { viewModel.updateTypeFilter(type) },
                            label = { AppTypography.LabelMedium(text = type.displayName) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))

                if (displayTransactions.isEmpty()) {
                    val emptyTitle = when (state.filter.type) {
                        TransactionType.INCOME -> stringResource(R.string.no_income_transactions)
                        TransactionType.EXPENSE -> stringResource(R.string.no_expense_transactions)
                        TransactionType.TRANSFER -> stringResource(R.string.no_transfers)
                        else -> if (searchQuery.isNotBlank()) stringResource(R.string.no_matching_transactions) else stringResource(R.string.no_transactions_yet)
                    }
                    val emptyDesc = when {
                        searchQuery.isNotBlank() -> stringResource(R.string.no_matching_query, searchQuery)
                        state.filter.type != null -> stringResource(R.string.no_type_recorded, state.filter.type?.displayName?.lowercase().orEmpty())
                        else -> stringResource(R.string.start_tracking_desc)
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        EmptyStateView(
                            title = emptyTitle,
                            description = emptyDesc,
                            actionText = if (isFilterActive) stringResource(R.string.clear_filters) else stringResource(R.string.btn_add_transaction),
                            onActionClick = {
                                if (isFilterActive) {
                                    searchQuery = ""
                                    viewModel.updateSearchQuery(null)
                                    viewModel.clearFilters()
                                } else {
                                    onNavigateToAddTransaction()
                                }
                            }
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            horizontal = AppDimensions.paddingDefault,
                            vertical = AppDimensions.paddingSmall
                        ),
                        verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
                    ) {
                        items(displayTransactions, key = { it.id }) { tx ->
                            TransactionListItem(
                                tx = tx,
                                currencyCode = state.currencyCode,
                                onClick = { selectedDetailTx = tx },
                                onEdit = { onNavigateToEditTransaction(tx.id) },
                                onDelete = { pendingDeleteTx = tx },
                                onDuplicate = { viewModel.duplicateTransaction(tx) }
                            )
                        }
                        item { Spacer(modifier = Modifier.height(AppDimensions.paddingLarge)) }
                    }
                }
            }
        }
    }

    if (showFilterSheet) {
        TransactionFilterBottomSheet(
            filter = state.filter,
            accounts = state.accounts,
            categories = state.categories,
            onAccountSelect = { viewModel.updateAccountFilter(it) },
            onCategorySelect = { viewModel.updateCategoryFilter(it) },
            onPaymentMethodSelect = { viewModel.updatePaymentMethodFilter(it) },
            onSortOrderSelect = { viewModel.updateSortOrder(it) },
            onClearFilters = { viewModel.clearFilters() },
            onDismiss = { showFilterSheet = false }
        )
    }

    selectedDetailTx?.let { tx ->
        TransactionDetailBottomSheet(
            transaction = tx,
            currencyCode = state.currencyCode,
            onEdit = {
                selectedDetailTx = null
                onNavigateToEditTransaction(tx.id)
            },
            onDuplicate = {
                viewModel.duplicateTransaction(tx)
                selectedDetailTx = null
            },
            onDelete = {
                selectedDetailTx = null
                pendingDeleteTx = tx
            },
            onDismiss = { selectedDetailTx = null }
        )
    }

    pendingDeleteTx?.let { tx ->
        AppAlertDialog(
            onDismissRequest = { pendingDeleteTx = null },
            title = stringResource(R.string.confirm_delete_transaction),
            text = { AppTypography.BodyMedium(text = stringResource(R.string.delete_transaction_warning, tx.title)) },
            confirmButtonText = stringResource(R.string.action_delete),
            onConfirm = {
                viewModel.deleteTransaction(tx)
                pendingDeleteTx = null
            },
            dismissButtonText = stringResource(R.string.action_cancel),
            onDismiss = { pendingDeleteTx = null },
            isDestructive = true
        )
    }
}
