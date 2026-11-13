package com.example.expensetracker.domain.usecase

import com.example.expensetracker.domain.model.Budget
import com.example.expensetracker.domain.model.CategoryLimit
import com.example.expensetracker.domain.repository.BudgetRepository
import javax.inject.Inject

class SaveBudgetUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository
) {
    suspend operator fun invoke(budget: Budget, categoryLimits: List<CategoryLimit> = emptyList()): Long {
        return budgetRepository.addBudget(budget, categoryLimits)
    }

    suspend fun updateBudget(budget: Budget, categoryLimits: List<CategoryLimit> = emptyList()) {
        budgetRepository.updateBudget(budget, categoryLimits)
    }
}

