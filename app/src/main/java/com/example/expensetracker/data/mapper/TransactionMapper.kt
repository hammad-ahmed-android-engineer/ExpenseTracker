package com.example.expensetracker.data.mapper

import com.example.expensetracker.data.local.entity.account.AccountEntity
import com.example.expensetracker.data.local.entity.category.CategoryEntity
import com.example.expensetracker.data.local.entity.transaction.TransactionEntity
import com.example.expensetracker.domain.model.transaction.Transaction
import com.example.expensetracker.domain.model.transaction.TransactionType

fun TransactionEntity.toDomain(
    category: CategoryEntity? = null,
    account: AccountEntity? = null,
    destAccount: AccountEntity? = null,
    tags: List<String> = emptyList()
): Transaction = Transaction(
    id = id,
    title = title,
    amount = amount,
    type = TransactionType.fromString(type),
    categoryId = categoryId,
    categoryName = category?.name ?: "",
    categoryIcon = category?.iconName ?: "Category",
    categoryColor = category?.colorHex ?: "#4CAF50",
    subcategoryId = subcategoryId,
    accountId = accountId,
    accountName = account?.name ?: "",
    destinationAccountId = destinationAccountId,
    destinationAccountName = destAccount?.name,
    date = date,
    paymentMethod = paymentMethod,
    note = note,
    location = location,
    isRecurring = isRecurring,
    recurringId = recurringId,
    attachmentPath = attachmentPath,
    tags = tags,
    originalAmount = originalAmount,
    originalCurrency = originalCurrency,
    exchangeRate = exchangeRate,
    createdAt = createdAt
)

fun Transaction.toEntity(): TransactionEntity = TransactionEntity(
    id = id,
    title = title,
    amount = amount,
    type = type.name,
    categoryId = categoryId,
    subcategoryId = subcategoryId,
    accountId = accountId,
    destinationAccountId = destinationAccountId,
    date = date,
    paymentMethod = paymentMethod,
    note = note,
    location = location,
    isRecurring = isRecurring,
    recurringId = recurringId,
    attachmentPath = attachmentPath,
    originalAmount = originalAmount,
    originalCurrency = originalCurrency,
    exchangeRate = exchangeRate,
    createdAt = createdAt
)
