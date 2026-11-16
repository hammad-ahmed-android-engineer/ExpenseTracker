package com.example.expensetracker.presentation.components.charts

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.stringResource
import com.example.expensetracker.R
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography

@Composable
fun TrendBarLineChart(
    groups: List<BarGroup>,
    currencyCode: String = "USD",
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(AppDimensions.spacing.extraLarge * 7)
) {
    var hasAnimated by rememberSaveable { mutableStateOf(false) }
    val progress = remember { Animatable(if (hasAnimated) 1f else 0f) }

    LaunchedEffect(groups) {
        if (!hasAnimated) {
            progress.animateTo(1f, animationSpec = tween(durationMillis = 800))
            hasAnimated = true
        } else {
            progress.snapTo(1f)
        }
    }

    if (groups.isEmpty()) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            AppTypography.BodyMedium(
                text = stringResource(R.string.no_transactions),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    val displayGroups = groups

    val maxVal = remember(displayGroups) {
        val peak = displayGroups.flatMap { listOf(it.income, it.expense) }.maxOrNull()?.coerceAtLeast(1000L) ?: 100000L
        val rounded = ((peak * 1.25f).toLong() / 1000L + 1) * 1000L
        rounded.coerceAtLeast(10000L)
    }

    val incomeColor = Color(0xFF34D399)
    val expenseColor = Color(0xFFF87171)
    val lineColor = Color(0xFF22D3EE)
    val gridColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)
    val textPaintColor = android.graphics.Color.GRAY

    Column(modifier = Modifier.fillMaxWidth()) {
        Canvas(modifier = modifier) {
            val yAxisWidth = 90f
            val xAxisHeight = 60f
            val chartWidth = size.width - yAxisWidth
            val chartHeight = size.height - xAxisHeight
            if (chartWidth <= 0f || chartHeight <= 0f) return@Canvas

            val ySteps = 4
            val yStepValue = maxVal / ySteps

            // Y-axis ticks and horizontal grid lines
            val paint = android.graphics.Paint().apply {
                color = textPaintColor
                textSize = 28f
                isAntiAlias = true
                textAlign = android.graphics.Paint.Align.RIGHT
            }

            for (i in 0..ySteps) {
                val value = yStepValue * (ySteps - i)
                val y = (chartHeight / ySteps) * i
                val label = if (value >= 1000L) "${value / 1000}K" else "$value"

                drawContext.canvas.nativeCanvas.drawText(
                    label,
                    yAxisWidth - 16f,
                    y + 10f,
                    paint
                )

                drawLine(
                    color = gridColor,
                    start = Offset(yAxisWidth, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1.5f
                )
            }

            // Draw bars and collect line points
            val groupCount = displayGroups.size.coerceAtLeast(1)
            val groupSlotWidth = chartWidth / groupCount
            val barWidth = (groupSlotWidth * 0.24f).coerceIn(6f, 32f)
            val cornerRad = CornerRadius(barWidth / 2f, barWidth / 2f)
            val linePoints = mutableListOf<Offset>()

            displayGroups.forEachIndexed { index, group ->
                val groupCenter = yAxisWidth + (index + 0.5f) * groupSlotWidth
                val incomeX = groupCenter - barWidth - 3f
                val expenseX = groupCenter + 3f

                val incomeHeight = (group.income.toFloat() / maxVal.toFloat()) * chartHeight * progress.value
                val expenseHeight = (group.expense.toFloat() / maxVal.toFloat()) * chartHeight * progress.value

                // Income Bar
                drawRoundRect(
                    color = incomeColor,
                    topLeft = Offset(incomeX, chartHeight - incomeHeight),
                    size = Size(barWidth, incomeHeight),
                    cornerRadius = cornerRad
                )

                // Expense Bar
                drawRoundRect(
                    color = expenseColor,
                    topLeft = Offset(expenseX, chartHeight - expenseHeight),
                    size = Size(barWidth, expenseHeight),
                    cornerRadius = cornerRad
                )

                // Line point sits on the peak of the income bar
                val pointY = chartHeight - incomeHeight.coerceAtLeast(10f)
                linePoints.add(Offset(groupCenter, pointY))

                // X-axis label
                val xPaint = android.graphics.Paint().apply {
                    color = textPaintColor
                    textSize = 28f
                    isAntiAlias = true
                    textAlign = android.graphics.Paint.Align.CENTER
                }
                drawContext.canvas.nativeCanvas.drawText(
                    group.label,
                    groupCenter,
                    size.height - 12f,
                    xPaint
                )
            }

            // Smooth curved line over bars
            if (linePoints.size >= 2) {
                val path = Path()
                path.moveTo(linePoints.first().x, linePoints.first().y)

                for (i in 0 until linePoints.size - 1) {
                    val p0 = linePoints[i]
                    val p1 = linePoints[i + 1]
                    val controlX = (p0.x + p1.x) / 2f
                    path.cubicTo(controlX, p0.y, controlX, p1.y, p1.x, p1.y)
                }

                drawPath(
                    path = path,
                    color = lineColor,
                    style = Stroke(width = 4f, cap = StrokeCap.Round)
                )

                linePoints.forEach { point ->
                    drawCircle(
                        color = lineColor,
                        radius = 6f,
                        center = point
                    )
                    drawCircle(
                        color = Color.White,
                        radius = 3f,
                        center = point
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))

        // Bottom Legend
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(AppDimensions.paddingSmall)
                    .clip(CircleShape)
                    .background(incomeColor)
            )
            Spacer(modifier = Modifier.width(AppDimensions.paddingMicro))
            AppTypography.LabelSmall(text = stringResource(R.string.income))

            Spacer(modifier = Modifier.width(AppDimensions.paddingLarge))

            Box(
                modifier = Modifier
                    .size(AppDimensions.paddingSmall)
                    .clip(CircleShape)
                    .background(expenseColor)
            )
            Spacer(modifier = Modifier.width(AppDimensions.paddingMicro))
            AppTypography.LabelSmall(text = stringResource(R.string.expenses))
        }
    }
}

