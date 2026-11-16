package com.example.expensetracker.presentation.transactions.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.expensetracker.R
import com.example.expensetracker.domain.model.TransactionType
import com.example.expensetracker.presentation.components.EmptyStateView

@Composable
fun TransactionsEmptyState(
    filterType: TransactionType?,
    searchQuery: String,
    isFilterActive: Boolean,
    onClearFilters: () -> Unit,
    onAddTransaction: () -> Unit,
    modifier: Modifier = Modifier
) {
    val emptyTitle = when (filterType) {
        TransactionType.INCOME -> stringResource(R.string.no_income_transactions)
        TransactionType.EXPENSE -> stringResource(R.string.no_expense_transactions)
        TransactionType.TRANSFER -> stringResource(R.string.no_transfers)
        else -> if (searchQuery.isNotBlank()) stringResource(R.string.no_matching_transactions) else stringResource(R.string.no_transactions_yet)
    }
    val emptyDesc = when {
        searchQuery.isNotBlank() -> stringResource(R.string.no_matching_query, searchQuery)
        filterType != null -> stringResource(R.string.no_type_recorded, filterType.displayName.lowercase())
        else -> stringResource(R.string.start_tracking_desc)
    }
    EmptyStateView(
        title = emptyTitle,
        description = emptyDesc,
        actionText = if (isFilterActive) stringResource(R.string.clear_filters) else stringResource(R.string.btn_add_transaction),
        onActionClick = {
            if (isFilterActive) onClearFilters() else onAddTransaction()
        },
        modifier = modifier
    )
}

