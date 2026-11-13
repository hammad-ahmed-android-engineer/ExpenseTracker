package com.example.expensetracker.presentation.insights

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.domain.model.FinancialInsight
import com.example.expensetracker.domain.usecase.GenerateFinancialInsightsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class InsightsViewModel @Inject constructor(
    generateFinancialInsightsUseCase: GenerateFinancialInsightsUseCase
) : ViewModel() {

    val insightsState: StateFlow<List<FinancialInsight>> = generateFinancialInsightsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}