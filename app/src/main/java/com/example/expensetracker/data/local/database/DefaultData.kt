package com.example.expensetracker.data.local.database

import com.example.expensetracker.data.local.entity.AccountEntity
import com.example.expensetracker.data.local.entity.CategoryEntity

object DefaultData {

    val defaultExpenseCategories = listOf(
        CategoryEntity(id = 1L, name = "Food & Dining", type = "EXPENSE", iconName = "Restaurant", colorHex = "#FF5722", orderIndex = 1, isDefault = true),
        CategoryEntity(id = 2L, name = "Groceries", type = "EXPENSE", iconName = "ShoppingCart", colorHex = "#4CAF50", orderIndex = 2, isDefault = true),
        CategoryEntity(id = 3L, name = "Transportation", type = "EXPENSE", iconName = "DirectionsCar", colorHex = "#2196F3", orderIndex = 3, isDefault = true),
        CategoryEntity(id = 4L, name = "Shopping", type = "EXPENSE", iconName = "ShoppingBag", colorHex = "#9C27B0", orderIndex = 4, isDefault = true),
        CategoryEntity(id = 5L, name = "Entertainment", type = "EXPENSE", iconName = "Movie", colorHex = "#E91E63", orderIndex = 5, isDefault = true),
        CategoryEntity(id = 6L, name = "Healthcare", type = "EXPENSE", iconName = "LocalHospital", colorHex = "#F44336", orderIndex = 6, isDefault = true),
        CategoryEntity(id = 7L, name = "Education", type = "EXPENSE", iconName = "School", colorHex = "#3F51B5", orderIndex = 7, isDefault = true),
        CategoryEntity(id = 8L, name = "Housing & Rent", type = "EXPENSE", iconName = "Home", colorHex = "#795548", orderIndex = 8, isDefault = true),
        CategoryEntity(id = 9L, name = "Utilities", type = "EXPENSE", iconName = "FlashOn", colorHex = "#FF9800", orderIndex = 9, isDefault = true),
        CategoryEntity(id = 10L, name = "Travel", type = "EXPENSE", iconName = "Flight", colorHex = "#00BCD4", orderIndex = 10, isDefault = true),
        CategoryEntity(id = 11L, name = "Subscriptions", type = "EXPENSE", iconName = "Subscriptions", colorHex = "#673AB7", orderIndex = 11, isDefault = true),
        CategoryEntity(id = 12L, name = "Personal Care", type = "EXPENSE", iconName = "SelfImprovement", colorHex = "#009688", orderIndex = 12, isDefault = true),
        CategoryEntity(id = 13L, name = "Insurance", type = "EXPENSE", iconName = "Security", colorHex = "#607D8B", orderIndex = 13, isDefault = true),
        CategoryEntity(id = 14L, name = "Gifts & Donations", type = "EXPENSE", iconName = "CardGiftcard", colorHex = "#EC407A", orderIndex = 14, isDefault = true),
        CategoryEntity(id = 15L, name = "Bills & Payments", type = "EXPENSE", iconName = "ReceiptLong", colorHex = "#FFC107", orderIndex = 15, isDefault = true),
        CategoryEntity(id = 16L, name = "Other Expenses", type = "EXPENSE", iconName = "MoreHoriz", colorHex = "#9E9E9E", orderIndex = 16, isDefault = true)
    )

    val defaultIncomeCategories = listOf(
        CategoryEntity(id = 17L, name = "Salary", type = "INCOME", iconName = "Payments", colorHex = "#2E7D32", orderIndex = 1, isDefault = true),
        CategoryEntity(id = 18L, name = "Freelancing", type = "INCOME", iconName = "Work", colorHex = "#1565C0", orderIndex = 2, isDefault = true),
        CategoryEntity(id = 19L, name = "Business", type = "INCOME", iconName = "Store", colorHex = "#6A1B9A", orderIndex = 3, isDefault = true),
        CategoryEntity(id = 20L, name = "Investments", type = "INCOME", iconName = "TrendingUp", colorHex = "#283593", orderIndex = 4, isDefault = true),
        CategoryEntity(id = 21L, name = "Rental Income", type = "INCOME", iconName = "Apartment", colorHex = "#4E342E", orderIndex = 5, isDefault = true),
        CategoryEntity(id = 22L, name = "Bonuses", type = "INCOME", iconName = "Redeem", colorHex = "#D84315", orderIndex = 6, isDefault = true),
        CategoryEntity(id = 23L, name = "Gifts", type = "INCOME", iconName = "CardGiftcard", colorHex = "#AD1457", orderIndex = 7, isDefault = true),
        CategoryEntity(id = 24L, name = "Other Income", type = "INCOME", iconName = "AccountBalanceWallet", colorHex = "#37474F", orderIndex = 8, isDefault = true)
    )

    val defaultAccounts = listOf(
        AccountEntity(
            id = 1L,
            name = "Checking",
            type = "BANK_ACCOUNT",
            initialBalance = 284050L,
            currentBalance = 284050L,
            currency = "USD",
            colorHex = "#3B4371",
            iconName = "CreditCard"
        ),
        AccountEntity(
            id = 2L,
            name = "Cash",
            type = "CASH",
            initialBalance = 254050L,
            currentBalance = 254050L,
            currency = "USD",
            colorHex = "#1E5E4E",
            iconName = "Payments"
        ),
        AccountEntity(
            id = 3L,
            name = "Savings",
            type = "SAVINGS_ACCOUNT",
            initialBalance = 1020000L,
            currentBalance = 1020000L,
            currency = "USD",
            colorHex = "#1D6A5D",
            iconName = "AccountBalance"
        ),
        AccountEntity(
            id = 4L,
            name = "Credit cards",
            type = "CREDIT_CARD",
            initialBalance = 1020000L,
            currentBalance = 1020000L,
            currency = "USD",
            colorHex = "#9E432A",
            iconName = "CreditCard"
        )
    )
}