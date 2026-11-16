package com.example.expensetracker.presentation.dashboard.components

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
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.expensetracker.R
import com.example.expensetracker.core.common.formatCurrency
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppCard
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppSurface
import com.example.expensetracker.core.designsystem.theme.semanticColors

@Composable
fun BalanceHeroCard(
    totalBalance: Long,
    monthlyIncome: Long,
    monthlyExpense: Long,
    netSavings: Long,
    expenseDeltaPct: Float,
    currencyCode: String,
    modifier: Modifier = Modifier
) {
    val colors = semanticColors
    val netWorthColor = Color(0xFF2C3258)
    val cardTextColor = Color.White

    AppCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppDimensions.cornerExtraLarge),
        colors = CardDefaults.cardColors(containerColor = netWorthColor),
        border = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppDimensions.paddingLarge)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppTypography.LabelMedium(
                    text = stringResource(R.string.net_worth_card),
                    color = cardTextColor.copy(alpha = 0.85f)
                )
                AppIcon(
                    imageVector = Icons.Default.UnfoldMore,
                    contentDescription = null,
                    tint = cardTextColor.copy(alpha = 0.70f),
                    modifier = Modifier.size(AppDimensions.icons.small)
                )
            }

            Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))

            AppTypography.DisplayLarge(
                text = totalBalance.formatCurrency(currencyCode),
                color = cardTextColor,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))

            AppSurface(
                shape = RoundedCornerShape(AppDimensions.cornerPill),
                color = Color.White.copy(alpha = 0.12f),
                border = null
            ) {
                Row(
                    modifier = Modifier.padding(
                        horizontal = AppDimensions.paddingMedium,
                        vertical = AppDimensions.paddingMicro
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingMicro)
                ) {
                    AppTypography.LabelSmall(
                        text = "▲ ${totalBalance.formatCurrency(currencyCode)} • ${stringResource(R.string.daily_trend)}",
                        color = Color(0xFF93C5FD)
                    )
                }
            }
        }
    }
}

