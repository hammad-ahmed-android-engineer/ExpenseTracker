package com.example.expensetracker.presentation.calendar.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.expensetracker.core.common.formatCurrencyCompact
import com.example.expensetracker.core.common.formatDate
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppBadge
import com.example.expensetracker.core.designsystem.theme.semanticColors

@Composable
fun CalendarDaySummaryHeader(
    selectedDateMillis: Long,
    dayIncome: Long,
    dayExpense: Long,
    currencyCode: String,
    modifier: Modifier = Modifier
) {
    val colors = semanticColors
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppTypography.TitleMedium(text = selectedDateMillis.formatDate("MMM dd, yyyy"))
        Row(horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingMicro)) {
            if (dayIncome > 0) {
                AppBadge(
                    text = "+${dayIncome.formatCurrencyCompact(currencyCode)}",
                    containerColor = colors.incomeContainer,
                    contentColor = colors.income
                )
            }
            if (dayExpense > 0) {
                AppBadge(
                    text = "-${dayExpense.formatCurrencyCompact(currencyCode)}",
                    containerColor = colors.expenseContainer,
                    contentColor = colors.expense
                )
            }
        }
    }
}

