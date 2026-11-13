package com.example.expensetracker.presentation.budgets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.core.common.endOfMonthMillis
import com.example.expensetracker.core.common.startOfMonthMillis
import com.example.expensetracker.core.common.toMinorUnits
import com.example.expensetracker.domain.model.Budget
import com.example.expensetracker.domain.model.BudgetPeriodType
import com.example.expensetracker.domain.model.Category
import com.example.expensetracker.domain.model.CategoryLimit
import com.example.expensetracker.domain.model.TransactionType
import com.example.expensetracker.domain.repository.BudgetRepository
import com.example.expensetracker.domain.repository.CategoryRepository
import com.example.expensetracker.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

data class BudgetsUiState(
    val isLoading: Boolean = true,
    val budgets: List<Budget> = emptyList(),
    val expenseCategories: List<Category> = emptyList(),
    val currencyCode: String = "USD"
)

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val budgetRepository: BudgetRepository,
    categoryRepository: CategoryRepository,
    userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val uiState: StateFlow<BudgetsUiState> = combine(
        budgetRepository.getAllBudgets(),
        categoryRepository.getCategoriesByType(TransactionType.EXPENSE),
        userPreferencesRepository.userPreferencesFlow
    ) { bList, cats, prefs ->
        BudgetsUiState(
            isLoading = false,
            budgets = bList,
            expenseCategories = cats,
            currencyCode = prefs.defaultCurrency
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BudgetsUiState()
    )

    fun addBudget(
        name: String,
        amountLimitString: String,
        periodType: BudgetPeriodType = BudgetPeriodType.MONTHLY,
        notifyThreshold: Int = 80,
        isRolloverEnabled: Boolean = false,
        category: Category? = null
    ) {
        val limit = amountLimitString.toMinorUnits()
        if (limit <= 0L) return

        val now = Calendar.getInstance()
        val year = now.get(Calendar.YEAR)
        val month = now.get(Calendar.MONTH)

        val (startDate, endDate) = when (periodType) {
            BudgetPeriodType.WEEKLY -> {
                val startCal = (now.clone() as Calendar).apply {
                    set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                val endCal = (startCal.clone() as Calendar).apply {
                    add(Calendar.DAY_OF_YEAR, 6)
                    set(Calendar.HOUR_OF_DAY, 23)
                    set(Calendar.MINUTE, 59)
                    set(Calendar.SECOND, 59)
                }
                startCal.timeInMillis to endCal.timeInMillis
            }
            BudgetPeriodType.YEARLY -> {
                0.startOfMonthMillis(year) to 11.endOfMonthMillis(year)
            }
            else -> {
                month.startOfMonthMillis(year) to month.endOfMonthMillis(year)
            }
        }

        val budget = Budget(
            name = name.trim().ifEmpty { category?.name ?: "${periodType.displayName} Budget" },
            amountLimit = limit,
            periodType = periodType,
            startDate = startDate,
            endDate = endDate,
            isRolloverEnabled = isRolloverEnabled,
            notifyThreshold = notifyThreshold
        )

        val catLimits = if (category != null) {
            listOf(
                CategoryLimit(
                    categoryId = category.id,
                    categoryName = category.name,
                    categoryColor = category.colorHex,
                    limitAmount = limit,
                    spentAmount = 0L
                )
            )
        } else emptyList()

        viewModelScope.launch {
            budgetRepository.addBudget(budget, catLimits)
        }
    }

    fun deleteBudget(budget: Budget) {
        viewModelScope.launch {
            budgetRepository.deleteBudget(budget)
        }
    }
}
