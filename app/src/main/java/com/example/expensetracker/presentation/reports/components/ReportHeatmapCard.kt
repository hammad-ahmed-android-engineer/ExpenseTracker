package com.example.expensetracker.presentation.reports.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.components.AppCard
import com.example.expensetracker.presentation.components.charts.DailyHeatmapData
import com.example.expensetracker.presentation.components.charts.DailySpendingHeatmap

@Composable
fun ReportHeatmapCard(
    days: List<DailyHeatmapData>,
    maxDailySpending: Long,
    currencyCode: String,
    modifier: Modifier = Modifier
) {
    AppCard(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(AppDimensions.paddingDefault)) {
            DailySpendingHeatmap(
                days = days,
                maxDailySpending = maxDailySpending,
                currencyCode = currencyCode
            )
        }
    }
}

