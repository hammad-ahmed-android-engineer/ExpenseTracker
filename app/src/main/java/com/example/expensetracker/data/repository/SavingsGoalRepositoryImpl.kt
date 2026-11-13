package com.example.expensetracker.data.repository

import com.example.expensetracker.data.local.dao.SavingsGoalDao
import com.example.expensetracker.data.local.entity.SavingsContributionEntity
import com.example.expensetracker.data.mapper.toDomain
import com.example.expensetracker.data.mapper.toEntity
import com.example.expensetracker.domain.model.SavingsContribution
import com.example.expensetracker.domain.model.SavingsGoal
import com.example.expensetracker.domain.repository.SavingsGoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SavingsGoalRepositoryImpl @Inject constructor(
    private val savingsGoalDao: SavingsGoalDao
) : SavingsGoalRepository {

    override fun getAllGoals(): Flow<List<SavingsGoal>> {
        return savingsGoalDao.getAllGoalsFlow().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getGoalById(id: Long): SavingsGoal? {
        return savingsGoalDao.getGoalById(id)?.toDomain()
    }

    override suspend fun addGoal(goal: SavingsGoal): Long {
        return savingsGoalDao.insertGoal(goal.toEntity())
    }

    override suspend fun updateGoal(goal: SavingsGoal) {
        savingsGoalDao.updateGoal(goal.toEntity())
    }

    override suspend fun deleteGoal(goal: SavingsGoal) {
        savingsGoalDao.deleteGoal(goal.toEntity())
    }

    override suspend fun addContribution(goalId: Long, contribution: SavingsContribution): Long {
        val id = savingsGoalDao.insertContribution(contribution.toEntity())
        val delta = if (contribution.type == "DEPOSIT") contribution.amount else -contribution.amount
        savingsGoalDao.updateGoalCurrentAmount(goalId, delta)

        // Check completion status
        val goal = savingsGoalDao.getGoalById(goalId)
        if (goal != null && goal.currentAmount >= goal.targetAmount && !goal.isCompleted) {
            savingsGoalDao.updateGoal(goal.copy(isCompleted = true))
        }
        return id
    }

    override fun getContributionsForGoal(goalId: Long): Flow<List<SavingsContribution>> {
        return savingsGoalDao.getContributionsForGoalFlow(goalId).map { list -> list.map { it.toDomain() } }
    }
}