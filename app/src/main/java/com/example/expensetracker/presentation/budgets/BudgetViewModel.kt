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
import com.example.expensetracker.domain.model.SavingsContribution
import com.example.expensetracker.domain.model.SavingsGoal
import com.example.expensetracker.domain.usecase.AddSavingsContributionUseCase
import com.example.expensetracker.domain.usecase.AddSavingsGoalUseCase
import com.example.expensetracker.domain.usecase.DeleteBudgetUseCase
import com.example.expensetracker.domain.usecase.DeleteSavingsGoalUseCase
import com.example.expensetracker.domain.usecase.GetBudgetsUseCase
import com.example.expensetracker.domain.usecase.GetCategoriesUseCase
import com.example.expensetracker.domain.usecase.GetSavingsGoalsUseCase
import com.example.expensetracker.domain.usecase.GetUserPreferencesUseCase
import com.example.expensetracker.domain.usecase.SaveBudgetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
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
    val savingsGoals: List<SavingsGoal> = emptyList(),
    val expenseCategories: List<Category> = emptyList(),
    val currencyCode: String = "USD",
    val message: String? = null
)

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val getBudgetsUseCase: GetBudgetsUseCase,
    private val saveBudgetUseCase: SaveBudgetUseCase,
    private val deleteBudgetUseCase: DeleteBudgetUseCase,
    private val getSavingsGoalsUseCase: GetSavingsGoalsUseCase,
    private val addSavingsGoalUseCase: AddSavingsGoalUseCase,
    private val addSavingsContributionUseCase: AddSavingsContributionUseCase,
    private val deleteSavingsGoalUseCase: DeleteSavingsGoalUseCase,
    getCategoriesUseCase: GetCategoriesUseCase,
    getUserPreferencesUseCase: GetUserPreferencesUseCase
) : ViewModel() {

    private val _message = MutableStateFlow<String?>(null)

    val uiState: StateFlow<BudgetsUiState> = combine(
        getBudgetsUseCase(),
        getSavingsGoalsUseCase(),
        getCategoriesUseCase.getByType(TransactionType.EXPENSE),
        getUserPreferencesUseCase(),
        _message
    ) { bList, goals, cats, prefs, msg ->
        BudgetsUiState(
            isLoading = false,
            budgets = bList,
            savingsGoals = goals,
            expenseCategories = cats,
            currencyCode = prefs.defaultCurrency,
            message = msg
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
        if (limit <= 0L) {
            _message.value = "Please enter a valid budget limit"
            return
        }

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
            saveBudgetUseCase(budget, catLimits)
        }
    }

    fun deleteBudget(budget: Budget) {
        viewModelScope.launch {
            deleteBudgetUseCase(budget)
        }
    }

    fun addSavingsGoal(
        title: String,
        targetAmountString: String,
        targetDate: Long = System.currentTimeMillis() + 86400000L * 180L,
        iconName: String = "Savings",
        colorHex: String = "#2DD4BF",
        description: String? = null,
        initialDepositString: String? = null
    ) {
        val target = targetAmountString.toMinorUnits()
        if (target <= 0L) {
            _message.value = "Please enter a valid target amount"
            return
        }
        val initialDeposit = initialDepositString?.toMinorUnits() ?: 0L
        viewModelScope.launch {
            val goalId = addSavingsGoalUseCase(
                SavingsGoal(
                    title = title.trim(),
                    targetAmount = target,
                    currentAmount = initialDeposit,
                    targetDate = targetDate,
                    iconName = iconName,
                    colorHex = colorHex,
                    description = description
                )
            )
            if (initialDeposit > 0L) {
                addSavingsContributionUseCase(
                    goalId = goalId,
                    contribution = SavingsContribution(
                        goalId = goalId,
                        amount = initialDeposit,
                        type = "DEPOSIT",
                        date = System.currentTimeMillis(),
                        note = "Initial deposit"
                    )
                )
            }
        }
    }

    fun deleteSavingsGoal(goal: SavingsGoal) {
        viewModelScope.launch {
            deleteSavingsGoalUseCase(goal)
        }
    }

    fun addFundsToGoal(goalId: Long, amount: Long, note: String = "Added funds") {
        if (amount <= 0) {
            _message.value = "Please enter a valid amount"
            return
        }
        viewModelScope.launch {
            addSavingsContributionUseCase(
                goalId = goalId,
                contribution = SavingsContribution(
                    goalId = goalId,
                    amount = amount,
                    type = "DEPOSIT",
                    date = System.currentTimeMillis(),
                    note = note
                )
            )
        }
    }

    fun addFundsToGoal(goalId: Long, amountString: String, note: String = "Added funds") {
        addFundsToGoal(goalId, amountString.toMinorUnits(), note)
    }

    fun clearMessage() {
        _message.value = null
    }
}
