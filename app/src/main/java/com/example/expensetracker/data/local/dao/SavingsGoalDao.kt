package com.example.expensetracker.data.local.dao

import androidx.room.*
import com.example.expensetracker.data.local.entity.SavingsContributionEntity
import com.example.expensetracker.data.local.entity.SavingsGoalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavingsGoalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: SavingsGoalEntity): Long

    @Update
    suspend fun updateGoal(goal: SavingsGoalEntity): Int

    @Delete
    suspend fun deleteGoal(goal: SavingsGoalEntity): Int

    @Query("SELECT * FROM savings_goals WHERE id = :id")
    suspend fun getGoalById(id: Long): SavingsGoalEntity?

    @Query("SELECT * FROM savings_goals ORDER BY isCompleted ASC, targetDate ASC")
    fun getAllGoalsFlow(): Flow<List<SavingsGoalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContribution(contribution: SavingsContributionEntity): Long

    @Query("SELECT * FROM savings_contributions WHERE goalId = :goalId ORDER BY date DESC")
    fun getContributionsForGoalFlow(goalId: Long): Flow<List<SavingsContributionEntity>>

    @Query("UPDATE savings_goals SET currentAmount = currentAmount + :delta WHERE id = :goalId")
    suspend fun updateGoalCurrentAmount(goalId: Long, delta: Long): Int
}