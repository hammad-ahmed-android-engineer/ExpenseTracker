package com.example.expensetracker.presentation.components.charts

import androidx.compose.ui.graphics.Color

data class ChartSlice(
    val label: String,
    val value: Long,
    val color: Color,
    val percentage: Float
)

data class BarGroup(
    val label: String,
    val income: Long,
    val expense: Long
)

data class LinePoint(
    val label: String,
    val value: Long
)

data class DailyHeatmapData(
    val dayOfMonth: Int,
    val amount: Long,
    val intensity: Float
)

data class WaterfallItem(
    val label: String,
    val amount: Long,
    val isPositive: Boolean
)

data class RadarEntry(
    val label: String,
    val value: Long
)

