package com.example.expensetracker.presentation.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.core.common.endOfDayMillis
import com.example.expensetracker.core.common.endOfMonthMillis
import com.example.expensetracker.core.common.formatDate
import com.example.expensetracker.core.common.startOfDayMillis
import com.example.expensetracker.core.common.startOfMonthMillis
import com.example.expensetracker.domain.model.CategorySpendingSummary
import com.example.expensetracker.domain.model.DailySpendingSummary
import com.example.expensetracker.domain.model.Transaction
import com.example.expensetracker.domain.model.TransactionType
import com.example.expensetracker.domain.usecase.GetReportsAnalyticsUseCase
import com.example.expensetracker.domain.usecase.GetUserPreferencesUseCase
import com.example.expensetracker.domain.usecase.ReportsAnalyticsData
import com.example.expensetracker.presentation.components.charts.BarGroup
import com.example.expensetracker.presentation.components.charts.DailyHeatmapData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar
import javax.inject.Inject

enum class DateFilterPeriod(val displayName: String) {
    DAILY("Daily"),
    WEEKLY("Weekly"),
    MONTHLY("Monthly"),
    QUARTERLY("Quarterly"),
    YEARLY("Yearly"),
    ALL_TIME("All Time")
}

enum class ReportChartTab(val displayName: String) {
    OVERVIEW("Overview"),
    TRENDS("Trends"),
    CASHFLOW("Cashflow"),
    RADAR("Radar")
}

data class DateRangeInfo(
    val start: Long,
    val end: Long,
    val label: String
)

data class ReportsUiState(
    val isLoading: Boolean = true,
    val selectedPeriod: DateFilterPeriod = DateFilterPeriod.MONTHLY,
    val periodOffset: Int = 0,
    val dateRangeLabel: String = "Monthly",
    val selectedTab: ReportChartTab = ReportChartTab.OVERVIEW,
    val isPieMode: Boolean = false,
    val totalIncome: Long = 0L,
    val totalExpense: Long = 0L,
    val netSavings: Long = 0L,
    val categorySpendings: List<CategorySpendingSummary> = emptyList(),
    val dailySpendings: List<DailySpendingSummary> = emptyList(),
    val trendGroups: List<BarGroup> = emptyList(),
    val heatmapDays: List<DailyHeatmapData> = emptyList(),
    val maxDailySpending: Long = 0L,
    val highestSpendingDayText: String = "",
    val highestSpendingAmount: Long = 0L,
    val currencyCode: String = "USD"
)

