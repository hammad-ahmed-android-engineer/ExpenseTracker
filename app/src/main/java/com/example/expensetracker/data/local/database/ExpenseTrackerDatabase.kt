package com.example.expensetracker.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.expensetracker.data.local.dao.*
import com.example.expensetracker.data.local.entity.*

@Database(
    entities = [
        TransactionEntity::class,
        AccountEntity::class,
        CategoryEntity::class,
        SubcategoryEntity::class,
        BudgetEntity::class,
        BudgetCategoryEntity::class,
        SavingsGoalEntity::class,
        SavingsContributionEntity::class,
        RecurringTransactionEntity::class,
        TagEntity::class,
        TransactionTagCrossRefEntity::class,
        ExchangeRateEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ExpenseTrackerDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun accountDao(): AccountDao
    abstract fun categoryDao(): CategoryDao
    abstract fun budgetDao(): BudgetDao
    abstract fun savingsGoalDao(): SavingsGoalDao
    abstract fun recurringTransactionDao(): RecurringTransactionDao
    abstract fun tagDao(): TagDao
    abstract fun exchangeRateDao(): ExchangeRateDao
}