package com.example.expensetracker.presentation.components.charts

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import com.example.expensetracker.core.common.formatCurrency
import com.example.expensetracker.core.common.formatCurrencyCompact
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppSurface
import java.util.Locale
import kotlin.math.atan2

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DonutChart(
    slices: List<ChartSlice>,
    centerText: String,
    centerValue: Long,
    currencyCode: String = "USD",
    showLegend: Boolean = true,
    isPieMode: Boolean = false,
    modifier: Modifier = Modifier
) {
    var selectedSliceIndex by remember { mutableStateOf<Int?>(null) }
    val progress = remember { Animatable(0f) }

    LaunchedEffect(slices) {
        progress.snapTo(0f)
        progress.animateTo(1f, animationSpec = tween(durationMillis = 750))
    }

    val total = remember(slices) { slices.sumOf { it.value } }
    val trackColor = MaterialTheme.colorScheme.surfaceContainerHighest

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(AppDimensions.components.emptyStateIconContainer * 3.4f)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(slices) {
                        detectTapGestures { offset ->
                            val center = Offset(size.width / 2f, size.height / 2f)
                            val touchVector = offset - center
                            val angle = (Math.toDegrees(
                                atan2(touchVector.y.toDouble(), touchVector.x.toDouble())
                            ).toFloat() + 360f + 90f) % 360f
                            val distance = touchVector.getDistance()
                            val outerRadius = size.width / 2f
                            val innerRadius = if (isPieMode) 0f else outerRadius * 0.55f

                            if (distance in innerRadius..outerRadius && total > 0) {
                                var currentAngle = 0f
                                slices.forEachIndexed { index, slice ->
                                    val sweep = (slice.value.toFloat() / total.toFloat()) * 360f
                                    if (angle >= currentAngle && angle <= currentAngle + sweep) {
                                        selectedSliceIndex = if (selectedSliceIndex == index) null else index
                                        return@detectTapGestures
                                    }
                                    currentAngle += sweep
                                }
                            } else {
                                selectedSliceIndex = null
                            }
                        }
                    }
            ) {
                val strokeWidth = size.width * 0.16f
                val radius = (size.width - strokeWidth * 1.3f) / 2f
                val center = Offset(size.width / 2f, size.height / 2f)
                val arcTopLeft = Offset(center.x - radius, center.y - radius)
                val arcSize = Size(radius * 2, radius * 2)

                if (total <= 0L || slices.isEmpty()) {
                    drawArc(
                        color = trackColor,
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = isPieMode,
                        topLeft = arcTopLeft,
                        size = arcSize,
                        style = if (isPieMode) androidx.compose.ui.graphics.drawscope.Fill else Stroke(width = strokeWidth)
                    )
                    return@Canvas
                }

                var startAngle = -90f
                val gapDegrees = if (slices.size > 1 && !isPieMode) 2f else 0f

                slices.forEachIndexed { index, slice ->
                    val rawSweep = (slice.value.toFloat() / total.toFloat()) * 360f * progress.value
                    val sweepAngle = (rawSweep - gapDegrees).coerceAtLeast(1f)
                    val isSelected = selectedSliceIndex == index
                    val currentStroke = if (isSelected) strokeWidth * 1.2f else strokeWidth

                    if (isPieMode) {
                        drawArc(
                            color = slice.color.copy(alpha = if (selectedSliceIndex == null || isSelected) 1f else 0.45f),
                            startAngle = startAngle,
                            sweepAngle = rawSweep,
                            useCenter = true,
                            topLeft = Offset(center.x - size.width * 0.44f, center.y - size.width * 0.44f),
                            size = Size(size.width * 0.88f, size.width * 0.88f)
                        )
                    } else {
                        drawArc(
                            color = slice.color.copy(alpha = if (selectedSliceIndex == null || isSelected) 1f else 0.4f),
                            startAngle = startAngle,
                            sweepAngle = sweepAngle,
                            useCenter = false,
                            topLeft = arcTopLeft,
                            size = arcSize,
                            style = Stroke(width = currentStroke, cap = StrokeCap.Butt)
                        )
                    }
                    startAngle += rawSweep
                }
            }

            if (!isPieMode) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val selected = selectedSliceIndex?.let { slices.getOrNull(it) }
                    if (selected != null) {
                        AppTypography.LabelSmall(
                            text = selected.label,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                        AppTypography.TitleMedium(
                            text = selected.value.formatCurrencyCompact(currencyCode),
                            color = selected.color,
                            fontWeight = FontWeight.Bold
                        )
                        AppTypography.Caption(
                            text = "%.1f%%".format(Locale.US, selected.percentage)
                        )
                    } else {
                        AppTypography.LabelSmall(
                            text = centerText,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        AppTypography.TitleLarge(
                            text = centerValue.formatCurrencyCompact(currencyCode),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        if (showLegend && slices.isNotEmpty()) {
            Spacer(modifier = Modifier.height(AppDimensions.paddingMedium))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall),
                verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
            ) {
                slices.take(6).forEachIndexed { index, slice ->
                    val isSelected = selectedSliceIndex == index
                    AppSurface(
                        onClick = {
                            selectedSliceIndex = if (selectedSliceIndex == index) null else index
                        },
                        shape = RoundedCornerShape(AppDimensions.cornerPill),
                        color = if (isSelected) {
                            slice.color.copy(alpha = 0.18f)
                        } else {
                            MaterialTheme.colorScheme.surfaceContainer
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(
                                horizontal = AppDimensions.paddingSmall,
                                vertical = AppDimensions.paddingMicro
                            ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(AppDimensions.icons.micro)
                                    .clip(CircleShape)
                                    .background(slice.color)
                            )
                            Spacer(modifier = Modifier.width(AppDimensions.paddingMicro))
                            AppTypography.LabelSmall(
                                text = "${slice.label} ${"%.0f".format(Locale.US, slice.percentage)}%",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}