data class HeatmapComputationResult(
    val days: List<DailyHeatmapData>,
    val maxDailySpending: Long,
    val highestDayText: String,
    val highestDayAmount: Long
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val getReportsAnalyticsUseCase: GetReportsAnalyticsUseCase,
    getUserPreferencesUseCase: GetUserPreferencesUseCase
) : ViewModel() {

    private val _selectedPeriod = MutableStateFlow<DateFilterPeriod>(DateFilterPeriod.MONTHLY)
    private val _periodOffset = MutableStateFlow<Int>(0)
    private val _selectedTab = MutableStateFlow<ReportChartTab>(ReportChartTab.OVERVIEW)
    private val _isPieMode = MutableStateFlow<Boolean>(false)

    private data class FilterParams(
        val period: DateFilterPeriod,
        val offset: Int,
        val tab: ReportChartTab,
        val isPie: Boolean
    )

    private val filterParamsFlow: Flow<FilterParams> = combine(
        _selectedPeriod,
        _periodOffset,
        _selectedTab,
        _isPieMode
    ) { period, offset, tab, isPie ->
        FilterParams(period, offset, tab, isPie)
    }

    private val dateRangeFlow: Flow<DateRangeInfo> = combine(
        _selectedPeriod,
        _periodOffset
    ) { period, offset ->
        calculateDateRange(period, offset)
    }

    private val analyticsFlow: Flow<Pair<DateRangeInfo, ReportsAnalyticsData>> = dateRangeFlow.flatMapLatest { range ->
        getReportsAnalyticsUseCase(range.start, range.end).combine(
            kotlinx.coroutines.flow.flowOf(range)
        ) { data, r -> r to data }
    }

    val uiState: StateFlow<ReportsUiState> = combine(
        analyticsFlow,
        filterParamsFlow,
        getUserPreferencesUseCase()
    ) { (range, analytics), filter, prefs ->
        val groups = computeTrendGroups(filter.period, analytics.transactions, range)
        val heatmapResult = computeHeatmapDays(filter.period, analytics.transactions, range)

        ReportsUiState(
            isLoading = false,
            selectedPeriod = filter.period,
            periodOffset = filter.offset,
            dateRangeLabel = range.label,
            selectedTab = filter.tab,
            isPieMode = filter.isPie,
            totalIncome = analytics.totalIncome,
            totalExpense = analytics.totalExpense,
            netSavings = analytics.netSavings,
            categorySpendings = analytics.categorySpendings,
            dailySpendings = analytics.dailySpendings,
            trendGroups = groups,
            heatmapDays = heatmapResult.days,
            maxDailySpending = heatmapResult.maxDailySpending,
            highestSpendingDayText = heatmapResult.highestDayText,
            highestSpendingAmount = heatmapResult.highestDayAmount,
            currencyCode = prefs.defaultCurrency
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ReportsUiState()
    )

    fun selectPeriod(period: DateFilterPeriod) {
        _selectedPeriod.value = period
        _periodOffset.value = 0
    }

    fun previousPeriod() {
        _periodOffset.value -= 1
    }

    fun nextPeriod() {
        _periodOffset.value += 1
    }

    fun resetPeriod() {
        _periodOffset.value = 0
    }

    fun selectTab(tab: ReportChartTab) {
        _selectedTab.value = tab
    }

    fun togglePieMode(isPie: Boolean) {
        _isPieMode.value = isPie
    }

    private fun calculateDateRange(period: DateFilterPeriod, offset: Int): DateRangeInfo {
        val now = Calendar.getInstance()

        return when (period) {
            DateFilterPeriod.DAILY -> {
                val c = (now.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, offset) }
                val start = c.timeInMillis.startOfDayMillis()
                val end = c.timeInMillis.endOfDayMillis()
                val label = if (offset == 0) "Today (${c.timeInMillis.formatDate("MMM d")})" else c.timeInMillis.formatDate("MMM d, yyyy")
                DateRangeInfo(start, end, label)
            }
            DateFilterPeriod.WEEKLY -> {
                val c = (now.clone() as Calendar).apply {
                    add(Calendar.WEEK_OF_YEAR, offset)
                    set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
                }
                val start = c.timeInMillis.startOfDayMillis()
                val endCal = (c.clone() as Calendar).apply { add(Calendar.DAY_OF_WEEK, 6) }
                val end = endCal.timeInMillis.endOfDayMillis()
                val label = "${start.formatDate("MMM d")} – ${end.formatDate("MMM d, yyyy")}"
                DateRangeInfo(start, end, label)
            }
            DateFilterPeriod.MONTHLY -> {
                val c = (now.clone() as Calendar).apply { add(Calendar.MONTH, offset) }
                val month = c.get(Calendar.MONTH)
                val year = c.get(Calendar.YEAR)
                val start = month.startOfMonthMillis(year)
                val end = month.endOfMonthMillis(year)
                val label = c.timeInMillis.formatDate("MMMM yyyy")
                DateRangeInfo(start, end, label)
            }
            DateFilterPeriod.QUARTERLY -> {
                val c = (now.clone() as Calendar).apply { add(Calendar.MONTH, offset * 3) }
                val currentMonth = c.get(Calendar.MONTH)
                val qIndex = currentMonth / 3
                val year = c.get(Calendar.YEAR)
                val start = (qIndex * 3).startOfMonthMillis(year)
                val end = (qIndex * 3 + 2).endOfMonthMillis(year)
                val qNames = listOf("Q1 (Jan–Mar)", "Q2 (Apr–Jun)", "Q3 (Jul–Sep)", "Q4 (Oct–Dec)")
                val label = "${qNames[qIndex]} $year"
                DateRangeInfo(start, end, label)
            }
            DateFilterPeriod.YEARLY -> {
                val c = (now.clone() as Calendar).apply { add(Calendar.YEAR, offset) }
                val year = c.get(Calendar.YEAR)
                val start = 0.startOfMonthMillis(year)
                val end = 11.endOfMonthMillis(year)
                val label = "Year $year"
                DateRangeInfo(start, end, label)
            }
            DateFilterPeriod.ALL_TIME -> {
                val start = 0L
                val end = System.currentTimeMillis() + (86400000L * 365L)
                DateRangeInfo(start, end, "All Time Records")
            }
        }
    }

    private fun computeTrendGroups(
        period: DateFilterPeriod,
        transactions: List<Transaction>,
        dateRange: DateRangeInfo
    ): List<BarGroup> {
        return when (period) {
            DateFilterPeriod.DAILY -> {
                val slots = listOf("00:00", "04:00", "08:00", "12:00", "16:00", "20:00")
                slots.mapIndexed { idx, slotLabel ->
                    val startHour = idx * 4
                    val endHour = startHour + 4
                    val inSlot = transactions.filter {
                        val cal = Calendar.getInstance().apply { timeInMillis = it.date }
                        val h = cal.get(Calendar.HOUR_OF_DAY)
                        h in startHour until endHour
                    }
                    val inc = inSlot.filter { it.type == TransactionType.INCOME || it.type == TransactionType.REFUND }.sumOf { it.amount }
                    val exp = inSlot.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
                    BarGroup(
                        label = slotLabel,
                        income = inc,
                        expense = exp
                    )
                }
            }
            DateFilterPeriod.WEEKLY -> {
                val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                val c = Calendar.getInstance().apply { timeInMillis = dateRange.start }
                days.mapIndexed { idx, dayName ->
                    val dayStart = (c.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, idx) }.timeInMillis.startOfDayMillis()
                    val dayEnd = dayStart.endOfDayMillis()
                    val dayTxs = transactions.filter { it.date in dayStart..dayEnd }
                    val inc = dayTxs.filter { it.type == TransactionType.INCOME || it.type == TransactionType.REFUND }.sumOf { it.amount }
                    val exp = dayTxs.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
                    BarGroup(
                        label = dayName,
                        income = inc,
                        expense = exp
                    )
                }
            }
            DateFilterPeriod.MONTHLY -> {
                val cal = Calendar.getInstance().apply { timeInMillis = dateRange.start }
                val maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
                val weekBuckets = mutableListOf<Pair<String, IntRange>>()
                weekBuckets.add("W1 (1-7)" to 1..7)
                weekBuckets.add("W2 (8-14)" to 8..14)
                weekBuckets.add("W3 (15-21)" to 15..21)
                weekBuckets.add("W4 (22-28)" to 22..28)
                if (maxDay > 28) {
                    weekBuckets.add("W5 (29-$maxDay)" to 29..maxDay)
                }
                weekBuckets.map { (label, range) ->
                    val inWeek = transactions.filter {
                        val c = Calendar.getInstance().apply { timeInMillis = it.date }
                        c.get(Calendar.DAY_OF_MONTH) in range
                    }
                    val inc = inWeek.filter { it.type == TransactionType.INCOME || it.type == TransactionType.REFUND }.sumOf { it.amount }
                    val exp = inWeek.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
                    BarGroup(
                        label = label,
                        income = inc,
                        expense = exp
                    )
                }
            }
            DateFilterPeriod.QUARTERLY -> {
                val cal = Calendar.getInstance().apply { timeInMillis = dateRange.start }
                (0..2).map { offsetMonth ->
                    val mCal = (cal.clone() as Calendar).apply { add(Calendar.MONTH, offsetMonth) }
                    val m = mCal.get(Calendar.MONTH)
                    val y = mCal.get(Calendar.YEAR)
                    val mStart = m.startOfMonthMillis(y)
                    val mEnd = m.endOfMonthMillis(y)
                    val mTxs = transactions.filter { it.date in mStart..mEnd }
                    val inc = mTxs.filter { it.type == TransactionType.INCOME || it.type == TransactionType.REFUND }.sumOf { it.amount }
                    val exp = mTxs.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
                    BarGroup(
                        label = mCal.timeInMillis.formatDate("MMM"),
                        income = inc,
                        expense = exp
                    )
                }
            }
            DateFilterPeriod.YEARLY -> {
                val cal = Calendar.getInstance().apply { timeInMillis = dateRange.start }
                val year = cal.get(Calendar.YEAR)
                (0..11).map { monthIdx ->
                    val mStart = monthIdx.startOfMonthMillis(year)
                    val mEnd = monthIdx.endOfMonthMillis(year)
                    val mTxs = transactions.filter { it.date in mStart..mEnd }
                    val inc = mTxs.filter { it.type == TransactionType.INCOME || it.type == TransactionType.REFUND }.sumOf { it.amount }
                    val exp = mTxs.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
                    val mCal = (cal.clone() as Calendar).apply { set(Calendar.MONTH, monthIdx) }
                    BarGroup(
                        label = mCal.timeInMillis.formatDate("MMM"),
                        income = inc,
                        expense = exp
                    )
                }
            }
            DateFilterPeriod.ALL_TIME -> {
                val cal = Calendar.getInstance()
                val currentYear = cal.get(Calendar.YEAR)
                (4 downTo 0).map { yOffset ->
                    val targetYear = currentYear - yOffset
                    val yStart = 0.startOfMonthMillis(targetYear)
                    val yEnd = 11.endOfMonthMillis(targetYear)
                    val yTxs = transactions.filter { it.date in yStart..yEnd }
                    val inc = yTxs.filter { it.type == TransactionType.INCOME || it.type == TransactionType.REFUND }.sumOf { it.amount }
                    val exp = yTxs.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
                    BarGroup(
                        label = targetYear.toString(),
                        income = inc,
                        expense = exp
                    )
                }
            }
        }
    }

    private fun computeHeatmapDays(
        period: DateFilterPeriod,
        transactions: List<Transaction>,
        dateRange: DateRangeInfo
    ): HeatmapComputationResult {
        val startCal = Calendar.getInstance().apply { timeInMillis = dateRange.start }
        val endCal = Calendar.getInstance().apply { timeInMillis = dateRange.end }

        // Start grid from the Monday on or before startCal
        val gridStartCal = (startCal.clone() as Calendar).apply {
            val dow = get(Calendar.DAY_OF_WEEK)
            val daysBack = if (dow == Calendar.SUNDAY) 6 else dow - Calendar.MONDAY
            add(Calendar.DAY_OF_YEAR, -daysBack)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // End grid on the Sunday on or after endCal
        val gridEndCal = (endCal.clone() as Calendar).apply {
            val dow = get(Calendar.DAY_OF_WEEK)
            val daysForward = if (dow == Calendar.SUNDAY) 0 else (Calendar.SATURDAY - dow + 1) % 7
            add(Calendar.DAY_OF_YEAR, daysForward)
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }

        val expensesByDay = transactions
            .filter { it.type == TransactionType.EXPENSE }
            .groupBy { it.date.startOfDayMillis() }

        val tempDays = mutableListOf<Triple<Long, Long, Int>>()
        val walkCal = gridStartCal.clone() as Calendar

        val maxDaysAllowed = 120
        var counter = 0
        while (walkCal.timeInMillis <= gridEndCal.timeInMillis && counter < maxDaysAllowed) {
            val dayStart = walkCal.timeInMillis.startOfDayMillis()
            val txs = expensesByDay[dayStart].orEmpty()
            val total = txs.sumOf { it.amount }
            tempDays.add(Triple(dayStart, total, txs.size))
            walkCal.add(Calendar.DAY_OF_YEAR, 1)
            counter++
        }

        val maxExpense = tempDays.maxOfOrNull { it.second }?.coerceAtLeast(1L) ?: 1L
        val inRangeExpenses = tempDays.filter { it.first in dateRange.start..dateRange.end }
        val realMaxDay = inRangeExpenses.filter { it.second > 0L }.maxByOrNull { it.second }
            ?: tempDays.filter { it.second > 0L }.maxByOrNull { it.second }

        val heatmapDays = tempDays.map { (dayStart, amount, txCount) ->
            val c = Calendar.getInstance().apply { timeInMillis = dayStart }
            val dowJava = c.get(Calendar.DAY_OF_WEEK)
            val dow = if (dowJava == Calendar.SUNDAY) 7 else dowJava - 1
            val inRange = dayStart in dateRange.start..dateRange.end
            val intensity = if (amount > 0L) {
                (amount.toFloat() / maxExpense.toFloat()).coerceIn(0.15f, 1.0f)
            } else {
                0.0f
            }
            DailyHeatmapData(
                dayOfMonth = c.get(Calendar.DAY_OF_MONTH),
                amount = amount,
                intensity = intensity,
                dateMillis = dayStart,
                formattedDate = dayStart.formatDate("EEE, MMM d, yyyy"),
                dayOfWeek = dow,
                transactionCount = txCount,
                isInSelectedPeriod = inRange
            )
        }

        val highestDayText = realMaxDay?.let { it.first.formatDate("MMM d") } ?: "No spending recorded"
        val highestDayAmount = realMaxDay?.second ?: 0L

        return HeatmapComputationResult(
            days = heatmapDays,
            maxDailySpending = highestDayAmount,
            highestDayText = highestDayText,
            highestDayAmount = highestDayAmount
        )
    }
}

