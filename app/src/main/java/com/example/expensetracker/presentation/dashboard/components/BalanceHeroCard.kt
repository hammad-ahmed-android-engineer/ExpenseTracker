package com.example.expensetracker.presentation.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.expensetracker.R
import com.example.expensetracker.core.common.formatCurrency
import com.example.expensetracker.core.common.formatCurrencyCompact
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppBadge
import com.example.expensetracker.core.designsystem.components.AppCard
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppSurface
import com.example.expensetracker.core.designsystem.theme.semanticColors
import java.util.Locale
import kotlin.math.abs

@Composable
fun BalanceHeroCard(
    totalBalance: Long,
    monthlyIncome: Long,
    monthlyExpense: Long,
    netSavings: Long,
    expenseDeltaPct: Float,
    currencyCode: String,
    compact: Boolean = false,
    modifier: Modifier = Modifier
) {
    val colors = semanticColors
    val primary = MaterialTheme.colorScheme.primary
    val primaryContainer = MaterialTheme.colorScheme.primaryContainer
    val onPrimaryContainer = MaterialTheme.colorScheme.onPrimaryContainer

    AppCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppDimensions.cornerExtraLarge),
        colors = CardDefaults.cardColors(containerColor = primaryContainer),
        border = null
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            primaryContainer,
                            primary.copy(alpha = 0.22f)
                        )
                    )
                )
                .padding(
                    if (compact) AppDimensions.paddingMedium else AppDimensions.paddingLarge
                )
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppTypography.LabelMedium(
                        text = stringResource(R.string.total_combined_balance),
                        color = onPrimaryContainer.copy(alpha = 0.78f)
                    )
                    if (expenseDeltaPct != 0f) {
                        val isLower = expenseDeltaPct < 0f
                        val label = "${if (isLower) "▼" else "▲"} ${"%.0f".format(Locale.US, abs(expenseDeltaPct))}% vs last mo"
                        AppBadge(
                            text = label,
                            containerColor = if (isLower) colors.incomeContainer else colors.expenseContainer,
                            contentColor = if (isLower) colors.income else colors.expense
                        )
                    }
                }

                Spacer(modifier = Modifier.height(AppDimensions.paddingMicro))

                AppTypography.DisplayLarge(
                    text = totalBalance.formatCurrency(currencyCode),
                    color = onPrimaryContainer,
                    maxLines = 1
                )

                Spacer(
                    modifier = Modifier.height(
                        if (compact) AppDimensions.paddingMedium else AppDimensions.paddingLarge
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
                ) {
                    AppSurface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(AppDimensions.cornerMedium),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.72f),
                        border = null
                    ) {
                        Row(
                            modifier = Modifier.padding(AppDimensions.paddingSmall),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(AppDimensions.icons.large)
                                    .clip(CircleShape)
                                    .background(colors.incomeContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                AppIcon(
                                    imageVector = Icons.Default.ArrowDownward,
                                    contentDescription = null,
                                    tint = colors.income,
                                    modifier = Modifier.size(AppDimensions.icons.small)
                                )
                            }
                            Spacer(modifier = Modifier.width(AppDimensions.paddingSmall))
                            Column {
                                AppTypography.Caption(text = stringResource(R.string.income))
                                AppTypography.TitleSmall(
                                    text = monthlyIncome.formatCurrencyCompact(currencyCode),
                                    fontWeight = FontWeight.Bold,
                                    color = colors.income,
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    AppSurface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(AppDimensions.cornerMedium),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.72f),
                        border = null
                    ) {
                        Row(
                            modifier = Modifier.padding(AppDimensions.paddingSmall),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(AppDimensions.icons.large)
                                    .clip(CircleShape)
                                    .background(colors.expenseContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                AppIcon(
                                    imageVector = Icons.Default.ArrowUpward,
                                    contentDescription = null,
                                    tint = colors.expense,
                                    modifier = Modifier.size(AppDimensions.icons.small)
                                )
                            }
                            Spacer(modifier = Modifier.width(AppDimensions.paddingSmall))
                            Column {
                                AppTypography.Caption(text = stringResource(R.string.expenses))
                                AppTypography.TitleSmall(
                                    text = monthlyExpense.formatCurrencyCompact(currencyCode),
                                    fontWeight = FontWeight.Bold,
                                    color = colors.expense,
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    AppSurface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(AppDimensions.cornerMedium),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.72f),
                        border = null
                    ) {
                        Row(
                            modifier = Modifier.padding(AppDimensions.paddingSmall),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(AppDimensions.icons.large)
                                    .clip(CircleShape)
                                    .background(primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                AppIcon(
                                    imageVector = Icons.Default.AccountBalanceWallet,
                                    contentDescription = null,
                                    tint = primary,
                                    modifier = Modifier.size(AppDimensions.icons.small)
                                )
                            }
                            Spacer(modifier = Modifier.width(AppDimensions.paddingSmall))
                            Column {
                                AppTypography.Caption(text = stringResource(R.string.net_savings))
                                AppTypography.TitleSmall(
                                    text = netSavings.formatCurrencyCompact(currencyCode),
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

