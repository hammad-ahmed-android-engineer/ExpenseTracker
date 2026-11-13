package com.example.expensetracker.presentation.savings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.expensetracker.R
import com.example.expensetracker.core.common.formatCurrency
import com.example.expensetracker.core.common.formatDate
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppBadge
import com.example.expensetracker.core.designsystem.components.AppButton
import com.example.expensetracker.core.designsystem.components.AppCard
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppIconButton
import com.example.expensetracker.core.designsystem.theme.semanticColors
import com.example.expensetracker.domain.model.SavingsGoal
import com.example.expensetracker.presentation.components.CategoryIcon
import com.example.expensetracker.presentation.components.charts.ProgressChartBar

@Composable
fun SavingsGoalCardItem(
    goal: SavingsGoal,
    currencyCode: String,
    onDepositClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = semanticColors
    AppCard(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(AppDimensions.paddingDefault)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    CategoryIcon(iconName = goal.iconName, colorHex = goal.colorHex)
                    Spacer(modifier = Modifier.width(AppDimensions.paddingMedium))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AppTypography.TitleMedium(text = goal.title)
                            if (goal.isCompleted) {
                                Spacer(modifier = Modifier.width(AppDimensions.paddingSmall))
                                AppBadge(
                                    text = "Completed",
                                    containerColor = colors.incomeContainer,
                                    contentColor = colors.income
                                )
                            }
                        }
                        AppTypography.Caption(text = "Target: ${goal.targetDate.formatDate()}")
                    }
                }
                AppIconButton(onClick = onDelete) {
                    AppIcon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(AppDimensions.paddingMedium))

            ProgressChartBar(
                title = stringResource(R.string.savings_progress),
                currentValue = goal.currentAmount,
                targetValue = goal.targetAmount,
                currencyCode = currencyCode,
                progressColor = if (goal.isCompleted) colors.income else colors.savings
            )

            val monthlyReq = goal.calculateMonthlyRequiredSavings()
            if (monthlyReq > 0) {
                Spacer(modifier = Modifier.height(AppDimensions.paddingMicro))
                AppTypography.LabelSmall(
                    text = "Need ${monthlyReq.formatCurrency(currencyCode)}/mo to reach goal on time",
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(AppDimensions.paddingMedium))

            AppButton(
                onClick = onDepositClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                AppTypography.ButtonText(text = stringResource(R.string.add_withdraw_savings))
            }
        }
    }
}

