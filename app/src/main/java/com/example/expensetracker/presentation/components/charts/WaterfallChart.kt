package com.example.expensetracker.presentation.components.charts

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import com.example.expensetracker.core.common.formatCurrency
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppSurface
import com.example.expensetracker.core.designsystem.theme.semanticColors
import kotlin.math.abs

@Composable
fun WaterfallChart(
    items: List<WaterfallItem>,
    currencyCode: String = "USD",
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(AppDimensions.spacing.extraLarge * 7)
) {
    if (items.isEmpty()) return

    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    val progress = remember { Animatable(0f) }

    LaunchedEffect(items) {
        progress.snapTo(0f)
        progress.animateTo(1f, animationSpec = tween(durationMillis = 750))
    }

    val (minCum, maxCum) = remember(items) {
        var cum = 0L
        var minV = 0L
        var maxV = 100L
        items.forEach {
            cum += if (it.isPositive) it.amount else -it.amount
            if (cum < minV) minV = cum
            if (cum > maxV) maxV = cum
        }
        minV to (maxV - minV).coerceAtLeast(100L)
    }

    val colors = semanticColors
    val incomeColor = colors.income
    val expenseColor = colors.expense
    val zeroLineColor = MaterialTheme.colorScheme.outlineVariant

    Column(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .pointerInput(items) {
                    detectTapGestures { offset ->
                        val itemWidth = size.width / items.size.coerceAtLeast(1)
                        val idx = (offset.x / itemWidth).toInt().coerceIn(0, items.size - 1)
                        selectedIndex = if (selectedIndex == idx) null else idx
                    }
                }
        ) {
            val width = size.width
            val height = size.height - 16f
            val itemWidth = width / items.size.coerceAtLeast(1)
            val barWidth = (itemWidth * 0.55f).coerceAtMost(48f)

            val zeroY = height - (((0L - minCum).toFloat() / maxCum.toFloat()) * height)
            drawLine(
                color = zeroLineColor,
                start = Offset(0f, zeroY),
                end = Offset(width, zeroY),
                strokeWidth = 2f
            )

            var cumulative = 0L
            items.forEachIndexed { index, item ->
                val prevCum = cumulative
                cumulative += if (item.isPositive) item.amount else -item.amount

                val yPrev = height - (((prevCum - minCum).toFloat() / maxCum.toFloat()) * height * progress.value)
                val yCum = height - (((cumulative - minCum).toFloat() / maxCum.toFloat()) * height * progress.value)

                val barTop = minOf(yPrev, yCum)
                val barHeight = abs(yPrev - yCum).coerceAtLeast(6f)

                val isSelected = selectedIndex == null || selectedIndex == index
                val color = (if (item.isPositive) incomeColor else expenseColor)
                    .copy(alpha = if (isSelected) 1f else 0.4f)
                val left = index * itemWidth + (itemWidth - barWidth) / 2f

                drawRoundRect(
                    color = color,
                    topLeft = Offset(left, barTop),
                    size = Size(barWidth, barHeight),
                    cornerRadius = CornerRadius(10f, 10f)
                )
            }
        }

        Spacer(modifier = Modifier.height(AppDimensions.paddingMicro))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            items.forEach { item ->
                AppTypography.Caption(text = item.label, maxLines = 1)
            }
        }

        val selected = selectedIndex?.let { items.getOrNull(it) }
        if (selected != null) {
            Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))
            AppSurface(
                shape = RoundedCornerShape(AppDimensions.cornerPill),
                color = MaterialTheme.colorScheme.surfaceContainerHigh
            ) {
                val sign = if (selected.isPositive) "+" else "-"
                AppTypography.LabelSmall(
                    text = "${selected.label}: $sign${selected.amount.formatCurrency(currencyCode)}",
                    color = if (selected.isPositive) incomeColor else expenseColor,
                    modifier = Modifier.padding(
                        horizontal = AppDimensions.paddingMedium,
                        vertical = AppDimensions.paddingMicro
                    )
                )
            }
        }
    }
}
