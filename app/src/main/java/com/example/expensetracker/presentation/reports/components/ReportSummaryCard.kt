package com.example.expensetracker.presentation.reports.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.expensetracker.R
import com.example.expensetracker.core.common.formatCurrency
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppCard
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppSurface

@Composable
fun ReportSummaryCard(
    totalIncome: Long,
    totalExpense: Long,
    netSavings: Long,
    currencyCode: String,
    modifier: Modifier = Modifier
) {
    AppCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(AppDimensions.paddingDefault),
            verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
        ) {
            AppTypography.TitleMedium(
                text = stringResource(R.string.period_financial_summary),
                fontWeight = FontWeight.SemiBold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
            ) {
                SummaryMetricItem(
                    title = stringResource(R.string.income_label),
                    amount = totalIncome.formatCurrency(currencyCode),
                    icon = Icons.Default.ArrowDownward,
                    color = Color(0xFF34D399),
                    modifier = Modifier.weight(1f)
                )

                SummaryMetricItem(
                    title = stringResource(R.string.expense_label),
                    amount = totalExpense.formatCurrency(currencyCode),
                    icon = Icons.Default.ArrowUpward,
                    color = Color(0xFFF87171),
                    modifier = Modifier.weight(1f)
                )

                SummaryMetricItem(
                    title = stringResource(R.string.net_label),
                    amount = netSavings.formatCurrency(currencyCode),
                    icon = Icons.Default.AccountBalanceWallet,
                    color = if (netSavings >= 0) Color(0xFF38BDF8) else Color(0xFFF87171),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun SummaryMetricItem(
    title: String,
    amount: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    AppSurface(
        shape = RoundedCornerShape(AppDimensions.cornerMedium),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(AppDimensions.paddingSmall),
            verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMicro)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingMicro)
            ) {
                AppIcon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(AppDimensions.icons.tiny)
                )
                AppTypography.LabelSmall(
                    text = title,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            AppTypography.BodyMedium(
                text = amount,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
