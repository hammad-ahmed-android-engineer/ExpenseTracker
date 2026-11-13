package com.example.expensetracker.presentation.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.expensetracker.R
import com.example.expensetracker.core.designsystem.AppDimensions

@Composable
fun QuickActionsStrip(
    onAddTransaction: () -> Unit,
    onAccounts: () -> Unit,
    onCategories: () -> Unit,
    onSavings: () -> Unit,
    onRecurring: () -> Unit,
    onCalendar: () -> Unit,
    onInsights: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
    ) {
        item {
            QuickActionTile(
                label = stringResource(R.string.action_add),
                icon = Icons.Default.Add,
                onClick = onAddTransaction,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                iconTint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        item {
            QuickActionTile(
                label = stringResource(R.string.nav_accounts),
                icon = Icons.Default.AccountBalance,
                onClick = onAccounts
            )
        }
        item {
            QuickActionTile(
                label = stringResource(R.string.action_transfer),
                icon = Icons.Default.SwapHoriz,
                onClick = onAccounts
            )
        }
        item {
            QuickActionTile(
                label = stringResource(R.string.nav_savings),
                icon = Icons.Default.Savings,
                onClick = onSavings
            )
        }
        item {
            QuickActionTile(
                label = stringResource(R.string.nav_recurring),
                icon = Icons.Default.Repeat,
                onClick = onRecurring
            )
        }
        item {
            QuickActionTile(
                label = stringResource(R.string.nav_categories),
                icon = Icons.Default.Category,
                onClick = onCategories
            )
        }
        item {
            QuickActionTile(
                label = stringResource(R.string.nav_calendar),
                icon = Icons.Default.CalendarMonth,
                onClick = onCalendar
            )
        }
        item {
            QuickActionTile(
                label = stringResource(R.string.nav_insights),
                icon = Icons.Default.AutoAwesome,
                onClick = onInsights
            )
        }
    }
}

