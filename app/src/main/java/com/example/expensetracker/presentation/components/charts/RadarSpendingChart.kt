package com.example.expensetracker.presentation.components.charts

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.expensetracker.core.common.formatCurrencyCompact
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RadarSpendingChart(
    entries: List<RadarEntry>,
    currencyCode: String = "USD",
    modifier: Modifier = Modifier
) {
    if (entries.size < 3) return

    val progress = remember { Animatable(0f) }
    LaunchedEffect(entries) {
        progress.snapTo(0f)
        progress.animateTo(1f, animationSpec = tween(durationMillis = 750))
    }

    val maxVal = remember(entries) { entries.maxOf { it.value }.coerceAtLeast(1L) }
    val primary = MaterialTheme.colorScheme.primary
    val gridColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Canvas(
            modifier = Modifier.size(AppDimensions.components.emptyStateIconContainer * 3.4f)
        ) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val maxRadius = size.minDimension * 0.4f
            val count = entries.size
            val angleStep = (2.0 * PI) / count

            for (ring in 1..4) {
                val r = maxRadius * (ring / 4f)
                val ringPath = Path()
                for (i in 0 until count) {
                    val angle = -PI / 2 + i * angleStep
                    val x = center.x + (r * cos(angle)).toFloat()
                    val y = center.y + (r * sin(angle)).toFloat()
                    if (i == 0) ringPath.moveTo(x, y) else ringPath.lineTo(x, y)
                }
                ringPath.close()
                drawPath(path = ringPath, color = gridColor, style = Stroke(width = 1.5f))
            }

            for (i in 0 until count) {
                val angle = -PI / 2 + i * angleStep
                val x = center.x + (maxRadius * cos(angle)).toFloat()
                val y = center.y + (maxRadius * sin(angle)).toFloat()
                drawLine(color = gridColor, start = center, end = Offset(x, y), strokeWidth = 1.5f)
            }

            val dataPath = Path()
            entries.forEachIndexed { i, entry ->
                val ratio = (entry.value.toFloat() / maxVal.toFloat()) * progress.value
                val r = maxRadius * ratio
                val angle = -PI / 2 + i * angleStep
                val x = center.x + (r * cos(angle)).toFloat()
                val y = center.y + (r * sin(angle)).toFloat()
                if (i == 0) dataPath.moveTo(x, y) else dataPath.lineTo(x, y)
            }
            dataPath.close()

            drawPath(path = dataPath, color = primary.copy(alpha = 0.25f))
            drawPath(path = dataPath, color = primary, style = Stroke(width = 4f))

            entries.forEachIndexed { i, entry ->
                val ratio = (entry.value.toFloat() / maxVal.toFloat()) * progress.value
                val r = maxRadius * ratio
                val angle = -PI / 2 + i * angleStep
                val x = center.x + (r * cos(angle)).toFloat()
                val y = center.y + (r * sin(angle)).toFloat()
                drawCircle(color = primary, radius = 7f, center = Offset(x, y))
            }
        }

        Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMicro)
        ) {
            entries.forEach { entry ->
                AppTypography.Caption(
                    text = "${entry.label}: ${entry.value.formatCurrencyCompact(currencyCode)}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

