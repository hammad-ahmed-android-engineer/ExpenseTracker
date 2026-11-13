package com.example.expensetracker.data.local.dao

import androidx.room.*
import com.example.expensetracker.data.local.entity.BudgetCategoryEntity
import com.example.expensetracker.data.local.entity.BudgetEntity
import kotlinx.coroutines.flow.Flow

data class BudgetWithSpent(
    @Embedded val budget: BudgetEntity,
    val totalSpent: Long
)

@Dao
interface BudgetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: BudgetEntity): Long

    @Update
    suspend fun updateBudget(budget: BudgetEntity): Int

    @Delete
    suspend fun deleteBudget(budget: BudgetEntity): Int

    @Query("SELECT * FROM budgets WHERE id = :id")
    suspend fun getBudgetById(id: Long): BudgetEntity?

    @Query("SELECT * FROM budgets ORDER BY endDate DESC")
    fun getAllBudgetsFlow(): Flow<List<BudgetEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudgetCategories(budgetCategories: List<BudgetCategoryEntity>)

    @Query("DELETE FROM budget_categories WHERE budgetId = :budgetId")
    suspend fun deleteBudgetCategoriesForBudget(budgetId: Long): Int

    @Query("SELECT * FROM budget_categories WHERE budgetId = :budgetId")
    fun getBudgetCategoriesForBudgetFlow(budgetId: Long): Flow<List<BudgetCategoryEntity>>

    @Query("SELECT * FROM budget_categories WHERE budgetId = :budgetId")
    suspend fun getBudgetCategoriesForBudget(budgetId: Long): List<BudgetCategoryEntity>

    @Query("DELETE FROM budgets")
    suspend fun deleteAllBudgets(): Int

    @Query("DELETE FROM budget_categories")
    suspend fun deleteAllBudgetCategories(): Int
}