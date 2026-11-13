package com.example.expensetracker.domain.usecase

import com.example.expensetracker.domain.model.SavingsGoal
import com.example.expensetracker.domain.repository.SavingsGoalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSavingsGoalsUseCase @Inject constructor(
    private val savingsGoalRepository: SavingsGoalRepository
) {
    operator fun invoke(): Flow<List<SavingsGoal>> = savingsGoalRepository.getAllGoals()

    suspend fun getGoalById(id: Long): SavingsGoal? = savingsGoalRepository.getGoalById(id)
}

