package com.example.expensetracker.presentation.components.charts

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.expensetracker.core.common.formatCurrencyCompact
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppProgressBar

@Composable
fun ProgressChartBar(
    title: String,
    currentValue: Long,
    targetValue: Long,
    currencyCode: String = "USD",
    progressColor: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier
) {
    val rawPct = if (targetValue > 0) (currentValue.toFloat() / targetValue.toFloat()) else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = rawPct.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 700),
        label = "progressAnim"
    )

    Column(modifier = modifier.fillMaxWidth().padding(vertical = AppDimensions.paddingMicro)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppTypography.TitleSmall(
                text = title,
                color = MaterialTheme.colorScheme.onSurface
            )
            AppTypography.LabelSmall(
                text = "${currentValue.formatCurrencyCompact(currencyCode)} / ${targetValue.formatCurrencyCompact(currencyCode)}",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))

        AppProgressBar(
            progress = animatedProgress,
            color = progressColor,
            height = AppDimensions.paddingSmall
        )
    }
}
