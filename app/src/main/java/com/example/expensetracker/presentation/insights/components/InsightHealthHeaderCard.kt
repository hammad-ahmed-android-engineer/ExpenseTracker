package com.example.expensetracker.presentation.insights.components

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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppCard
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppSurface
import com.example.expensetracker.domain.model.insight.FinancialInsight
import com.example.expensetracker.domain.model.insight.InsightType

@Composable
fun InsightHealthHeaderCard(
    insights: List<FinancialInsight>,
    modifier: Modifier = Modifier
) {
    val positiveCount = insights.count { it.type == InsightType.POSITIVE }
    val warningCount = insights.count { it.type == InsightType.WARNING }
    val infoCount = insights.count { it.type == InsightType.INFO }

    val statusText = when {
        warningCount == 0 && positiveCount > 0 -> "Strong Financial Discipline"
        warningCount > 0 -> "Attention Recommended"
        else -> "Steady Financial Pulse"
    }

    val statusSubtitle = when {
        warningCount == 0 && positiveCount > 0 -> "Your spending is well within targets and healthy margins."
        warningCount > 0 -> "$warningCount area${if (warningCount > 1) "s require" else " requires"} attention to keep spending in control."
        else -> "Track daily expenses to unlock deeper predictive intelligence."
    }

    val statusColor = when {
        warningCount == 0 && positiveCount > 0 -> Color(0xFF34D399)
        warningCount > 0 -> Color(0xFFFBBF24)
        else -> MaterialTheme.colorScheme.primary
    }

    AppCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(AppDimensions.paddingDefault),
            verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
            ) {
                AppSurface(
                    shape = RoundedCornerShape(AppDimensions.cornerMedium),
                    color = statusColor.copy(alpha = 0.15f),
                    modifier = Modifier.size(AppDimensions.components.buttonSmallHeight)
                ) {
                    Column(
                        modifier = Modifier.padding(AppDimensions.paddingSmall),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AppIcon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = statusColor,
                            modifier = Modifier.size(AppDimensions.icons.medium)
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    AppTypography.TitleMedium(
                        text = statusText,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(AppDimensions.paddingMicro))
                    AppTypography.Caption(
                        text = statusSubtitle,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
            ) {
                InsightMetricBadge(
                    label = "Positive",
                    count = positiveCount,
                    icon = Icons.Default.CheckCircle,
                    color = Color(0xFF34D399),
                    modifier = Modifier.weight(1f)
                )
                InsightMetricBadge(
                    label = "Warnings",
                    count = warningCount,
                    icon = Icons.Default.Warning,
                    color = Color(0xFFFBBF24),
                    modifier = Modifier.weight(1f)
                )
                InsightMetricBadge(
                    label = "Info",
                    count = infoCount,
                    icon = Icons.Default.Info,
                    color = Color(0xFF38BDF8),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun InsightMetricBadge(
    label: String,
    count: Int,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    AppSurface(
        shape = RoundedCornerShape(AppDimensions.cornerMedium),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = AppDimensions.paddingSmall,
                vertical = AppDimensions.paddingSmall
            ),
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
                text = "$count $label",
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
