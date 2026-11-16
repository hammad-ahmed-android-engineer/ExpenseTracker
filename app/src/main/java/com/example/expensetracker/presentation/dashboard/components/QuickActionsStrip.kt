package com.example.expensetracker.presentation.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = AppDimensions.paddingSmall),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        QuickActionTile(
            label = "Income",
            symbolText = "$",
            onClick = onAddTransaction,
            containerColor = Color(0xFF10B981),
            iconTint = Color.White
        )
        QuickActionTile(
            label = "Expense",
            symbolText = "✕",
            onClick = onAddTransaction,
            containerColor = Color(0xFFEF4444),
            iconTint = Color.White
        )
        QuickActionTile(
            label = "Transfer",
            icon = Icons.Default.SwapHoriz,
            onClick = onAccounts,
            containerColor = Color(0xFF6366F1),
            iconTint = Color.White
        )
        QuickActionTile(
            label = "Goals",
            icon = Icons.Default.TrackChanges,
            onClick = onSavings,
            containerColor = Color(0xFF2E3048),
            iconTint = Color.White
        )
    }
}

