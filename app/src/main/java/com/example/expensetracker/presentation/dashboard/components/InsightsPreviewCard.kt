package com.example.expensetracker.presentation.dashboard.components

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
import androidx.compose.material.icons.filled.ChevronRight
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
fun InsightsPreviewCard(
    insight: FinancialInsight,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = semanticColors
    val tint = when (insight.type) {
        InsightType.POSITIVE -> colors.income
        InsightType.WARNING -> colors.warning
        InsightType.INFO -> MaterialTheme.colorScheme.primary
    }

    AppCard(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppDimensions.paddingDefault),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppIcon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(AppDimensions.icons.large)
            )
            Spacer(modifier = Modifier.width(AppDimensions.paddingMedium))
            Column(modifier = Modifier.weight(1f)) {
                AppTypography.TitleSmall(text = insight.title, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(AppDimensions.paddingMicro))
                AppTypography.BodySmall(
                    text = insight.message,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
            }
            AppIcon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

