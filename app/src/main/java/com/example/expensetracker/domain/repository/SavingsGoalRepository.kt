package com.example.expensetracker.domain.repository

import com.example.expensetracker.domain.model.SavingsContribution
import com.example.expensetracker.domain.model.SavingsGoal
import kotlinx.coroutines.flow.Flow

interface SavingsGoalRepository {
    fun getAllGoals(): Flow<List<SavingsGoal>>
    suspend fun getGoalById(id: Long): SavingsGoal?
    suspend fun addGoal(goal: SavingsGoal): Long
    suspend fun updateGoal(goal: SavingsGoal)
    suspend fun deleteGoal(goal: SavingsGoal)
    suspend fun addContribution(goalId: Long, contribution: SavingsContribution): Long
    fun getContributionsForGoal(goalId: Long): Flow<List<SavingsContribution>>
}