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
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.mutableStateOf
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
import com.example.expensetracker.core.designsystem.components.AppScaffold
import com.example.expensetracker.presentation.transactions.components.TransactionDetailBottomSheet
import com.example.expensetracker.presentation.transactions.components.TransactionFilterBottomSheet
import com.example.expensetracker.presentation.transactions.components.TransactionListItem
import com.example.expensetracker.presentation.transactions.components.TransactionSearchAndFiltersBar
import com.example.expensetracker.presentation.transactions.components.TransactionTotalsCard
import com.example.expensetracker.presentation.transactions.components.TransactionsEmptyState

@Composable
fun TransactionsScreen(
    viewModel: TransactionViewModel,
    onNavigateToAddTransaction: () -> Unit,
    onNavigateToEditTransaction: (Long) -> Unit,
    onNavigateToCalendar: () -> Unit,
    onNavigateToCategories: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    var searchQuery by rememberSaveable { mutableStateOf("") }
    var showFilterSheet by rememberSaveable { mutableStateOf(false) }
    var selectedDetailTxId by rememberSaveable { mutableStateOf<Long?>(null) }
    var pendingDeleteTxId by rememberSaveable { mutableStateOf<Long?>(null) }
    val selectedDetailTx = selectedDetailTxId?.let { id -> state.transactions.find { it.id == id } }
    val pendingDeleteTx = pendingDeleteTxId?.let { id -> state.transactions.find { it.id == id } }

    val isFilterActive = state.filter.type != null ||
            state.filter.categoryId != null ||
            state.filter.accountId != null ||
            state.filter.startDate != null ||
            state.filter.endDate != null ||
            searchQuery.isNotBlank()

    val displayTransactions = state.transactions

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
                    TransactionSearchAndFiltersBar(
                        searchQuery = searchQuery,
                        selectedType = state.filter.type,
                        onSearchQueryChange = {
                            searchQuery = it
                            viewModel.updateSearchQuery(it)
                        },
                        onClearSearch = {
                            searchQuery = ""
                            viewModel.updateSearchQuery(null)
                        },
                        onTypeSelect = { viewModel.updateTypeFilter(it) }
                    )

                    TransactionTotalsCard(
                        totalIncome = state.totalIncome,
                        totalExpense = state.totalExpense,
                        currencyCode = state.currencyCode
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(0.58f)
                        .fillMaxHeight()
                ) {
                    if (displayTransactions.isEmpty()) {
                        TransactionsEmptyState(
                            filterType = state.filter.type,
                            searchQuery = searchQuery,
                            isFilterActive = isFilterActive,
                            onClearFilters = {
                                searchQuery = ""
                                viewModel.updateSearchQuery(null)
                                viewModel.clearFilters()
                            },
                            onAddTransaction = onNavigateToAddTransaction,
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
                                    onClick = { selectedDetailTxId = tx.id },
                                    onEdit = { onNavigateToEditTransaction(tx.id) },
                                    onDelete = { pendingDeleteTxId = tx.id },
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
                TransactionSearchAndFiltersBar(
                    searchQuery = searchQuery,
                    selectedType = state.filter.type,
                    onSearchQueryChange = {
                        searchQuery = it
                        viewModel.updateSearchQuery(it)
                    },
                    onClearSearch = {
                        searchQuery = ""
                        viewModel.updateSearchQuery(null)
                    },
                    onTypeSelect = { viewModel.updateTypeFilter(it) },
                    searchModifier = Modifier.padding(
                        horizontal = AppDimensions.paddingDefault,
                        vertical = AppDimensions.paddingSmall
                    ),
                    chipsContentPadding = PaddingValues(horizontal = AppDimensions.paddingDefault)
                )

                Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))

                if (displayTransactions.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        TransactionsEmptyState(
                            filterType = state.filter.type,
                            searchQuery = searchQuery,
                            isFilterActive = isFilterActive,
                            onClearFilters = {
                                searchQuery = ""
                                viewModel.updateSearchQuery(null)
                                viewModel.clearFilters()
                            },
                            onAddTransaction = onNavigateToAddTransaction
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
                                onClick = { selectedDetailTxId = tx.id },
                                onEdit = { onNavigateToEditTransaction(tx.id) },
                                onDelete = { pendingDeleteTxId = tx.id },
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
                selectedDetailTxId = null
                onNavigateToEditTransaction(tx.id)
            },
            onDuplicate = {
                viewModel.duplicateTransaction(tx)
                selectedDetailTxId = null
            },
            onDelete = {
                selectedDetailTxId = null
                pendingDeleteTxId = tx.id
            },
            onDismiss = { selectedDetailTxId = null }
        )
    }

    pendingDeleteTx?.let { tx ->
        AppAlertDialog(
            onDismissRequest = { pendingDeleteTxId = null },
            title = stringResource(R.string.confirm_delete_transaction),
            text = { AppTypography.BodyMedium(text = stringResource(R.string.delete_transaction_warning, tx.title)) },
            confirmButtonText = stringResource(R.string.action_delete),
            onConfirm = {
                viewModel.deleteTransaction(tx)
                pendingDeleteTxId = null
            },
            dismissButtonText = stringResource(R.string.action_cancel),
            onDismiss = { pendingDeleteTxId = null },
            isDestructive = true
        )
    }
}
