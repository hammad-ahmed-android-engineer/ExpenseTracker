package com.example.expensetracker.presentation.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.expensetracker.R
import com.example.expensetracker.core.common.formatCurrency
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppBadge
import com.example.expensetracker.core.designsystem.components.AppCard
import com.example.expensetracker.core.designsystem.theme.semanticColors
import com.example.expensetracker.presentation.components.charts.ProgressChartBar
import java.util.Locale

@Composable
fun BudgetHealthCard(
    totalSpent: Long,
    totalBudget: Long,
    currencyCode: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = semanticColors
    val pct = if (totalBudget > 0) (totalSpent.toFloat() / totalBudget.toFloat()) * 100f else 0f
    val remaining = totalBudget - totalSpent
    val barColor = when {
        pct > 100f -> colors.expense
        pct >= 80f -> colors.warning
        else -> MaterialTheme.colorScheme.primary
    }

    AppCard(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(AppDimensions.paddingDefault)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppTypography.TitleMedium(text = stringResource(R.string.monthly_budget_utilization))
                AppBadge(
                    text = "${"%.0f".format(Locale.US, pct)}%",
                    containerColor = barColor.copy(alpha = 0.16f),
                    contentColor = barColor
                )
            }
            Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))
            ProgressChartBar(
                title = stringResource(R.string.total_spending),
                currentValue = totalSpent,
                targetValue = totalBudget,
                currencyCode = currencyCode,
                progressColor = barColor
            )
            Spacer(modifier = Modifier.height(AppDimensions.paddingMicro))
            AppTypography.Caption(
                text = if (remaining >= 0) {
                    "${remaining.formatCurrency(currencyCode)} remaining this period"
                } else {
                    "Over budget by ${(-remaining).formatCurrency(currencyCode)}"
                },
                color = if (remaining < 0) colors.expense else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

