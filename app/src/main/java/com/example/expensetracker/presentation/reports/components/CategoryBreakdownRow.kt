package com.example.expensetracker.presentation.reports.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.expensetracker.core.common.formatCurrency
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppCard
import com.example.expensetracker.core.designsystem.components.AppProgressBar
import com.example.expensetracker.core.utils.toComposeColor
import com.example.expensetracker.domain.model.CategorySpendingSummary
import com.example.expensetracker.presentation.components.CategoryIcon
import java.util.Locale

@Composable
fun CategoryBreakdownRow(
    summary: CategorySpendingSummary,
    currencyCode: String,
    modifier: Modifier = Modifier
) {
    val accent = summary.categoryColor.toComposeColor()

    AppCard(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(AppDimensions.paddingMedium)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CategoryIcon(iconName = summary.categoryIcon, colorHex = summary.categoryColor)
                Spacer(modifier = Modifier.width(AppDimensions.paddingMedium))
                Column(modifier = Modifier.weight(1f)) {
                    AppTypography.TitleSmall(
                        text = summary.categoryName,
                        fontWeight = FontWeight.SemiBold
                    )
                    AppTypography.Caption(
                        text = "${"%.1f".format(Locale.US, summary.percentage)}% of expenses"
                    )
                }
                AppTypography.TitleSmall(
                    text = summary.totalAmount.formatCurrency(currencyCode),
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))
            AppProgressBar(
                progress = (summary.percentage / 100f).coerceIn(0f, 1f),
                color = accent,
                height = AppDimensions.paddingMicro * 2
            )
        }
    }
}

