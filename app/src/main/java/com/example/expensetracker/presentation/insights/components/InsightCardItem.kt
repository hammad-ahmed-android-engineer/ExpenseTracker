package com.example.expensetracker.presentation.insights.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppCard
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.theme.semanticColors
import com.example.expensetracker.domain.model.FinancialInsight
import com.example.expensetracker.domain.model.InsightType

@Composable
fun InsightCardItem(
    insight: FinancialInsight,
    modifier: Modifier = Modifier
) {
    val colors = semanticColors
    val (bgColor, icon, iconColor) = when (insight.type) {
        InsightType.POSITIVE -> Triple(colors.incomeContainer.copy(alpha = 0.45f), Icons.Default.TrendingDown, colors.income)
        InsightType.WARNING -> Triple(colors.warningContainer.copy(alpha = 0.45f), Icons.Default.Warning, colors.warning)
        InsightType.INFO -> Triple(MaterialTheme.colorScheme.surfaceContainerLow, Icons.Default.AutoAwesome, MaterialTheme.colorScheme.primary)
    }

    AppCard(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Row(
            modifier = Modifier.padding(AppDimensions.paddingDefault),
            verticalAlignment = Alignment.Top
        ) {
            AppIcon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(AppDimensions.icons.large)
            )
            Spacer(modifier = Modifier.width(AppDimensions.paddingMedium))
            Column {
                AppTypography.TitleMedium(
                    text = insight.title,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(AppDimensions.paddingMicro))
                AppTypography.BodyMedium(
                    text = insight.message,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

