package com.example.expensetracker.presentation.calendar

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import com.example.expensetracker.R
import com.example.expensetracker.core.common.formatCurrencyCompact
import com.example.expensetracker.core.common.formatDate
import com.example.expensetracker.core.common.formatMonthYear
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppBadge
import com.example.expensetracker.core.designsystem.components.AppCard
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppIconButton
import com.example.expensetracker.core.designsystem.components.AppScaffold
import com.example.expensetracker.core.designsystem.components.AppTopAppBar
import com.example.expensetracker.core.designsystem.theme.semanticColors
import com.example.expensetracker.presentation.calendar.components.CalendarTransactionRow
import com.example.expensetracker.presentation.components.charts.HeatmapCalendar
import java.util.Calendar

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val colors = semanticColors
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val monthName = remember(state.currentYear, state.currentMonthZeroBased) {
        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, state.currentYear)
            set(Calendar.MONTH, state.currentMonthZeroBased)
            set(Calendar.DAY_OF_MONTH, 1)
        }
        cal.timeInMillis.formatMonthYear()
    }

    AppScaffold(
        topBar = {
            AppTopAppBar(
                title = { AppTypography.TitleLarge(text = stringResource(R.string.financial_calendar)) },
                navigationIcon = {
                    AppIconButton(onClick = onNavigateBack) {
                        AppIcon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    AppIconButton(onClick = { viewModel.onPreviousMonth() }) {
                        AppIcon(imageVector = Icons.Default.ChevronLeft, contentDescription = stringResource(R.string.prev_month))
                    }
                    AppIconButton(onClick = { viewModel.onNextMonth() }) {
                        AppIcon(imageVector = Icons.Default.ChevronRight, contentDescription = stringResource(R.string.next_month))
                    }
                }
            )
        }
    ) { padding ->
        if (isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = AppDimensions.paddingDefault),
                horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingDefault)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxHeight(),
                    contentPadding = PaddingValues(vertical = AppDimensions.paddingSmall)
                ) {
                    item {
                        AppCard(modifier = Modifier.fillMaxWidth()) {
                            HeatmapCalendar(
                                monthName = monthName,
                                days = state.heatmapDays,
                                selectedDayOfMonth = state.selectedDayOfMonth,
                                onDayClick = { day -> viewModel.onDateSelect(day.dayOfMonth) }
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxHeight()
                        .padding(vertical = AppDimensions.paddingSmall)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppTypography.TitleMedium(text = state.selectedDateMillis.formatDate("MMM dd, yyyy"))
                        Row(horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingMicro)) {
                            if (state.selectedDayIncome > 0) {
                                AppBadge(
                                    text = "+${state.selectedDayIncome.formatCurrencyCompact(state.currencyCode)}",
                                    containerColor = colors.incomeContainer,
                                    contentColor = colors.income
                                )
                            }
                            if (state.selectedDayExpense > 0) {
                                AppBadge(
                                    text = "-${state.selectedDayExpense.formatCurrencyCompact(state.currencyCode)}",
                                    containerColor = colors.expenseContainer,
                                    contentColor = colors.expense
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))

                    if (state.selectedDateTransactions.isEmpty()) {
                        AppTypography.BodyMedium(
                            text = stringResource(R.string.no_transactions_day),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)) {
                            items(state.selectedDateTransactions, key = { it.id }) { tx ->
                                CalendarTransactionRow(tx = tx, currencyCode = state.currencyCode)
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
                    AppCard(modifier = Modifier.fillMaxWidth()) {
                        HeatmapCalendar(
                            monthName = monthName,
                            days = state.heatmapDays,
                            selectedDayOfMonth = state.selectedDayOfMonth,
                            onDayClick = { day -> viewModel.onDateSelect(day.dayOfMonth) }
                        )
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppTypography.TitleMedium(text = state.selectedDateMillis.formatDate("MMM dd, yyyy"))
                        Row(horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingMicro)) {
                            if (state.selectedDayIncome > 0) {
                                AppBadge(
                                    text = "+${state.selectedDayIncome.formatCurrencyCompact(state.currencyCode)}",
                                    containerColor = colors.incomeContainer,
                                    contentColor = colors.income
                                )
                            }
                            if (state.selectedDayExpense > 0) {
                                AppBadge(
                                    text = "-${state.selectedDayExpense.formatCurrencyCompact(state.currencyCode)}",
                                    containerColor = colors.expenseContainer,
                                    contentColor = colors.expense
                                )
                            }
                        }
                    }
                }

                if (state.selectedDateTransactions.isEmpty()) {
                    item {
                        AppTypography.BodyMedium(
                            text = stringResource(R.string.no_transactions_day),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    items(state.selectedDateTransactions, key = { it.id }) { tx ->
                        CalendarTransactionRow(tx = tx, currencyCode = state.currencyCode)
                    }
                }
            }
        }
    }
}
