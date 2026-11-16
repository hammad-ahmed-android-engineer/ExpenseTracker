package com.example.expensetracker.data.mapper

import com.example.expensetracker.data.local.entity.budget.BudgetEntity
import com.example.expensetracker.domain.model.budget.Budget
import com.example.expensetracker.domain.model.budget.BudgetPeriodType
import com.example.expensetracker.domain.model.budget.CategoryLimit

fun BudgetEntity.toDomain(
    spent: Long = 0L,
    categoryLimits: List<CategoryLimit> = emptyList()
): Budget = Budget(
    id = id,
    name = name,
    amountLimit = amountLimit,
    spentAmount = spent,
    periodType = BudgetPeriodType.fromString(periodType),
    startDate = startDate,
    endDate = endDate,
    isRolloverEnabled = isRolloverEnabled,
    notifyThreshold = notifyThreshold,
    categoryLimits = categoryLimits,
    createdAt = createdAt
)

fun Budget.toEntity(): BudgetEntity = BudgetEntity(
    id = id,
    name = name,
    amountLimit = amountLimit,
    periodType = periodType.name,
    startDate = startDate,
    endDate = endDate,
    isRolloverEnabled = isRolloverEnabled,
    notifyThreshold = notifyThreshold,
    createdAt = createdAt
)
