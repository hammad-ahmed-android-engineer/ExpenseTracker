package com.example.expensetracker.domain.usecase

import com.example.expensetracker.domain.model.Budget
import com.example.expensetracker.domain.repository.BudgetRepository
import javax.inject.Inject

class DeleteBudgetUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository
) {
    suspend operator fun invoke(budget: Budget) {
        budgetRepository.deleteBudget(budget)
    }
}

