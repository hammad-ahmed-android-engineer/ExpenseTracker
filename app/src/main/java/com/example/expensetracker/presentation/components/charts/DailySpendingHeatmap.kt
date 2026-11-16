package com.example.expensetracker.presentation.components.charts

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.expensetracker.R
import com.example.expensetracker.core.common.formatCurrency
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.utils.clickableDebounced
import com.example.expensetracker.core.designsystem.components.AppSurface

@Composable
fun DailySpendingHeatmap(
    days: List<DailyHeatmapData>,
    maxDailySpending: Long,
    currencyCode: String,
    modifier: Modifier = Modifier,
    onDayClick: ((DailyHeatmapData) -> Unit)? = null
) {
    val primary = MaterialTheme.colorScheme.primary
    val emptyColor = MaterialTheme.colorScheme.surfaceContainerHighest

    // Find the highest spending day to initialize the selection inspection
    val defaultSelection = remember(days) {
        days.filter { it.isInSelectedPeriod && it.amount > 0L }.maxByOrNull { it.amount }
            ?: days.firstOrNull { it.isInSelectedPeriod }
    }
    var selectedDay by remember(days) { mutableStateOf<DailyHeatmapData?>(defaultSelection) }

    val weeks = remember(days) { days.chunked(7) }

    Column(modifier = modifier.fillMaxWidth()) {
        // Top Title & Less -> More Legend Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppTypography.TitleMedium(
                text = stringResource(R.string.daily_spending_heatmap),
                fontWeight = FontWeight.SemiBold
            )

            // Header Legend: Less ▫ ◽ ◻ ◼ More
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingTiny)
            ) {
                AppTypography.LabelSmall(
                    text = stringResource(R.string.less),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                listOf(0f, 0.24f, 0.49f, 0.74f, 1f).forEach { intVal ->
                    val color = heatmapColorForIntensity(intVal, primary, emptyColor)
                    Box(
                        modifier = Modifier
                            .size(AppDimensions.icons.micro)
                            .clip(RoundedCornerShape(AppDimensions.corners.micro))
                            .background(color)
                    )
                }
                AppTypography.LabelSmall(
                    text = stringResource(R.string.more),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(AppDimensions.paddingMedium))

        // Heatmap Matrix: 7 rows (Mon to Sun) with week columns
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            // Day Labels Column
            Column(
                modifier = Modifier
                    .width(AppDimensions.components.buttonSmallHeight)
                    .padding(end = AppDimensions.paddingMicro),
                verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMicro)
            ) {
                val rowLabels = listOf("Mon", "", "Wed", "", "Fri", "", "")
                rowLabels.forEach { label ->
                    Box(
                        modifier = Modifier.size(width = AppDimensions.spacing.extraLarge, height = AppDimensions.icons.small),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (label.isNotEmpty()) {
                            AppTypography.LabelSmall(
                                text = label,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Week Columns Grid
            Row(
                horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingMicro),
                modifier = Modifier
                    .weight(1f)
                    .horizontalScroll(rememberScrollState())
            ) {
                weeks.forEach { week ->
                    Column(
                        verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMicro)
                    ) {
                        (0..6).forEach { dayIdx ->
                            val dayData = week.getOrNull(dayIdx)
                            if (dayData != null) {
                                val isSelected = selectedDay == dayData
                                val cellColor = heatmapColorForIntensity(dayData.intensity, primary, emptyColor)
                                val cellAlpha = if (dayData.isInSelectedPeriod) 1f else 0.35f

                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(AppDimensions.icons.small)
                                        .alpha(cellAlpha)
                                        .clip(RoundedCornerShape(AppDimensions.corners.micro))
                                        .background(cellColor)
                                        .then(
                                            if (isSelected) {
                                                Modifier.border(
                                                    BorderStroke(AppDimensions.borders.medium, MaterialTheme.colorScheme.onSurface),
                                                    RoundedCornerShape(AppDimensions.corners.micro)
                                                )
                                            } else Modifier
                                        )
                                        .clickableDebounced {
                                            selectedDay = dayData
                                            onDayClick?.invoke(dayData)
                                        }
                                ) {
                                    AppTypography.LabelSmall(
                                        text = dayData.dayOfMonth.toString(),
                                        fontWeight = if (isSelected || dayData.intensity >= 0.5f) FontWeight.Bold else FontWeight.Normal,
                                        color = if (dayData.intensity >= 0.75f) {
                                            MaterialTheme.colorScheme.onPrimary
                                        } else {
                                            MaterialTheme.colorScheme.onSurface.copy(alpha = if (dayData.isInSelectedPeriod) 0.85f else 0.4f)
                                        }
                                    )
                                }
                            } else {
                                Spacer(modifier = Modifier.size(AppDimensions.icons.small))
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(AppDimensions.paddingMedium))

        // Spending Level Ranges Guide
        AppSurface(
            shape = RoundedCornerShape(AppDimensions.cornerSmall),
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(AppDimensions.paddingSmall)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = AppDimensions.paddingMicro),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppTypography.LabelSmall(
                        text = stringResource(R.string.spending_levels_guide),
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    AppTypography.LabelSmall(
                        text = if (maxDailySpending > 0L) stringResource(R.string.heatmap_peak_label, maxDailySpending.formatCurrency(currencyCode)) else stringResource(R.string.no_expenses),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                val levelRanges = rememberLevelRanges(maxDailySpending, currencyCode)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    levelRanges.forEach { level ->
                        val swatchColor = heatmapColorForIntensity(level.sampleIntensity, primary, emptyColor)
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMicro),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(AppDimensions.icons.tiny)
                                    .clip(RoundedCornerShape(AppDimensions.corners.micro))
                                    .background(swatchColor)
                            )
                            AppTypography.LabelSmall(
                                text = level.label,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            AppTypography.LabelSmall(
                                text = level.rangeText,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))

        // Interactive Selected Day Inspector
        selectedDay?.let { day ->
            AppSurface(
                shape = RoundedCornerShape(AppDimensions.cornerSmall),
                color = MaterialTheme.colorScheme.surfaceContainerHighest,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = AppDimensions.paddingMedium,
                            vertical = AppDimensions.paddingSmall
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
                    ) {
                        val levelColor = heatmapColorForIntensity(day.intensity, primary, emptyColor)
                        Box(
                            modifier = Modifier
                                .size(AppDimensions.icons.small)
                                .clip(RoundedCornerShape(AppDimensions.corners.micro))
                                .background(levelColor)
                        )
                        Column {
                            AppTypography.BodySmall(
                                text = day.formattedDate.ifEmpty { stringResource(R.string.heatmap_day_format, day.dayOfMonth) },
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            AppTypography.LabelSmall(
                                text = spendingLevelDescription(day.intensity, day.amount > 0L, day.transactionCount),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    AppTypography.TitleSmall(
                        text = if (day.amount > 0L) day.amount.formatCurrency(currencyCode) else "$0.00",
                        fontWeight = FontWeight.Bold,
                        color = if (day.amount > 0L) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

private fun heatmapColorForIntensity(intensity: Float, primary: Color, emptyColor: Color): Color {
    return when {
        intensity <= 0f -> emptyColor
        intensity < 0.25f -> primary.copy(alpha = 0.25f)
        intensity < 0.50f -> primary.copy(alpha = 0.50f)
        intensity < 0.75f -> primary.copy(alpha = 0.75f)
        else -> primary
    }
}

private data class HeatmapLevelInfo(
    val label: String,
    val rangeText: String,
    val sampleIntensity: Float
)

@Composable
private fun rememberLevelRanges(maxExpense: Long, currencyCode: String): List<HeatmapLevelInfo> {
    val noneLabel = stringResource(R.string.level_none)
    val lowLabel = stringResource(R.string.level_low)
    val medLabel = stringResource(R.string.level_med)
    val highLabel = stringResource(R.string.level_high)
    val peakLabel = stringResource(R.string.level_peak)

    return remember(maxExpense, currencyCode, noneLabel, lowLabel, medLabel, highLabel, peakLabel) {
        if (maxExpense <= 0L) {
            listOf(
                HeatmapLevelInfo(noneLabel, "$0", 0f),
                HeatmapLevelInfo(lowLabel, "< 25%", 0.2f),
                HeatmapLevelInfo(medLabel, "25-50%", 0.45f),
                HeatmapLevelInfo(highLabel, "50-75%", 0.7f),
                HeatmapLevelInfo(peakLabel, "75-100%", 1f)
            )
        } else {
            val q1 = maxExpense / 4
            val q2 = maxExpense / 2
            val q3 = (maxExpense * 3) / 4
            listOf(
                HeatmapLevelInfo(noneLabel, "$0", 0f),
                HeatmapLevelInfo(lowLabel, "< ${q1.formatCurrency(currencyCode)}", 0.2f),
                HeatmapLevelInfo(medLabel, "${q2.formatCurrency(currencyCode)}", 0.45f),
                HeatmapLevelInfo(highLabel, "${q3.formatCurrency(currencyCode)}", 0.7f),
                HeatmapLevelInfo(peakLabel, "≥ ${q3.formatCurrency(currencyCode)}", 1f)
            )
        }
    }
}

@Composable
private fun spendingLevelDescription(intensity: Float, hasExpense: Boolean, txCount: Int): String {
    if (!hasExpense || intensity <= 0f) {
        return stringResource(R.string.no_spending_recorded)
    }
    return when {
        intensity < 0.25f -> if (txCount > 0) stringResource(R.string.spending_level_1_tx, txCount) else stringResource(R.string.spending_level_1)
        intensity < 0.50f -> if (txCount > 0) stringResource(R.string.spending_level_2_tx, txCount) else stringResource(R.string.spending_level_2)
        intensity < 0.75f -> if (txCount > 0) stringResource(R.string.spending_level_3_tx, txCount) else stringResource(R.string.spending_level_3)
        else -> if (txCount > 0) stringResource(R.string.spending_level_4_tx, txCount) else stringResource(R.string.spending_level_4)
    }
}

