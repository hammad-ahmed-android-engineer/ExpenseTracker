package com.example.expensetracker.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.domain.model.Account
import com.example.expensetracker.domain.model.DashboardWidgetType
import com.example.expensetracker.domain.model.FinancialInsight
import com.example.expensetracker.domain.model.FinancialSummary
import com.example.expensetracker.domain.model.Transaction
import com.example.expensetracker.domain.model.UserPreferences
import com.example.expensetracker.domain.usecase.GenerateFinancialInsightsUseCase
import com.example.expensetracker.domain.usecase.GetAccountsUseCase
import com.example.expensetracker.domain.usecase.GetAllTransactionsUseCase
import com.example.expensetracker.domain.usecase.GetDashboardSummaryUseCase
import com.example.expensetracker.domain.usecase.GetUserPreferencesUseCase
import com.example.expensetracker.domain.usecase.UpdateUserPreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val isLoading: Boolean = true,
    val summary: FinancialSummary = FinancialSummary(),
    val accounts: List<Account> = emptyList(),
    val recentTransactions: List<Transaction> = emptyList(),
    val insights: List<FinancialInsight> = emptyList(),
    val preferences: UserPreferences = UserPreferences()
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    getDashboardSummaryUseCase: GetDashboardSummaryUseCase,
    generateFinancialInsightsUseCase: GenerateFinancialInsightsUseCase,
    getAccountsUseCase: GetAccountsUseCase,
    getAllTransactionsUseCase: GetAllTransactionsUseCase,
    getUserPreferencesUseCase: GetUserPreferencesUseCase,
    private val updateUserPreferencesUseCase: UpdateUserPreferencesUseCase
) : ViewModel() {

    val uiState: StateFlow<DashboardUiState> = combine(
        getDashboardSummaryUseCase(),
        getAccountsUseCase.getActiveAccounts(),
        getAllTransactionsUseCase(),
        generateFinancialInsightsUseCase(),
        getUserPreferencesUseCase()
    ) { summary, accounts, transactions, insights, prefs ->
        DashboardUiState(
            isLoading = false,
            summary = summary,
            accounts = accounts,
            recentTransactions = transactions.take(6),
            insights = insights,
            preferences = prefs
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState()
    )

    fun toggleWidgetVisibility(widget: DashboardWidgetType) {
        viewModelScope.launch {
            val prefs = uiState.value.preferences
            val hidden = prefs.hiddenDashboardWidgets
                .split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .toMutableSet()
            if (widget.key in hidden) hidden.remove(widget.key) else hidden.add(widget.key)
            updateUserPreferencesUseCase.updateHiddenDashboardWidgets(hidden.joinToString(","))
        }
    }

    fun moveWidget(widget: DashboardWidgetType, moveUp: Boolean) {
        viewModelScope.launch {
            val ordered = uiState.value.preferences.orderedWidgets().toMutableList()
            val index = ordered.indexOf(widget)
            if (index == -1) return@launch
            val target = if (moveUp) index - 1 else index + 1
            if (target !in ordered.indices) return@launch
            ordered.removeAt(index)
            ordered.add(target, widget)
            updateUserPreferencesUseCase.updateDashboardWidgetOrder(ordered.joinToString(",") { it.key })
        }
    }
}
