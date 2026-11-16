package com.example.expensetracker.data.mapper

import com.example.expensetracker.data.local.entity.account.AccountEntity
import com.example.expensetracker.domain.model.account.Account
import com.example.expensetracker.domain.model.account.AccountType

fun AccountEntity.toDomain(): Account = Account(
    id = id,
    name = name,
    type = AccountType.fromString(type),
    initialBalance = initialBalance,
    currentBalance = currentBalance,
    currency = currency,
    colorHex = colorHex,
    iconName = iconName,
    isArchived = isArchived,
    createdAt = createdAt
)

fun Account.toEntity(): AccountEntity = AccountEntity(
    id = id,
    name = name,
    type = type.name,
    initialBalance = initialBalance,
    currentBalance = currentBalance,
    currency = currency,
    colorHex = colorHex,
    iconName = iconName,
    isArchived = isArchived,
    createdAt = createdAt
)
