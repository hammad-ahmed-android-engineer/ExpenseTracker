package com.example.expensetracker.data.mapper

import com.example.expensetracker.data.local.entity.savings.SavingsContributionEntity
import com.example.expensetracker.data.local.entity.savings.SavingsGoalEntity
import com.example.expensetracker.domain.model.savings.SavingsContribution
import com.example.expensetracker.domain.model.savings.SavingsGoal

fun SavingsGoalEntity.toDomain(contributions: List<SavingsContributionEntity> = emptyList()): SavingsGoal = SavingsGoal(
    id = id,
    title = title,
    targetAmount = targetAmount,
    currentAmount = currentAmount,
    targetDate = targetDate,
    iconName = iconName,
    colorHex = colorHex,
    description = description,
    linkedAccountId = linkedAccountId,
    isCompleted = isCompleted,
    contributions = contributions.map { it.toDomain() },
    createdAt = createdAt
)

fun SavingsGoal.toEntity(): SavingsGoalEntity = SavingsGoalEntity(
    id = id,
    title = title,
    targetAmount = targetAmount,
    currentAmount = currentAmount,
    targetDate = targetDate,
    iconName = iconName,
    colorHex = colorHex,
    description = description,
    linkedAccountId = linkedAccountId,
    isCompleted = isCompleted,
    createdAt = createdAt
)

fun SavingsContributionEntity.toDomain(): SavingsContribution = SavingsContribution(
    id = id,
    goalId = goalId,
    amount = amount,
    type = type,
    date = date,
    note = note
)

fun SavingsContribution.toEntity(): SavingsContributionEntity = SavingsContributionEntity(
    id = id,
    goalId = goalId,
    amount = amount,
    type = type,
    date = date,
    note = note
)
