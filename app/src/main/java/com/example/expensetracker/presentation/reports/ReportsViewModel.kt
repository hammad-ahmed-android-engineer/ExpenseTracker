package com.example.expensetracker.presentation.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.core.common.endOfDayMillis
import com.example.expensetracker.core.common.endOfMonthMillis
import com.example.expensetracker.core.common.startOfDayMillis
import com.example.expensetracker.core.common.startOfMonthMillis
import com.example.expensetracker.domain.model.CategorySpendingSummary
import com.example.expensetracker.domain.model.DailySpendingSummary
import com.example.expensetracker.domain.repository.UserPreferencesRepository
import com.example.expensetracker.domain.usecase.GetReportsAnalyticsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar
import javax.inject.Inject

enum class DateFilterPeriod(val displayName: String) {
    THIS_WEEK("This Week"),
    LAST_7_DAYS("Last 7 Days"),
    THIS_MONTH("This Month"),
    LAST_MONTH("Last Month"),
    LAST_3_MONTHS("3 Months"),
    LAST_6_MONTHS("6 Months"),
    THIS_YEAR("This Year"),
    ALL_TIME("All Time")
}

enum class ReportChartTab(val displayName: String) {
    OVERVIEW("Overview"),
    TRENDS("Trends"),
    CASHFLOW("Cashflow"),
    RADAR("Radar")
}

data class ReportsUiState(
    val isLoading: Boolean = true,
    val selectedPeriod: DateFilterPeriod = DateFilterPeriod.THIS_MONTH,
    val selectedTab: ReportChartTab = ReportChartTab.OVERVIEW,
    val isPieMode: Boolean = false,
    val totalIncome: Long = 0L,
    val totalExpense: Long = 0L,
    val netSavings: Long = 0L,
    val categorySpendings: List<CategorySpendingSummary> = emptyList(),
    val dailySpendings: List<DailySpendingSummary> = emptyList(),
    val currencyCode: String = "USD"
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val getReportsAnalyticsUseCase: GetReportsAnalyticsUseCase,
    userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _selectedPeriod = MutableStateFlow(DateFilterPeriod.THIS_MONTH)
    private val _selectedTab = MutableStateFlow(ReportChartTab.OVERVIEW)
    private val _isPieMode = MutableStateFlow(false)

    private val analyticsFlow = _selectedPeriod.flatMapLatest { period ->
        val (start, end) = calculateDateRange(period)
        getReportsAnalyticsUseCase(start, end)
    }

    val uiState: StateFlow<ReportsUiState> = combine(
        analyticsFlow,
        _selectedPeriod,
        _selectedTab,
        _isPieMode,
        userPreferencesRepository.userPreferencesFlow
    ) { analytics, period, tab, isPie, prefs ->
        ReportsUiState(
            isLoading = false,
            selectedPeriod = period,
            selectedTab = tab,
            isPieMode = isPie,
            totalIncome = analytics.totalIncome,
            totalExpense = analytics.totalExpense,
            netSavings = analytics.netSavings,
            categorySpendings = analytics.categorySpendings,
            dailySpendings = analytics.dailySpendings,
            currencyCode = prefs.defaultCurrency
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ReportsUiState()
    )

    fun selectPeriod(period: DateFilterPeriod) {
        _selectedPeriod.value = period
    }

    fun selectTab(tab: ReportChartTab) {
        _selectedTab.value = tab
    }

    fun togglePieMode(isPie: Boolean) {
        _isPieMode.value = isPie
    }

    private fun calculateDateRange(period: DateFilterPeriod): Pair<Long, Long> {
        val now = Calendar.getInstance()
        val year = now.get(Calendar.YEAR)
        val month = now.get(Calendar.MONTH)

        return when (period) {
            DateFilterPeriod.THIS_WEEK -> {
                val start = (now.clone() as Calendar).apply {
                    set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
                }.timeInMillis.startOfDayMillis()
                start to System.currentTimeMillis().endOfDayMillis()
            }
            DateFilterPeriod.LAST_7_DAYS -> {
                val start = (now.clone() as Calendar).apply {
                    add(Calendar.DAY_OF_YEAR, -6)
                }.timeInMillis.startOfDayMillis()
                start to System.currentTimeMillis().endOfDayMillis()
            }
            DateFilterPeriod.THIS_MONTH -> month.startOfMonthMillis(year) to month.endOfMonthMillis(year)
            DateFilterPeriod.LAST_MONTH -> {
                val prev = (now.clone() as Calendar).apply { add(Calendar.MONTH, -1) }
                val pMonth = prev.get(Calendar.MONTH)
                val pYear = prev.get(Calendar.YEAR)
                pMonth.startOfMonthMillis(pYear) to pMonth.endOfMonthMillis(pYear)
            }
            DateFilterPeriod.LAST_3_MONTHS -> {
                val prev = (now.clone() as Calendar).apply { add(Calendar.MONTH, -3) }
                prev.timeInMillis.startOfDayMillis() to System.currentTimeMillis().endOfDayMillis()
            }
            DateFilterPeriod.LAST_6_MONTHS -> {
                val prev = (now.clone() as Calendar).apply { add(Calendar.MONTH, -6) }
                prev.timeInMillis.startOfDayMillis() to System.currentTimeMillis().endOfDayMillis()
            }
            DateFilterPeriod.THIS_YEAR -> 0.startOfMonthMillis(year) to 11.endOfMonthMillis(year)
            DateFilterPeriod.ALL_TIME -> 0L to (System.currentTimeMillis() + 315_360_000_000L)
        }
    }
}
