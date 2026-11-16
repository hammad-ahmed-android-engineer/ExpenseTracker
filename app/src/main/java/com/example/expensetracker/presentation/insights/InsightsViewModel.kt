package com.example.expensetracker.presentation.insights

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.domain.model.FinancialInsight
import com.example.expensetracker.domain.usecase.GenerateFinancialInsightsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class InsightsUiState(
    val isLoading: Boolean = true,
    val insights: List<FinancialInsight> = emptyList()
)

@HiltViewModel
class InsightsViewModel @Inject constructor(
    generateFinancialInsightsUseCase: GenerateFinancialInsightsUseCase
) : ViewModel() {

    val uiState: StateFlow<InsightsUiState> = generateFinancialInsightsUseCase()
        .map { list ->
            InsightsUiState(
                isLoading = false,
                insights = list
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = InsightsUiState()
        )
}
