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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import com.example.expensetracker.R
import com.example.expensetracker.core.common.formatDate
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppCard
import com.example.expensetracker.core.designsystem.components.AppCircularProgressIndicator
import com.example.expensetracker.core.designsystem.components.AppFilterChip
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppIconButton
import com.example.expensetracker.core.designsystem.components.AppOutlinedButton
import com.example.expensetracker.core.designsystem.components.AppScaffold
import com.example.expensetracker.core.designsystem.components.AppSegmentedSelector
import com.example.expensetracker.core.designsystem.components.AppTopAppBar
import com.example.expensetracker.core.utils.toComposeColor
import com.example.expensetracker.presentation.components.EmptyStateView
import com.example.expensetracker.presentation.components.charts.BarGroup
import com.example.expensetracker.presentation.components.charts.ChartSlice
import com.example.expensetracker.presentation.components.charts.DonutChart
import com.example.expensetracker.presentation.components.charts.GroupedBarChart
import com.example.expensetracker.presentation.components.charts.LineChart
import com.example.expensetracker.presentation.components.charts.LinePoint
import com.example.expensetracker.presentation.components.charts.RadarEntry
import com.example.expensetracker.presentation.components.charts.RadarSpendingChart
import com.example.expensetracker.presentation.components.charts.WaterfallChart
import com.example.expensetracker.presentation.components.charts.WaterfallItem
import com.example.expensetracker.presentation.reports.components.CategoryBreakdownRow
import com.example.expensetracker.presentation.reports.components.ReportsKpiCard

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
                title = { AppTypography.TitleLarge(text = stringResource(R.string.financial_analytics)) },
                actions = {
                    AppIconButton(onClick = onNavigateToCalendar) {
                        AppIcon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = stringResource(R.string.nav_calendar)
                        )
                    }
                    AppIconButton(onClick = onNavigateToInsights) {
                        AppIcon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = stringResource(R.string.nav_insights)
                        )
                    }
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
        } else if (isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = AppDimensions.paddingDefault),
                horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingDefault)
            ) {
                Column(
                    modifier = Modifier
                        .weight(0.4f)
                        .fillMaxHeight()
                        .padding(vertical = AppDimensions.paddingSmall),
                    verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
                ) {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)) {
                        items(DateFilterPeriod.entries) { period ->
                            AppFilterChip(
                                selected = state.selectedPeriod == period,
                                onClick = { viewModel.selectPeriod(period) },
                                label = { AppTypography.LabelSmall(text = period.displayName) }
                            )
                        }
                    }

                    ReportsKpiCard(
                        totalIncome = state.totalIncome,
                        totalExpense = state.totalExpense,
                        netSavings = state.netSavings,
                        currencyCode = state.currencyCode
                    )

                    AppOutlinedButton(
                        onClick = onNavigateToInsights,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AppTypography.ButtonText(text = stringResource(R.string.insights_title))
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .weight(0.6f)
                        .fillMaxHeight(),
                    contentPadding = PaddingValues(vertical = AppDimensions.paddingSmall),
                    verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
                ) {
                    item {
                        AppSegmentedSelector(
                            options = ReportChartTab.entries,
                            selectedOption = state.selectedTab,
                            onOptionSelected = { viewModel.selectTab(it) },
                            labelProvider = { it.displayName }
                        )
                    }

                    if (state.dailySpendings.isEmpty() && state.categorySpendings.isEmpty()) {
                        item {
                            EmptyStateView(
                                title = "No Analytics Yet",
                                description = "Add transactions in this period to generate interactive financial charts."
                            )
                        }
                    } else {
                        when (state.selectedTab) {
                            ReportChartTab.OVERVIEW -> {
                                if (state.categorySpendings.isNotEmpty()) {
                                    item {
                                        AppCard(modifier = Modifier.fillMaxWidth()) {
                                            Column(modifier = Modifier.padding(AppDimensions.paddingDefault)) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    AppTypography.TitleMedium(text = "Category Breakdown")
                                                    AppFilterChip(
                                                        selected = state.isPieMode,
                                                        onClick = { viewModel.togglePieMode(!state.isPieMode) },
                                                        label = { AppTypography.LabelSmall(text = if (state.isPieMode) "Pie" else "Donut") }
                                                    )
                                                }
                                                Spacer(modifier = Modifier.height(AppDimensions.paddingMedium))
                                                DonutChart(
                                                    slices = state.categorySpendings.map {
                                                        ChartSlice(
                                                            label = it.categoryName,
                                                            value = it.totalAmount,
                                                            color = it.categoryColor.toComposeColor(),
                                                            percentage = it.percentage
                                                        )
                                                    },
                                                    centerText = stringResource(R.string.total_expenses),
                                                    centerValue = state.totalExpense,
                                                    currencyCode = state.currencyCode,
                                                    isPieMode = state.isPieMode
                                                )
                                            }
                                        }
                                    }
                                    items(state.categorySpendings, key = { it.categoryId }) { cs ->
                                        CategoryBreakdownRow(summary = cs, currencyCode = state.currencyCode)
                                    }
                                }
                            }
                            ReportChartTab.TRENDS -> {
                                if (state.dailySpendings.isNotEmpty()) {
                                    item {
                                        AppCard(modifier = Modifier.fillMaxWidth()) {
                                            Column(modifier = Modifier.padding(AppDimensions.paddingDefault)) {
                                                AppTypography.TitleMedium(text = stringResource(R.string.daily_income_vs_expenses))
                                                Spacer(modifier = Modifier.height(AppDimensions.paddingMedium))
                                                GroupedBarChart(
                                                    groups = state.dailySpendings.map {
                                                        BarGroup(
                                                            label = it.date.formatDate("dd MMM"),
                                                            income = it.totalIncome,
                                                            expense = it.totalExpense
                                                        )
                                                    },
                                                    currencyCode = state.currencyCode
                                                )
                                            }
                                        }
                                    }
                                    item {
                                        AppCard(modifier = Modifier.fillMaxWidth()) {
                                            Column(modifier = Modifier.padding(AppDimensions.paddingDefault)) {
                                                AppTypography.TitleMedium(text = stringResource(R.string.expense_trend_line))
                                                Spacer(modifier = Modifier.height(AppDimensions.paddingMedium))
                                                LineChart(
                                                    points = state.dailySpendings.map {
                                                        LinePoint(
                                                            label = it.date.formatDate("dd MMM"),
                                                            value = it.totalExpense
                                                        )
                                                    },
                                                    currencyCode = state.currencyCode
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            ReportChartTab.CASHFLOW -> {
                                item {
                                    AppCard(modifier = Modifier.fillMaxWidth()) {
                                        Column(modifier = Modifier.padding(AppDimensions.paddingDefault)) {
                                            AppTypography.TitleMedium(text = "Cashflow Waterfall")
                                            Spacer(modifier = Modifier.height(AppDimensions.paddingMedium))
                                            WaterfallChart(
                                                items = buildWaterfallItems(state),
                                                currencyCode = state.currencyCode
                                            )
                                        }
                                    }
                                }
                            }
                            ReportChartTab.RADAR -> {
                                item {
                                    AppCard(modifier = Modifier.fillMaxWidth()) {
                                        Column(modifier = Modifier.padding(AppDimensions.paddingDefault)) {
                                            AppTypography.TitleMedium(text = "Category Radar")
                                            Spacer(modifier = Modifier.height(AppDimensions.paddingMedium))
                                            if (state.categorySpendings.size >= 3) {
                                                RadarSpendingChart(
                                                    entries = state.categorySpendings.take(6).map {
                                                        RadarEntry(label = it.categoryName, value = it.totalAmount)
                                                    },
                                                    currencyCode = state.currencyCode
                                                )
                                            } else {
                                                AppTypography.BodyMedium(
                                                    text = "Need at least 3 expense categories in this period for radar comparison.",
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }
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
                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)) {
                        items(DateFilterPeriod.entries) { period ->
                            AppFilterChip(
                                selected = state.selectedPeriod == period,
                                onClick = { viewModel.selectPeriod(period) },
                                label = { AppTypography.LabelSmall(text = period.displayName) }
                            )
                        }
                    }
                }

                item {
                    ReportsKpiCard(
                        totalIncome = state.totalIncome,
                        totalExpense = state.totalExpense,
                        netSavings = state.netSavings,
                        currencyCode = state.currencyCode
                    )
                }

                item {
                    AppSegmentedSelector(
                        options = ReportChartTab.entries,
                        selectedOption = state.selectedTab,
                        onOptionSelected = { viewModel.selectTab(it) },
                        labelProvider = { it.displayName }
                    )
                }

                if (state.dailySpendings.isEmpty() && state.categorySpendings.isEmpty()) {
                    item {
                        EmptyStateView(
                            title = "No Analytics Yet",
                            description = "Add transactions in this period to generate interactive financial charts."
                        )
                    }
                } else {
                    when (state.selectedTab) {
                        ReportChartTab.OVERVIEW -> {
                            if (state.categorySpendings.isNotEmpty()) {
                                item {
                                    AppCard(modifier = Modifier.fillMaxWidth()) {
                                        Column(modifier = Modifier.padding(AppDimensions.paddingDefault)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                AppTypography.TitleMedium(text = "Category Breakdown")
                                                AppFilterChip(
                                                    selected = state.isPieMode,
                                                    onClick = { viewModel.togglePieMode(!state.isPieMode) },
                                                    label = { AppTypography.LabelSmall(text = if (state.isPieMode) "Pie" else "Donut") }
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(AppDimensions.paddingMedium))
                                            DonutChart(
                                                slices = state.categorySpendings.map {
                                                    ChartSlice(
                                                        label = it.categoryName,
                                                        value = it.totalAmount,
                                                        color = it.categoryColor.toComposeColor(),
                                                        percentage = it.percentage
                                                    )
                                                },
                                                centerText = stringResource(R.string.total_expenses),
                                                centerValue = state.totalExpense,
                                                currencyCode = state.currencyCode,
                                                isPieMode = state.isPieMode
                                            )
                                        }
                                    }
                                }
                                item {
                                    AppTypography.TitleMedium(text = stringResource(R.string.monthly_spending_category))
                                }
                                items(state.categorySpendings, key = { it.categoryId }) { cs ->
                                    CategoryBreakdownRow(summary = cs, currencyCode = state.currencyCode)
                                }
                            }
                        }
                        ReportChartTab.TRENDS -> {
                            if (state.dailySpendings.isNotEmpty()) {
                                item {
                                    AppCard(modifier = Modifier.fillMaxWidth()) {
                                        Column(modifier = Modifier.padding(AppDimensions.paddingDefault)) {
                                            AppTypography.TitleMedium(text = stringResource(R.string.daily_income_vs_expenses))
                                            Spacer(modifier = Modifier.height(AppDimensions.paddingMedium))
                                            GroupedBarChart(
                                                groups = state.dailySpendings.map {
                                                    BarGroup(
                                                        label = it.date.formatDate("dd MMM"),
                                                        income = it.totalIncome,
                                                        expense = it.totalExpense
                                                    )
                                                },
                                                currencyCode = state.currencyCode
                                            )
                                        }
                                    }
                                }
                                item {
                                    AppCard(modifier = Modifier.fillMaxWidth()) {
                                        Column(modifier = Modifier.padding(AppDimensions.paddingDefault)) {
                                            AppTypography.TitleMedium(text = stringResource(R.string.expense_trend_line))
                                            Spacer(modifier = Modifier.height(AppDimensions.paddingMedium))
                                            LineChart(
                                                points = state.dailySpendings.map {
                                                    LinePoint(
                                                        label = it.date.formatDate("dd MMM"),
                                                        value = it.totalExpense
                                                    )
                                                },
                                                currencyCode = state.currencyCode
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        ReportChartTab.CASHFLOW -> {
                            item {
                                AppCard(modifier = Modifier.fillMaxWidth()) {
                                    Column(modifier = Modifier.padding(AppDimensions.paddingDefault)) {
                                        AppTypography.TitleMedium(text = "Cashflow Waterfall")
                                        Spacer(modifier = Modifier.height(AppDimensions.paddingMedium))
                                        WaterfallChart(
                                            items = buildWaterfallItems(state),
                                            currencyCode = state.currencyCode
                                        )
                                    }
                                }
                            }
                        }
                        ReportChartTab.RADAR -> {
                            item {
                                AppCard(modifier = Modifier.fillMaxWidth()) {
                                    Column(modifier = Modifier.padding(AppDimensions.paddingDefault)) {
                                        AppTypography.TitleMedium(text = "Category Radar")
                                        Spacer(modifier = Modifier.height(AppDimensions.paddingMedium))
                                        if (state.categorySpendings.size >= 3) {
                                            RadarSpendingChart(
                                                entries = state.categorySpendings.take(6).map {
                                                    RadarEntry(label = it.categoryName, value = it.totalAmount)
                                                },
                                                currencyCode = state.currencyCode
                                            )
                                        } else {
                                            AppTypography.BodyMedium(
                                                text = "Need at least 3 expense categories in this period for radar comparison.",
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun buildWaterfallItems(state: ReportsUiState): List<WaterfallItem> {
    val items = mutableListOf<WaterfallItem>()
    if (state.totalIncome > 0L) {
        items.add(WaterfallItem(label = "Income", amount = state.totalIncome, isPositive = true))
    }
    state.categorySpendings.take(4).forEach { cat ->
        items.add(
            WaterfallItem(
                label = cat.categoryName.take(8),
                amount = cat.totalAmount,
                isPositive = false
            )
        )
    }
    return items
}
