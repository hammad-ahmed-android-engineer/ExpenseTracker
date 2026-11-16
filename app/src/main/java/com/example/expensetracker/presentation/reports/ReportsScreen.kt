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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.expensetracker.R
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppCircularProgressIndicator
import com.example.expensetracker.core.designsystem.components.AppScaffold
import com.example.expensetracker.core.designsystem.components.AppTopAppBar
import com.example.expensetracker.presentation.reports.components.ReportHeatmapCard
import com.example.expensetracker.presentation.reports.components.ReportInsightsHighlightCard
import com.example.expensetracker.presentation.reports.components.ReportPeriodSelector
import com.example.expensetracker.presentation.reports.components.ReportSummaryCard
import com.example.expensetracker.presentation.reports.components.ReportTrendChartCard
import java.util.Calendar

@Composable
fun ReportsScreen(
    viewModel: ReportsViewModel,
    onNavigateToInsights: () -> Unit,
    onNavigateToCalendar: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
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
                            ReportPeriodSelector(
                                selectedPeriod = state.selectedPeriod,
                                dateRangeLabel = state.dateRangeLabel,
                                onSelectPeriod = { viewModel.selectPeriod(it) },
                                onPreviousPeriod = { viewModel.previousPeriod() },
                                onNextPeriod = { viewModel.nextPeriod() }
                            )
                        }

                        item(key = "financial_summary") {
                            ReportSummaryCard(
                                totalIncome = state.totalIncome,
                                totalExpense = state.totalExpense,
                                netSavings = state.netSavings,
                                currencyCode = state.currencyCode
                            )
                        }

                        item(key = "trend_chart") {
                            ReportTrendChartCard(
                                periodDisplayName = state.selectedPeriod.displayName,
                                trendGroups = trendGroups,
                                currencyCode = state.currencyCode
                            )
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
                            ReportHeatmapCard(
                                days = state.heatmapDays,
                                maxDailySpending = state.maxDailySpending,
                                currencyCode = state.currencyCode
                            )
                        }

                        item(key = "insights_card") {
                            ReportInsightsHighlightCard(
                                highestSpendingDayText = state.highestSpendingDayText,
                                onNavigateToInsights = onNavigateToInsights
                            )
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
                        ReportPeriodSelector(
                            selectedPeriod = state.selectedPeriod,
                            dateRangeLabel = state.dateRangeLabel,
                            onSelectPeriod = { viewModel.selectPeriod(it) },
                            onPreviousPeriod = { viewModel.previousPeriod() },
                            onNextPeriod = { viewModel.nextPeriod() }
                        )
                    }

                    item(key = "financial_summary") {
                        ReportSummaryCard(
                            totalIncome = state.totalIncome,
                            totalExpense = state.totalExpense,
                            netSavings = state.netSavings,
                            currencyCode = state.currencyCode
                        )
                    }

                    item(key = "trend_chart") {
                        ReportTrendChartCard(
                            periodDisplayName = state.selectedPeriod.displayName,
                            trendGroups = trendGroups,
                            currencyCode = state.currencyCode
                        )
                    }

                    item(key = "heatmap_calendar") {
                        ReportHeatmapCard(
                            days = state.heatmapDays,
                            maxDailySpending = state.maxDailySpending,
                            currencyCode = state.currencyCode
                        )
                    }

                    item(key = "insights_card") {
                        ReportInsightsHighlightCard(
                            highestSpendingDayText = state.highestSpendingDayText,
                            onNavigateToInsights = onNavigateToInsights
                        )
                    }

                    item { Spacer(modifier = Modifier.height(AppDimensions.paddingLarge)) }
                }
            }
        }
    }
}
