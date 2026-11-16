package com.example.expensetracker.presentation.reports.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppCard
import com.example.expensetracker.presentation.components.charts.BarGroup
import com.example.expensetracker.presentation.components.charts.TrendBarLineChart

@Composable
fun ReportTrendChartCard(
    periodDisplayName: String,
    trendGroups: List<BarGroup>,
    currencyCode: String,
    modifier: Modifier = Modifier
) {
    AppCard(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(AppDimensions.paddingDefault)) {
            AppTypography.TitleMedium(
                text = "$periodDisplayName Trends Bar Chart & Line Graph",
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(AppDimensions.paddingMedium))
            TrendBarLineChart(
                groups = trendGroups,
                currencyCode = currencyCode
            )
        }
    }
}

