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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.expensetracker.R
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppCard
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppSurface

@Composable
fun ReportInsightsHighlightCard(
    highestSpendingDayText: String,
    onNavigateToInsights: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppCard(
        onClick = onNavigateToInsights,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(AppDimensions.paddingDefault)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppTypography.TitleMedium(
                    text = stringResource(R.string.nav_insights),
                    fontWeight = FontWeight.SemiBold
                )
                AppIcon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "View Insights",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(AppDimensions.icons.small)
                )
            }
            Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))
            AppSurface(
                shape = RoundedCornerShape(AppDimensions.cornerMedium),
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(AppDimensions.paddingMedium),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
                ) {
                    AppIcon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = Color(0xFFFBBF24),
                        modifier = Modifier.size(AppDimensions.icons.medium)
                    )
                    AppTypography.BodyMedium(
                        text = "${stringResource(R.string.highest_spending_day_prefix)} $highestSpendingDayText.",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
