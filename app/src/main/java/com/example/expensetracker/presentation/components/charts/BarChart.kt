package com.example.expensetracker.presentation.components.charts

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import com.example.expensetracker.core.common.formatCurrencyCompact
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppSurface
import com.example.expensetracker.core.designsystem.theme.semanticColors

@Composable
fun GroupedBarChart(
    groups: List<BarGroup>,
    currencyCode: String = "USD",
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(AppDimensions.spacing.extraLarge * 8)
) {
    if (groups.isEmpty()) return

    var selectedGroupIndex by remember { mutableStateOf<Int?>(null) }
    val progress = remember { Animatable(0f) }

    LaunchedEffect(groups) {
        progress.snapTo(0f)
        progress.animateTo(1f, animationSpec = tween(durationMillis = 750))
    }

    val maxVal = remember(groups) {
        groups.flatMap { listOf(it.income, it.expense) }.maxOrNull()?.coerceAtLeast(100L) ?: 100L
    }

    val colors = semanticColors
    val incomeColor = colors.income
    val expenseColor = colors.expense
    val gridColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingDefault),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(AppDimensions.icons.micro)
                        .clip(CircleShape)
                        .background(incomeColor)
                )
                Spacer(modifier = Modifier.width(AppDimensions.paddingMicro))
                AppTypography.LabelSmall(text = "Income", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(AppDimensions.icons.micro)
                        .clip(CircleShape)
                        .background(expenseColor)
                )
                Spacer(modifier = Modifier.width(AppDimensions.paddingMicro))
                AppTypography.LabelSmall(text = "Expense", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .pointerInput(groups) {
                    detectTapGestures { offset ->
                        val groupWidth = size.width / groups.size.coerceAtLeast(1)
                        val index = (offset.x / groupWidth).toInt().coerceIn(0, groups.size - 1)
                        selectedGroupIndex = if (selectedGroupIndex == index) null else index
                    }
                }
        ) {
            val width = size.width
            val height = size.height
            val groupWidth = width / groups.size.coerceAtLeast(1)
            val barWidth = (groupWidth * 0.28f).coerceAtMost(28f)
            val gap = (barWidth * 0.25f).coerceAtLeast(4f)

            for (i in 0..3) {
                val y = height * (i / 3f)
                drawLine(
                    color = gridColor,
                    start = Offset(0f, y),
                    end = Offset(width, y),
                    strokeWidth = 1.5f
                )
            }

            groups.forEachIndexed { index, group ->
                val isHighlighted = selectedGroupIndex == null || selectedGroupIndex == index
                val alpha = if (isHighlighted) 1f else 0.35f
                val groupCenter = index * groupWidth + groupWidth / 2f
                val totalPairWidth = barWidth * 2 + gap
                val startX = groupCenter - totalPairWidth / 2f

                val incomeHeight = (group.income.toFloat() / maxVal.toFloat()) * height * progress.value
                val expenseHeight = (group.expense.toFloat() / maxVal.toFloat()) * height * progress.value

                if (incomeHeight > 0f) {
                    drawRoundRect(
                        color = incomeColor.copy(alpha = alpha),
                        topLeft = Offset(startX, height - incomeHeight),
                        size = Size(barWidth, incomeHeight),
                        cornerRadius = CornerRadius(10f, 10f)
                    )
                }

                if (expenseHeight > 0f) {
                    drawRoundRect(
                        color = expenseColor.copy(alpha = alpha),
                        topLeft = Offset(startX + barWidth + gap, height - expenseHeight),
                        size = Size(barWidth, expenseHeight),
                        cornerRadius = CornerRadius(10f, 10f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(AppDimensions.paddingMicro))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val step = (groups.size / 4).coerceAtLeast(1)
            groups.filterIndexed { i, _ -> i % step == 0 || i == groups.lastIndex }.take(5).forEach {
                AppTypography.Caption(text = it.label)
            }
        }

        val selected = selectedGroupIndex?.let { groups.getOrNull(it) }
        if (selected != null) {
            Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))
            AppSurface(
                shape = RoundedCornerShape(AppDimensions.cornerPill),
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(
                        horizontal = AppDimensions.paddingMedium,
                        vertical = AppDimensions.paddingMicro
                    ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppTypography.LabelSmall(text = selected.label, color = MaterialTheme.colorScheme.onSurface)
                    AppTypography.LabelSmall(
                        text = "+${selected.income.formatCurrencyCompact(currencyCode)}",
                        color = incomeColor
                    )
                    AppTypography.LabelSmall(
                        text = "-${selected.expense.formatCurrencyCompact(currencyCode)}",
                        color = expenseColor
                    )
                }
            }
        }
    }
}
