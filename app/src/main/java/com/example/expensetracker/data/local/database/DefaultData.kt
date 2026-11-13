package com.example.expensetracker.data.local.database

import com.example.expensetracker.data.local.entity.AccountEntity
import com.example.expensetracker.data.local.entity.CategoryEntity

object DefaultData {

    val defaultExpenseCategories = listOf(
        CategoryEntity(name = "Food & Dining", type = "EXPENSE", iconName = "Restaurant", colorHex = "#FF5722", orderIndex = 1, isDefault = true),
        CategoryEntity(name = "Groceries", type = "EXPENSE", iconName = "ShoppingCart", colorHex = "#4CAF50", orderIndex = 2, isDefault = true),
        CategoryEntity(name = "Transportation", type = "EXPENSE", iconName = "DirectionsCar", colorHex = "#2196F3", orderIndex = 3, isDefault = true),
        CategoryEntity(name = "Shopping", type = "EXPENSE", iconName = "ShoppingBag", colorHex = "#9C27B0", orderIndex = 4, isDefault = true),
        CategoryEntity(name = "Entertainment", type = "EXPENSE", iconName = "Movie", colorHex = "#E91E63", orderIndex = 5, isDefault = true),
        CategoryEntity(name = "Healthcare", type = "EXPENSE", iconName = "LocalHospital", colorHex = "#F44336", orderIndex = 6, isDefault = true),
        CategoryEntity(name = "Education", type = "EXPENSE", iconName = "School", colorHex = "#3F51B5", orderIndex = 7, isDefault = true),
        CategoryEntity(name = "Housing & Rent", type = "EXPENSE", iconName = "Home", colorHex = "#795548", orderIndex = 8, isDefault = true),
        CategoryEntity(name = "Utilities", type = "EXPENSE", iconName = "FlashOn", colorHex = "#FF9800", orderIndex = 9, isDefault = true),
        CategoryEntity(name = "Travel", type = "EXPENSE", iconName = "Flight", colorHex = "#00BCD4", orderIndex = 10, isDefault = true),
        CategoryEntity(name = "Subscriptions", type = "EXPENSE", iconName = "Subscriptions", colorHex = "#673AB7", orderIndex = 11, isDefault = true),
        CategoryEntity(name = "Personal Care", type = "EXPENSE", iconName = "SelfImprovement", colorHex = "#009688", orderIndex = 12, isDefault = true),
        CategoryEntity(name = "Insurance", type = "EXPENSE", iconName = "Security", colorHex = "#607D8B", orderIndex = 13, isDefault = true),
        CategoryEntity(name = "Gifts & Donations", type = "EXPENSE", iconName = "CardGiftcard", colorHex = "#EC407A", orderIndex = 14, isDefault = true),
        CategoryEntity(name = "Bills & Payments", type = "EXPENSE", iconName = "ReceiptLong", colorHex = "#FFC107", orderIndex = 15, isDefault = true),
        CategoryEntity(name = "Other Expenses", type = "EXPENSE", iconName = "MoreHoriz", colorHex = "#9E9E9E", orderIndex = 16, isDefault = true)
    )

    val defaultIncomeCategories = listOf(
        CategoryEntity(name = "Salary", type = "INCOME", iconName = "Payments", colorHex = "#2E7D32", orderIndex = 1, isDefault = true),
        CategoryEntity(name = "Freelancing", type = "INCOME", iconName = "Work", colorHex = "#1565C0", orderIndex = 2, isDefault = true),
        CategoryEntity(name = "Business", type = "INCOME", iconName = "Store", colorHex = "#6A1B9A", orderIndex = 3, isDefault = true),
        CategoryEntity(name = "Investments", type = "INCOME", iconName = "TrendingUp", colorHex = "#283593", orderIndex = 4, isDefault = true),
        CategoryEntity(name = "Rental Income", type = "INCOME", iconName = "Apartment", colorHex = "#4E342E", orderIndex = 5, isDefault = true),
        CategoryEntity(name = "Bonuses", type = "INCOME", iconName = "Redeem", colorHex = "#D84315", orderIndex = 6, isDefault = true),
        CategoryEntity(name = "Gifts", type = "INCOME", iconName = "CardGiftcard", colorHex = "#AD1457", orderIndex = 7, isDefault = true),
        CategoryEntity(name = "Other Income", type = "INCOME", iconName = "AccountBalanceWallet", colorHex = "#37474F", orderIndex = 8, isDefault = true)
    )

    val defaultAccounts = listOf(
        AccountEntity(
            name = "Cash Wallet",
            type = "CASH",
            initialBalance = 10000L, // $100.00
            currentBalance = 10000L,
            currency = "USD",
            colorHex = "#4CAF50",
            iconName = "Payments"
        ),
        AccountEntity(
            name = "Main Bank Account",
            type = "BANK_ACCOUNT",
            initialBalance = 250000L, // $2500.00
            currentBalance = 250000L,
            currency = "USD",
            colorHex = "#2196F3",
            iconName = "AccountBalance"
        )
    )
}