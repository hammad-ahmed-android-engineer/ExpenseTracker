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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import com.example.expensetracker.core.common.formatCurrency
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppSurface

@Composable
fun LineChart(
    points: List<LinePoint>,
    currencyCode: String = "USD",
    lineColor: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(AppDimensions.spacing.extraLarge * 7)
) {
    if (points.isEmpty()) return

    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    val progress = remember { Animatable(0f) }

    LaunchedEffect(points) {
        progress.snapTo(0f)
        progress.animateTo(1f, animationSpec = tween(durationMillis = 750))
    }

    val maxVal = remember(points) {
        points.maxOfOrNull { it.value }?.coerceAtLeast(100L) ?: 100L
    }

    val gridColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
    val dotCenterColor = MaterialTheme.colorScheme.surface

    Column(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .pointerInput(points) {
                    detectTapGestures { offset ->
                        if (points.size == 1) {
                            selectedIndex = if (selectedIndex == 0) null else 0
                        } else {
                            val stepX = size.width / (points.size - 1).coerceAtLeast(1)
                            val index = ((offset.x + stepX / 2f) / stepX).toInt().coerceIn(0, points.size - 1)
                            selectedIndex = if (selectedIndex == index) null else index
                        }
                    }
                }
        ) {
            val width = size.width
            val height = size.height - 16f
            val horizontalPad = 14f
            val usableWidth = (width - horizontalPad * 2).coerceAtLeast(1f)
            val stepX = if (points.size <= 1) 0f else usableWidth / (points.size - 1)

            for (i in 0..3) {
                val y = height * (i / 3f)
                drawLine(
                    color = gridColor,
                    start = Offset(0f, y),
                    end = Offset(width, y),
                    strokeWidth = 1.5f
                )
            }

            if (points.size > 1) {
                val path = Path()
                val areaPath = Path()

                points.forEachIndexed { i, point ->
                    val x = horizontalPad + i * stepX
                    val normalizedY = (point.value.toFloat() / maxVal.toFloat()) * height * progress.value
                    val y = height - normalizedY

                    if (i == 0) {
                        path.moveTo(x, y)
                        areaPath.moveTo(x, height)
                        areaPath.lineTo(x, y)
                    } else {
                        val prevX = horizontalPad + (i - 1) * stepX
                        val prevY = height - ((points[i - 1].value.toFloat() / maxVal.toFloat()) * height * progress.value)
                        val controlX = prevX + (x - prevX) / 2f
                        path.cubicTo(controlX, prevY, controlX, y, x, y)
                        areaPath.cubicTo(controlX, prevY, controlX, y, x, y)
                    }
                    if (i == points.size - 1) {
                        areaPath.lineTo(x, height)
                        areaPath.close()
                    }
                }

                drawPath(
                    path = areaPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(lineColor.copy(alpha = 0.28f), lineColor.copy(alpha = 0.0f)),
                        startY = 0f,
                        endY = height
                    )
                )

                drawPath(
                    path = path,
                    color = lineColor,
                    style = Stroke(width = 6f, cap = StrokeCap.Round)
                )
            }

            points.forEachIndexed { i, point ->
                val x = if (points.size == 1) width / 2f else horizontalPad + i * stepX
                val y = height - ((point.value.toFloat() / maxVal.toFloat()) * height * progress.value)
                val isSelected = selectedIndex == i

                if (isSelected) {
                    drawLine(
                        color = lineColor.copy(alpha = 0.45f),
                        start = Offset(x, 0f),
                        end = Offset(x, height),
                        strokeWidth = 2f
                    )
                }

                drawCircle(
                    color = lineColor,
                    radius = if (isSelected) 13f else 8f,
                    center = Offset(x, y)
                )
                drawCircle(
                    color = dotCenterColor,
                    radius = if (isSelected) 6f else 4f,
                    center = Offset(x, y)
                )
            }
        }

        Spacer(modifier = Modifier.height(AppDimensions.paddingMicro))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val step = (points.size / 4).coerceAtLeast(1)
            points.filterIndexed { i, _ -> i % step == 0 || i == points.lastIndex }.take(5).forEach {
                AppTypography.Caption(text = it.label)
            }
        }

        val selected = selectedIndex?.let { points.getOrNull(it) }
        if (selected != null) {
            Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                AppSurface(
                    shape = RoundedCornerShape(AppDimensions.cornerPill),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    AppTypography.LabelMedium(
                        text = "${selected.label} • ${selected.value.formatCurrency(currencyCode)}",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(
                            horizontal = AppDimensions.paddingMedium,
                            vertical = AppDimensions.paddingMicro
                        )
                    )
                }
            }
        }
    }
}
