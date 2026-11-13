package com.example.expensetracker.domain.usecase

import com.example.expensetracker.domain.model.Budget
import com.example.expensetracker.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBudgetsUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository
) {
    operator fun invoke(): Flow<List<Budget>> = budgetRepository.getAllBudgets()

    suspend fun getBudgetById(id: Long): Budget? = budgetRepository.getBudgetById(id)
}

