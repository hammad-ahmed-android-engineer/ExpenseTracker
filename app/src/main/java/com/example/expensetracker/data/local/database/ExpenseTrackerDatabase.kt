package com.example.expensetracker.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.expensetracker.data.local.dao.*
import com.example.expensetracker.data.local.entity.account.AccountEntity
import com.example.expensetracker.data.local.entity.budget.BudgetCategoryEntity
import com.example.expensetracker.data.local.entity.budget.BudgetEntity
import com.example.expensetracker.data.local.entity.category.CategoryEntity
import com.example.expensetracker.data.local.entity.category.SubcategoryEntity
import com.example.expensetracker.data.local.entity.exchange.ExchangeRateEntity
import com.example.expensetracker.data.local.entity.recurring.RecurringTransactionEntity
import com.example.expensetracker.data.local.entity.savings.SavingsContributionEntity
import com.example.expensetracker.data.local.entity.savings.SavingsGoalEntity
import com.example.expensetracker.data.local.entity.transaction.TagEntity
import com.example.expensetracker.data.local.entity.transaction.TransactionEntity
import com.example.expensetracker.data.local.entity.transaction.TransactionTagCrossRefEntity

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