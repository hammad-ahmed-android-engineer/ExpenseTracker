package com.example.expensetracker.presentation.savings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.core.common.toMinorUnits
import com.example.expensetracker.domain.model.SavingsContribution
import com.example.expensetracker.domain.model.SavingsGoal
import com.example.expensetracker.domain.usecase.AddSavingsContributionUseCase
import com.example.expensetracker.domain.usecase.AddSavingsGoalUseCase
import com.example.expensetracker.domain.usecase.DeleteSavingsGoalUseCase
import com.example.expensetracker.domain.usecase.GetSavingsGoalsUseCase
import com.example.expensetracker.domain.usecase.GetUserPreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

data class SavingsUiState(
    val isLoading: Boolean = true,
    val goals: List<SavingsGoal> = emptyList(),
    val currencyCode: String = "USD",
    val message: String? = null
)

@HiltViewModel
class SavingsViewModel @Inject constructor(
    private val getSavingsGoalsUseCase: GetSavingsGoalsUseCase,
    private val addSavingsGoalUseCase: AddSavingsGoalUseCase,
    private val addSavingsContributionUseCase: AddSavingsContributionUseCase,
    private val deleteSavingsGoalUseCase: DeleteSavingsGoalUseCase,
    getUserPreferencesUseCase: GetUserPreferencesUseCase
) : ViewModel() {

    private val _message = MutableStateFlow<String?>(null)

    val uiState: StateFlow<SavingsUiState> = combine(
        getSavingsGoalsUseCase(),
        getUserPreferencesUseCase(),
        _message
    ) { goals, prefs, msg ->
        SavingsUiState(
            isLoading = false,
            goals = goals,
            currencyCode = prefs.defaultCurrency,
            message = msg
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SavingsUiState()
    )

    fun addGoal(
        title: String,
        targetAmountString: String,
        description: String? = null
    ) {
        val target = targetAmountString.toMinorUnits()
        if (target <= 0L) {
            _message.value = "Please enter a valid target amount"
            return
        }

        val cal = Calendar.getInstance().apply { add(Calendar.YEAR, 1) }

        val goal = SavingsGoal(
            title = title.trim().ifEmpty { "Savings Goal" },
            targetAmount = target,
            currentAmount = 0L,
            targetDate = cal.timeInMillis,
            description = description?.trim()?.ifEmpty { null }
        )

        viewModelScope.launch {
            addSavingsGoalUseCase(goal)
        }
    }

    fun addContribution(goalId: Long, amountString: String, isDeposit: Boolean) {
        val amount = amountString.toMinorUnits()
        if (amount <= 0L) {
            _message.value = "Please enter a valid amount"
            return
        }

        val contribution = SavingsContribution(
            goalId = goalId,
            amount = amount,
            type = if (isDeposit) "DEPOSIT" else "WITHDRAWAL",
            date = System.currentTimeMillis(),
            note = if (isDeposit) "Deposit contribution" else "Withdrawal"
        )

        viewModelScope.launch {
            addSavingsContributionUseCase(goalId, contribution)
        }
    }

    fun deleteGoal(goal: SavingsGoal) {
        viewModelScope.launch {
            deleteSavingsGoalUseCase(goal)
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}
