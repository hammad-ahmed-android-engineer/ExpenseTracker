package com.example.expensetracker.presentation.reports

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.expensetracker.R
import com.example.expensetracker.core.common.formatDate
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppCard
import com.example.expensetracker.core.designsystem.components.AppCircularProgressIndicator
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppIconButton
import com.example.expensetracker.core.designsystem.components.AppScaffold
import com.example.expensetracker.core.designsystem.components.AppSegmentedSelector
import com.example.expensetracker.core.designsystem.components.AppSurface
import com.example.expensetracker.core.designsystem.components.AppTopAppBar
import com.example.expensetracker.presentation.components.charts.BarGroup
import com.example.expensetracker.presentation.components.charts.DailySpendingHeatmap
import com.example.expensetracker.presentation.components.charts.TrendBarLineChart
import java.util.Calendar

@Composable
fun ReportsScreen(
    viewModel: ReportsViewModel,
    onNavigateToInsights: () -> Unit,
    onNavigateToCalendar: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    AppScaffold(
        topBar = {
            AppTopAppBar(
                title = {
                    AppTypography.TitleLarge(
                        text = stringResource(R.string.insights_analytics_title),
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { padding ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                AppCircularProgressIndicator()
            }
        } else {
            val trendGroups = state.trendGroups

            if (isLandscape) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = AppDimensions.paddingDefault),
                    horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingDefault)
                ) {
                    // Left Column: Period Switcher & Trend Chart
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        contentPadding = PaddingValues(vertical = AppDimensions.paddingSmall),
                        verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
                    ) {
                        item(key = "period_selector") {
                            Column(verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)) {
                                androidx.compose.foundation.lazy.LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    items(DateFilterPeriod.entries.size) { idx ->
                                        val period = DateFilterPeriod.entries[idx]
                                        com.example.expensetracker.core.designsystem.components.AppFilterChip(
                                            selected = state.selectedPeriod == period,
                                            onClick = { viewModel.selectPeriod(period) },
                                            label = { AppTypography.LabelSmall(text = period.displayName) }
                                        )
                                    }
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AppIconButton(onClick = { viewModel.previousPeriod() }) {
                                        AppIcon(
                                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                            contentDescription = "Previous Range"
                                        )
                                    }
                                    AppTypography.TitleSmall(
                                        text = state.dateRangeLabel,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    AppIconButton(onClick = { viewModel.nextPeriod() }) {
                                        AppIcon(
                                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                            contentDescription = "Next Range"
                                        )
                                    }
                                }
                            }
                        }

                        item(key = "trend_chart") {
                            AppCard(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(AppDimensions.paddingDefault)) {
                                    AppTypography.TitleMedium(
                                        text = "${state.selectedPeriod.displayName} Trends Bar Chart & Line Graph",
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Spacer(modifier = Modifier.height(AppDimensions.paddingMedium))
                                    TrendBarLineChart(
                                        groups = trendGroups,
                                        currencyCode = state.currencyCode
                                    )
                                }
                            }
                        }
                    }

                    // Right Column: Heatmap Calendar & Insights
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        contentPadding = PaddingValues(vertical = AppDimensions.paddingSmall),
                        verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
                    ) {
                        item(key = "heatmap_calendar") {
                            AppCard(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(AppDimensions.paddingDefault)) {
                                    DailySpendingHeatmap(
                                        days = state.heatmapDays,
                                        maxDailySpending = state.maxDailySpending,
                                        currencyCode = state.currencyCode
                                    )
                                }
                            }
                        }

                        item(key = "insights_card") {
                            AppCard(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(AppDimensions.paddingDefault)) {
                                    AppTypography.TitleMedium(
                                        text = stringResource(R.string.nav_insights),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))
                                    AppSurface(
                                        shape = androidx.compose.foundation.shape.RoundedCornerShape(AppDimensions.cornerMedium),
                                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(AppDimensions.paddingMedium),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
                                        ) {
                                            AppIcon(
                                                imageVector = Icons.Default.Lightbulb,
                                                contentDescription = null,
                                                tint = Color(0xFFFBBF24),
                                                modifier = Modifier.size(AppDimensions.icons.medium)
                                            )
                                            AppTypography.BodyMedium(
                                                text = "${stringResource(R.string.highest_spending_day_prefix)} ${state.highestSpendingDayText}.",
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = AppDimensions.paddingDefault),
                    contentPadding = PaddingValues(vertical = AppDimensions.paddingSmall),
                    verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
                ) {
                    item(key = "period_selector") {
                        Column(verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)) {
                            androidx.compose.foundation.lazy.LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(DateFilterPeriod.entries.size) { idx ->
                                    val period = DateFilterPeriod.entries[idx]
                                    com.example.expensetracker.core.designsystem.components.AppFilterChip(
                                        selected = state.selectedPeriod == period,
                                        onClick = { viewModel.selectPeriod(period) },
                                        label = { AppTypography.LabelSmall(text = period.displayName) }
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AppIconButton(onClick = { viewModel.previousPeriod() }) {
                                    AppIcon(
                                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                        contentDescription = "Previous Range"
                                    )
                                }
                                AppTypography.TitleSmall(
                                    text = state.dateRangeLabel,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                AppIconButton(onClick = { viewModel.nextPeriod() }) {
                                    AppIcon(
                                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                        contentDescription = "Next Range"
                                    )
                                }
                            }
                        }
                    }

                    item(key = "trend_chart") {
                        AppCard(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(AppDimensions.paddingDefault)) {
                                AppTypography.TitleMedium(
                                    text = "${state.selectedPeriod.displayName} Trends Bar Chart & Line Graph",
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(AppDimensions.paddingMedium))
                                TrendBarLineChart(
                                    groups = trendGroups,
                                    currencyCode = state.currencyCode
                                )
                            }
                        }
                    }

                    item(key = "heatmap_calendar") {
                        AppCard(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(AppDimensions.paddingDefault)) {
                                DailySpendingHeatmap(
                                    days = state.heatmapDays,
                                    maxDailySpending = state.maxDailySpending,
                                    currencyCode = state.currencyCode
                                )
                            }
                        }
                    }

                    item(key = "insights_card") {
                        AppCard(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(AppDimensions.paddingDefault)) {
                                AppTypography.TitleMedium(
                                    text = stringResource(R.string.nav_insights),
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))
                                AppSurface(
                                    shape = androidx.compose.foundation.shape.RoundedCornerShape(AppDimensions.cornerMedium),
                                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(AppDimensions.paddingMedium),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
                                    ) {
                                        AppIcon(
                                            imageVector = Icons.Default.Lightbulb,
                                            contentDescription = null,
                                            tint = Color(0xFFFBBF24),
                                            modifier = Modifier.size(AppDimensions.icons.medium)
                                        )
                                        AppTypography.BodyMedium(
                                            text = "${stringResource(R.string.highest_spending_day_prefix)} ${state.highestSpendingDayText}.",
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(AppDimensions.paddingLarge)) }
                }
            }
        }
    }
}
