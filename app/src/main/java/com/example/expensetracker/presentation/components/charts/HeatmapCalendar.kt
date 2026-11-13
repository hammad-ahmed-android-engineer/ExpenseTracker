package com.example.expensetracker.presentation.components.charts

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppSurface

@Composable
fun HeatmapCalendar(
    monthName: String,
    days: List<DailyHeatmapData>,
    selectedDayOfMonth: Int? = null,
    onDayClick: (DailyHeatmapData) -> Unit,
    modifier: Modifier = Modifier
) {
    val weekDays = listOf("M", "T", "W", "T", "F", "S", "S")
    val primary = MaterialTheme.colorScheme.primary

    Column(modifier = modifier.padding(AppDimensions.paddingMedium)) {
        AppTypography.TitleMedium(
            text = monthName,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = AppDimensions.paddingMedium)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            weekDays.forEach { day ->
                AppTypography.LabelSmall(
                    text = day,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))

        days.chunked(7).forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = AppDimensions.paddingMicro),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                row.forEach { dayData ->
                    val isSelected = selectedDayOfMonth == dayData.dayOfMonth
                    val bgColor = when {
                        dayData.intensity <= 0f -> MaterialTheme.colorScheme.surfaceContainerHigh
                        dayData.intensity < 0.25f -> primary.copy(alpha = 0.22f)
                        dayData.intensity < 0.5f -> primary.copy(alpha = 0.45f)
                        dayData.intensity < 0.75f -> primary.copy(alpha = 0.70f)
                        else -> primary
                    }
                    val textColor = when {
                        dayData.intensity >= 0.75f -> MaterialTheme.colorScheme.onPrimary
                        else -> MaterialTheme.colorScheme.onSurface
                    }

                    AppSurface(
                        onClick = { onDayClick(dayData) },
                        modifier = Modifier.size(AppDimensions.components.buttonSmallHeight),
                        shape = RoundedCornerShape(AppDimensions.cornerSmall),
                        color = bgColor,
                        border = if (isSelected) {
                            BorderStroke(AppDimensions.borders.medium, MaterialTheme.colorScheme.onSurface)
                        } else null
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            AppTypography.LabelSmall(
                                text = dayData.dayOfMonth.toString(),
                                fontWeight = if (isSelected || dayData.intensity > 0f) FontWeight.Bold else FontWeight.Normal,
                                color = textColor
                            )
                        }
                    }
                }
                repeat((7 - row.size).coerceAtLeast(0)) {
                    Spacer(modifier = Modifier.size(AppDimensions.components.buttonSmallHeight))
                }
            }
        }
    }
}
