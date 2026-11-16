package com.example.expensetracker.data.mapper

import com.example.expensetracker.data.local.entity.account.AccountEntity
import com.example.expensetracker.data.local.entity.category.CategoryEntity
import com.example.expensetracker.data.local.entity.recurring.RecurringTransactionEntity
import com.example.expensetracker.domain.model.recurring.RecurringFrequency
import com.example.expensetracker.domain.model.recurring.RecurringTransaction
import com.example.expensetracker.domain.model.transaction.TransactionType

fun RecurringTransactionEntity.toDomain(
    category: CategoryEntity? = null,
    account: AccountEntity? = null,
    destAccount: AccountEntity? = null
): RecurringTransaction = RecurringTransaction(
    id = id,
    title = title,
    amount = amount,
    type = TransactionType.fromString(type),
    categoryId = categoryId,
    categoryName = category?.name ?: "",
    categoryIcon = category?.iconName ?: "Category",
    categoryColor = category?.colorHex ?: "#4CAF50",
    accountId = accountId,
    accountName = account?.name ?: "",
    destinationAccountId = destinationAccountId,
    destinationAccountName = destAccount?.name,
    frequency = RecurringFrequency.fromString(frequency),
    intervalDays = intervalDays,
    startDate = startDate,
    endDate = endDate,
    lastExecutedDate = lastExecutedDate,
    nextDueDate = nextDueDate,
    isEnabled = isEnabled,
    note = note
)

fun RecurringTransaction.toEntity(): RecurringTransactionEntity = RecurringTransactionEntity(
    id = id,
    title = title,
    amount = amount,
    type = type.name,
    categoryId = categoryId,
    accountId = accountId,
    destinationAccountId = destinationAccountId,
    frequency = frequency.name,
    intervalDays = intervalDays,
    startDate = startDate,
    endDate = endDate,
    lastExecutedDate = lastExecutedDate,
    nextDueDate = nextDueDate,
    isEnabled = isEnabled,
    note = note
)
