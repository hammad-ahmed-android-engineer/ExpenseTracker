package com.example.expensetracker.presentation.savings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.core.common.toMinorUnits
import com.example.expensetracker.domain.model.SavingsContribution
import com.example.expensetracker.domain.model.SavingsGoal
import com.example.expensetracker.domain.repository.SavingsGoalRepository
import com.example.expensetracker.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
    val currencyCode: String = "USD"
)

@HiltViewModel
class SavingsViewModel @Inject constructor(
    private val savingsGoalRepository: SavingsGoalRepository,
    userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val uiState: StateFlow<SavingsUiState> = combine(
        savingsGoalRepository.getAllGoals(),
        userPreferencesRepository.userPreferencesFlow
    ) { goals, prefs ->
        SavingsUiState(
            isLoading = false,
            goals = goals,
            currencyCode = prefs.defaultCurrency
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
        if (target <= 0L) return

        val cal = Calendar.getInstance().apply { add(Calendar.YEAR, 1) }

        val goal = SavingsGoal(
            title = title.trim().ifEmpty { "Savings Goal" },
            targetAmount = target,
            currentAmount = 0L,
            targetDate = cal.timeInMillis,
            description = description?.trim()?.ifEmpty { null }
        )

        viewModelScope.launch {
            savingsGoalRepository.addGoal(goal)
        }
    }

    fun addContribution(goalId: Long, amountString: String, isDeposit: Boolean) {
        val amount = amountString.toMinorUnits()
        if (amount <= 0L) return

        val contribution = SavingsContribution(
            goalId = goalId,
            amount = amount,
            type = if (isDeposit) "DEPOSIT" else "WITHDRAWAL",
            date = System.currentTimeMillis(),
            note = if (isDeposit) "Deposit contribution" else "Withdrawal"
        )

        viewModelScope.launch {
            savingsGoalRepository.addContribution(goalId, contribution)
        }
    }

    fun deleteGoal(goal: SavingsGoal) {
        viewModelScope.launch {
            savingsGoalRepository.deleteGoal(goal)
        }
    }
}
