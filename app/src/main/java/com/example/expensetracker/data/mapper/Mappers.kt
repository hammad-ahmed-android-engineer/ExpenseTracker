package com.example.expensetracker.data.mapper

import com.example.expensetracker.data.local.entity.*
import com.example.expensetracker.domain.model.*

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

fun CategoryEntity.toDomain(subcategories: List<SubcategoryEntity> = emptyList()): Category = Category(
    id = id,
    name = name,
    type = TransactionType.fromString(type),
    iconName = iconName,
    colorHex = colorHex,
    orderIndex = orderIndex,
    isDefault = isDefault,
    subcategories = subcategories.map { it.toDomain() }
)

fun SubcategoryEntity.toDomain(): Subcategory = Subcategory(
    id = id,
    categoryId = categoryId,
    name = name,
    iconName = iconName
)

fun Category.toEntity(): CategoryEntity = CategoryEntity(
    id = id,
    name = name,
    type = type.name,
    iconName = iconName,
    colorHex = colorHex,
    orderIndex = orderIndex,
    isDefault = isDefault
)

fun Subcategory.toEntity(): SubcategoryEntity = SubcategoryEntity(
    id = id,
    categoryId = categoryId,
    name = name,
    iconName = iconName
)

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