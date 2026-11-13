package com.example.expensetracker.domain.usecase

import com.example.expensetracker.domain.model.SavingsGoal
import com.example.expensetracker.domain.repository.SavingsGoalRepository
import javax.inject.Inject

class DeleteSavingsGoalUseCase @Inject constructor(
    private val savingsGoalRepository: SavingsGoalRepository
) {
    suspend operator fun invoke(goal: SavingsGoal) {
        savingsGoalRepository.deleteGoal(goal)
    }
}

