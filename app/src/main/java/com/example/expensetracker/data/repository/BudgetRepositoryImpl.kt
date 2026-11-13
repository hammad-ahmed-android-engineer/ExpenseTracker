package com.example.expensetracker.data.repository

import com.example.expensetracker.data.local.dao.BudgetDao
import com.example.expensetracker.data.local.dao.CategoryDao
import com.example.expensetracker.data.local.dao.TransactionDao
import com.example.expensetracker.data.local.entity.BudgetCategoryEntity
import com.example.expensetracker.data.mapper.toDomain
import com.example.expensetracker.data.mapper.toEntity
import com.example.expensetracker.domain.model.Budget
import com.example.expensetracker.domain.model.CategoryLimit
import com.example.expensetracker.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BudgetRepositoryImpl @Inject constructor(
    private val budgetDao: BudgetDao,
    private val transactionDao: TransactionDao,
    private val categoryDao: CategoryDao
) : BudgetRepository {

    override fun getAllBudgets(): Flow<List<Budget>> =
        budgetDao.getAllBudgetsFlow().map { list ->
            list.map { budgetEntity ->
                val catLimitsEntities = budgetDao.getBudgetCategoriesForBudget(budgetEntity.id)
                val catLimits = catLimitsEntities.map { cl ->
                    val cat = categoryDao.getCategoryById(cl.categoryId)
                    val catSpent = transactionDao.getCategoryAmountByTypeAndDateRange(
                        type = "EXPENSE",
                        categoryId = cl.categoryId,
                        startDate = budgetEntity.startDate,
                        endDate = budgetEntity.endDate
                    )
                    CategoryLimit(
                        categoryId = cl.categoryId,
                        categoryName = cat?.name ?: "",
                        categoryColor = cat?.colorHex ?: "#10B981",
                        limitAmount = cl.categoryLimit,
                        spentAmount = catSpent
                    )
                }
                val spent = if (catLimits.isNotEmpty()) {
                    catLimits.sumOf { it.spentAmount }
                } else {
                    val allCats = categoryDao.getAllCategories()
                    val matchedCat = allCats.find { it.name.contains(budgetEntity.name, ignoreCase = true) || budgetEntity.name.contains(it.name, ignoreCase = true) }
                    if (matchedCat != null) {
                        transactionDao.getCategoryAmountByTypeAndDateRange(
                            type = "EXPENSE",
                            categoryId = matchedCat.id,
                            startDate = budgetEntity.startDate,
                            endDate = budgetEntity.endDate
                        )
                    } else {
                        transactionDao.getTotalAmountByTypeAndDateRange(
                            "EXPENSE",
                            budgetEntity.startDate,
                            budgetEntity.endDate
                        )
                    }
                }
                budgetEntity.toDomain(spent, catLimits)
            }
        }

    override suspend fun getBudgetById(id: Long): Budget? {
        val entity = budgetDao.getBudgetById(id) ?: return null
        val catLimitsEntities = budgetDao.getBudgetCategoriesForBudget(entity.id)
        val catLimits = catLimitsEntities.map { cl ->
            val cat = categoryDao.getCategoryById(cl.categoryId)
            val catSpent = transactionDao.getCategoryAmountByTypeAndDateRange(
                type = "EXPENSE",
                categoryId = cl.categoryId,
                startDate = entity.startDate,
                endDate = entity.endDate
            )
            CategoryLimit(
                categoryId = cl.categoryId,
                categoryName = cat?.name ?: "",
                categoryColor = cat?.colorHex ?: "#10B981",
                limitAmount = cl.categoryLimit,
                spentAmount = catSpent
            )
        }
        val spent = if (catLimits.isNotEmpty()) {
            catLimits.sumOf { it.spentAmount }
        } else {
            transactionDao.getTotalAmountByTypeAndDateRange("EXPENSE", entity.startDate, entity.endDate)
        }
        return entity.toDomain(spent, catLimits)
    }

    override suspend fun addBudget(budget: Budget, categoryLimits: List<CategoryLimit>): Long {
        val budgetId = budgetDao.insertBudget(budget.toEntity())
        if (categoryLimits.isNotEmpty()) {
            val entities = categoryLimits.map {
                BudgetCategoryEntity(budgetId = budgetId, categoryId = it.categoryId, categoryLimit = it.limitAmount)
            }
            budgetDao.insertBudgetCategories(entities)
        }
        return budgetId
    }

    override suspend fun updateBudget(budget: Budget, categoryLimits: List<CategoryLimit>) {
        budgetDao.updateBudget(budget.toEntity())
        budgetDao.deleteBudgetCategoriesForBudget(budget.id)
        if (categoryLimits.isNotEmpty()) {
            val entities = categoryLimits.map {
                BudgetCategoryEntity(budgetId = budget.id, categoryId = it.categoryId, categoryLimit = it.limitAmount)
            }
            budgetDao.insertBudgetCategories(entities)
        }
    }

    override suspend fun deleteBudget(budget: Budget) {
        budgetDao.deleteBudgetCategoriesForBudget(budget.id)
        budgetDao.deleteBudget(budget.toEntity())
    }
}
