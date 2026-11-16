package com.example.expensetracker.presentation.reports.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.expensetracker.R
import com.example.expensetracker.core.common.formatCurrencyCompact
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppBadge
import com.example.expensetracker.core.designsystem.components.AppCard
import com.example.expensetracker.core.designsystem.theme.semanticColors
import java.util.Locale

@Composable
fun ReportsKpiCard(
    totalIncome: Long,
    totalExpense: Long,
    netSavings: Long,
    currencyCode: String,
    modifier: Modifier = Modifier
) {
    val colors = semanticColors
    val savingsRate = if (totalIncome > 0) {
        ((netSavings.toFloat() / totalIncome.toFloat()) * 100f).coerceIn(0f, 100f)
    } else 0f

    AppCard(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        border = null
    ) {
        Column(modifier = Modifier.padding(AppDimensions.paddingDefault)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppTypography.TitleSmall(
                    text = stringResource(R.string.period_summary),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                AppBadge(
                    text = "Savings ${"%.0f".format(Locale.US, savingsRate)}%",
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                    contentColor = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(AppDimensions.paddingMedium))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    AppTypography.Caption(
                        text = stringResource(R.string.total_income),
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.75f)
                    )
                    AppTypography.TitleMedium(
                        text = totalIncome.formatCurrencyCompact(currencyCode),
                        fontWeight = FontWeight.Bold,
                        color = colors.income
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AppTypography.Caption(
                        text = stringResource(R.string.total_expenses_title),
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.75f)
                    )
                    AppTypography.TitleMedium(
                        text = totalExpense.formatCurrencyCompact(currencyCode),
                        fontWeight = FontWeight.Bold,
                        color = colors.expense
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    AppTypography.Caption(
                        text = stringResource(R.string.net_savings),
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.75f)
                    )
                    AppTypography.TitleMedium(
                        text = netSavings.formatCurrencyCompact(currencyCode),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

