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
import com.example.expensetracker.domain.model.Transaction
import com.example.expensetracker.domain.model.TransactionType
import com.example.expensetracker.presentation.components.ConfirmationDialog
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

    var searchQuery by remember { mutableStateOf("") }
    var showFilterSheet by remember { mutableStateOf(false) }
    var selectedDetailTx by remember { mutableStateOf<Transaction?>(null) }
    var pendingDeleteTx by remember { mutableStateOf<Transaction?>(null) }

    val grouped = remember(state.transactions) {
        state.transactions.groupBy { it.date.formatRelativeDate() }
    }

    AppScaffold(
        topBar = {
            AppTopAppBar(
                title = { AppTypography.TitleLarge(text = stringResource(R.string.nav_transactions)) },
                actions = {
                    AppIconButton(onClick = onNavigateToCategories) {
                        AppIcon(
                            imageVector = Icons.Default.Category,
                            contentDescription = stringResource(R.string.nav_categories)
                        )
                    }
                    AppIconButton(onClick = onNavigateToCalendar) {
                        AppIcon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = stringResource(R.string.nav_calendar)
                        )
                    }
                    AppIconButton(onClick = { showFilterSheet = true }) {
                        AppIcon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = stringResource(R.string.action_filter)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            AppFloatingActionButton(onClick = onNavigateToAddTransaction) {
                AppIcon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.action_add)
                )
            }
        }
    ) { padding ->
        if (isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
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
                        leadingIcon = { AppIcon(imageVector = Icons.Default.Search, contentDescription = null) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                AppIconButton(onClick = {
                                    searchQuery = ""
                                    viewModel.updateSearchQuery(null)
                                }) {
                                    AppIcon(imageVector = Icons.Default.Clear, contentDescription = stringResource(R.string.action_clear))
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(AppDimensions.cornerPill)
                    )

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)) {
                        item {
                            AppFilterChip(
                                selected = state.filter.type == null,
                                onClick = { viewModel.updateTypeFilter(null) },
                                label = { AppTypography.LabelSmall(text = stringResource(R.string.filter_all)) }
                            )
                        }
                        items(TransactionType.entries) { type ->
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

                Box(modifier = Modifier.weight(0.58f).fillMaxHeight()) {
                    if (state.isLoading) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            AppCircularProgressIndicator()
                        }
                    } else if (state.transactions.isEmpty()) {
                        EmptyStateView(
                            title = stringResource(R.string.no_transactions_title),
                            description = stringResource(R.string.no_transactions_desc),
                            actionText = stringResource(R.string.action_add_transaction),
                            onActionClick = onNavigateToAddTransaction
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(vertical = AppDimensions.paddingSmall),
                            verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
                        ) {
                            grouped.forEach { (dateHeader, txList) ->
                                val dailyTotal = txList.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
                                item(key = "hdr_$dateHeader") {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = AppDimensions.paddingMicro),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        AppTypography.TitleSmall(
                                            text = dateHeader,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        if (dailyTotal > 0) {
                                            AppTypography.Caption(
                                                text = "${stringResource(R.string.spent_prefix)} ${dailyTotal.formatCurrencyCompact(state.currencyCode)}"
                                            )
                                        }
                                    }
                                }
                                items(txList, key = { it.id }) { tx ->
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
                            item { Spacer(modifier = Modifier.height(AppDimensions.components.fab)) }
                        }
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                AppOutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        viewModel.updateSearchQuery(it)
                    },
                    placeholder = { AppTypography.BodyMedium(text = stringResource(R.string.search_transactions_hint)) },
                    leadingIcon = { AppIcon(imageVector = Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            AppIconButton(onClick = {
                                searchQuery = ""
                                viewModel.updateSearchQuery(null)
                            }) {
                                AppIcon(imageVector = Icons.Default.Clear, contentDescription = stringResource(R.string.action_clear))
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppDimensions.paddingDefault, vertical = AppDimensions.paddingSmall),
                    shape = RoundedCornerShape(AppDimensions.cornerPill)
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
                    items(TransactionType.entries) { type ->
                        AppFilterChip(
                            selected = state.filter.type == type,
                            onClick = { viewModel.updateTypeFilter(type) },
                            label = { AppTypography.LabelMedium(text = type.displayName) }
                        )
                    }
                }

                if (state.transactions.isNotEmpty()) {
                    AppCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = AppDimensions.paddingDefault, vertical = AppDimensions.paddingSmall)
                    ) {
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
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                AppTypography.Caption(text = "Count")
                                AppTypography.TitleSmall(
                                    text = "${state.transactions.size}",
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

                if (state.isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        AppCircularProgressIndicator()
                    }
                } else if (state.transactions.isEmpty()) {
                    EmptyStateView(
                        title = stringResource(R.string.no_transactions_title),
                        description = stringResource(R.string.no_transactions_desc),
                        actionText = stringResource(R.string.action_add_transaction),
                        onActionClick = onNavigateToAddTransaction
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            horizontal = AppDimensions.paddingDefault,
                            vertical = AppDimensions.paddingSmall
                        ),
                        verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
                    ) {
                        grouped.forEach { (dateHeader, txList) ->
                            val dailyTotal = txList.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
                            item(key = "hdr_$dateHeader") {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = AppDimensions.paddingMicro),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AppTypography.TitleSmall(
                                        text = dateHeader,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    if (dailyTotal > 0) {
                                        AppTypography.Caption(
                                            text = "${stringResource(R.string.spent_prefix)} ${dailyTotal.formatCurrencyCompact(state.currencyCode)}"
                                        )
                                    }
                                }
                            }
                            items(txList, key = { it.id }) { tx ->
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
                        item { Spacer(modifier = Modifier.height(AppDimensions.components.fab * 1.4f)) }
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
        ConfirmationDialog(
            title = "Delete Transaction",
            message = "Delete \"${tx.title}\"? Account balances will be reverted automatically.",
            onConfirm = {
                viewModel.deleteTransaction(tx)
                pendingDeleteTx = null
            },
            onDismiss = { pendingDeleteTx = null }
        )
    }
}
