package com.example.expensetracker.presentation.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.core.common.endOfMonthMillis
import com.example.expensetracker.core.common.startOfDayMillis
import com.example.expensetracker.core.common.startOfMonthMillis
import com.example.expensetracker.domain.model.Transaction
import com.example.expensetracker.domain.model.TransactionType
import com.example.expensetracker.domain.usecase.GetTransactionsByDateRangeUseCase
import com.example.expensetracker.domain.usecase.GetUserPreferencesUseCase
import com.example.expensetracker.presentation.components.charts.DailyHeatmapData
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

data class CalendarUiState(
    val isLoading: Boolean = true,
    val currentYear: Int = Calendar.getInstance().get(Calendar.YEAR),
    val currentMonthZeroBased: Int = Calendar.getInstance().get(Calendar.MONTH),
    val selectedDayOfMonth: Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
    val heatmapDays: List<DailyHeatmapData> = emptyList(),
    val selectedDateTransactions: List<Transaction> = emptyList(),
    val selectedDayIncome: Long = 0L,
    val selectedDayExpense: Long = 0L,
    val selectedDateMillis: Long = System.currentTimeMillis(),
    val currencyCode: String = "USD"
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getTransactionsByDateRangeUseCase: GetTransactionsByDateRangeUseCase,
    getUserPreferencesUseCase: GetUserPreferencesUseCase
) : ViewModel() {

    private val _currentYearMonth = MutableStateFlow(
        Calendar.getInstance().get(Calendar.YEAR) to Calendar.getInstance().get(Calendar.MONTH)
    )
    private val _selectedDate = MutableStateFlow(System.currentTimeMillis())

    private val monthTransactionsFlow = _currentYearMonth.flatMapLatest { (year, month) ->
        getTransactionsByDateRangeUseCase(
            month.startOfMonthMillis(year),
            month.endOfMonthMillis(year)
        )
    }

    val uiState: StateFlow<CalendarUiState> = combine(
        _currentYearMonth,
        _selectedDate,
        monthTransactionsFlow,
        getUserPreferencesUseCase()
    ) { (year, month), selectedDate, monthTxs, prefs ->
        val byDay = monthTxs.groupBy { it.date.startOfDayMillis() }
        val dailyExpenseMap = byDay.mapValues { (_, list) ->
            list.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
        }
        val maxExpense = dailyExpenseMap.values.maxOrNull()?.coerceAtLeast(100L) ?: 100L

        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, 1)
        }
        val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        val days = (1..daysInMonth).map { day ->
            val dayStart = (cal.clone() as Calendar).apply {
                set(Calendar.DAY_OF_MONTH, day)
            }.timeInMillis.startOfDayMillis()
            val expense = dailyExpenseMap[dayStart] ?: 0L
            val intensity = if (expense > 0) (expense.toFloat() / maxExpense.toFloat()).coerceIn(0.15f, 1.0f) else 0f
            DailyHeatmapData(dayOfMonth = day, amount = expense, intensity = intensity)
        }

        val selDayStart = selectedDate.startOfDayMillis()
        val selCal = Calendar.getInstance().apply { timeInMillis = selectedDate }
        val dayTxs = byDay[selDayStart].orEmpty()
        val dayIncome = dayTxs.filter { it.type == TransactionType.INCOME || it.type == TransactionType.REFUND }
            .sumOf { it.amount }
        val dayExpense = dayTxs.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }

        CalendarUiState(
            isLoading = false,
            currentYear = year,
            currentMonthZeroBased = month,
            selectedDayOfMonth = selCal.get(Calendar.DAY_OF_MONTH),
            heatmapDays = days,
            selectedDateTransactions = dayTxs,
            selectedDayIncome = dayIncome,
            selectedDayExpense = dayExpense,
            selectedDateMillis = selectedDate,
            currencyCode = prefs.defaultCurrency
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CalendarUiState()
    )

    fun onDateSelect(dayOfMonth: Int) {
        val (year, month) = _currentYearMonth.value
        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }
        _selectedDate.value = cal.timeInMillis
    }

    fun onPreviousMonth() {
        shiftMonth(-1)
    }

    fun onNextMonth() {
        shiftMonth(1)
    }

    private fun shiftMonth(delta: Int) {
        val (year, month) = _currentYearMonth.value
        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, 1)
            add(Calendar.MONTH, delta)
        }
        _currentYearMonth.value = cal.get(Calendar.YEAR) to cal.get(Calendar.MONTH)
        _selectedDate.value = cal.timeInMillis
    }
}
