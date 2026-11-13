package com.example.expensetracker.domain.repository

import com.example.expensetracker.domain.model.Budget
import com.example.expensetracker.domain.model.CategoryLimit
import kotlinx.coroutines.flow.Flow

interface BudgetRepository {
    fun getAllBudgets(): Flow<List<Budget>>
    suspend fun getBudgetById(id: Long): Budget?
    suspend fun addBudget(budget: Budget, categoryLimits: List<CategoryLimit>): Long
    suspend fun updateBudget(budget: Budget, categoryLimits: List<CategoryLimit>)
    suspend fun deleteBudget(budget: Budget)
}