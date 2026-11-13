package com.example.expensetracker.domain.usecase

import com.example.expensetracker.domain.model.SavingsGoal
import com.example.expensetracker.domain.repository.SavingsGoalRepository
import javax.inject.Inject

class AddSavingsGoalUseCase @Inject constructor(
    private val savingsGoalRepository: SavingsGoalRepository
) {
    suspend operator fun invoke(goal: SavingsGoal): Long {
        return savingsGoalRepository.addGoal(goal)
    }

    suspend fun updateGoal(goal: SavingsGoal) {
        savingsGoalRepository.updateGoal(goal)
    }
}

