package com.example.expensetracker.presentation.transactions.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.expensetracker.R
import com.example.expensetracker.core.common.formatCurrencyCompact
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppCard
import com.example.expensetracker.core.designsystem.theme.semanticColors

@Composable
fun TransactionTotalsCard(
    totalIncome: Long,
    totalExpense: Long,
    currencyCode: String,
    modifier: Modifier = Modifier
) {
    val colors = semanticColors
    AppCard(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppDimensions.paddingMedium),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                AppTypography.Caption(text = stringResource(R.string.income))
                AppTypography.TitleSmall(
                    text = "+${totalIncome.formatCurrencyCompact(currencyCode)}",
                    color = colors.income,
                    fontWeight = FontWeight.Bold
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                AppTypography.Caption(text = stringResource(R.string.expenses))
                AppTypography.TitleSmall(
                    text = "-${totalExpense.formatCurrencyCompact(currencyCode)}",
                    color = colors.expense,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

