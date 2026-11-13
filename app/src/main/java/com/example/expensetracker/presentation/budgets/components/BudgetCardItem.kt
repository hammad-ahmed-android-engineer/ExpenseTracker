package com.example.expensetracker.presentation.budgets.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppIconButton
import com.example.expensetracker.core.designsystem.theme.semanticColors
import com.example.expensetracker.domain.model.Budget
import com.example.expensetracker.presentation.components.charts.ProgressChartBar

@Composable
fun BudgetCardItem(
    budget: Budget,
    currencyCode: String,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = semanticColors
    val progressColor = when {
        budget.isExceeded -> colors.expense
        budget.isWarning -> colors.warning
        else -> MaterialTheme.colorScheme.primary
    }

    AppCard(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(AppDimensions.paddingDefault)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    AppTypography.TitleMedium(text = budget.name)
                    AppTypography.Caption(
                        text = "${budget.periodType.displayName} • Alert at ${budget.notifyThreshold}%" +
                            if (budget.isRolloverEnabled) " • Rollover" else ""
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (budget.isExceeded) {
                        AppBadge(
                            text = stringResource(R.string.exceeded),
                            containerColor = colors.expenseContainer,
                            contentColor = colors.expense
                        )
                    } else if (budget.isWarning) {
                        AppBadge(
                            text = stringResource(R.string.near_limit),
                            containerColor = colors.warningContainer,
                            contentColor = colors.warning
                        )
                    }
                    AppIconButton(onClick = onDelete) {
                        AppIcon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(AppDimensions.paddingMedium))

            ProgressChartBar(
                title = stringResource(R.string.spent_vs_limit),
                currentValue = budget.spentAmount,
                targetValue = budget.amountLimit,
                currencyCode = currencyCode,
                progressColor = progressColor
            )

            Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))

            AppTypography.LabelMedium(
                text = "${stringResource(R.string.remaining)} ${budget.remainingAmount.formatCurrency(currencyCode)}",
                color = if (budget.remainingAmount < 0) colors.expense else MaterialTheme.colorScheme.primary
            )
        }
    }
}

