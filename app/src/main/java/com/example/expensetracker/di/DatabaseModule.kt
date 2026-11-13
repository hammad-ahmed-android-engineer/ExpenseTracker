package com.example.expensetracker.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.expensetracker.data.local.dao.*
import com.example.expensetracker.data.local.database.DefaultData
import com.example.expensetracker.data.local.database.ExpenseTrackerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): ExpenseTrackerDatabase {
        return Room.databaseBuilder(
            context,
            ExpenseTrackerDatabase::class.java,
            "expense_tracker_db"
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    db.beginTransaction()
                    try {
                        for (cat in DefaultData.defaultExpenseCategories + DefaultData.defaultIncomeCategories) {
                            db.execSQL(
                                "INSERT OR IGNORE INTO categories (id, name, type, iconName, colorHex, orderIndex, isDefault) VALUES (?, ?, ?, ?, ?, ?, ?)",
                                arrayOf(cat.id, cat.name, cat.type, cat.iconName, cat.colorHex, cat.orderIndex, if (cat.isDefault) 1 else 0)
                            )
                        }
                        for (acc in DefaultData.defaultAccounts) {
                            db.execSQL(
                                "INSERT OR IGNORE INTO accounts (id, name, type, initialBalance, currentBalance, currency, colorHex, iconName, isArchived, createdAt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                                arrayOf(acc.id, acc.name, acc.type, acc.initialBalance, acc.currentBalance, acc.currency, acc.colorHex, acc.iconName, if (acc.isArchived) 1 else 0, acc.createdAt)
                            )
                        }
                        db.setTransactionSuccessful()
                    } finally {
                        db.endTransaction()
                    }
                }
            })
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideTransactionDao(db: ExpenseTrackerDatabase): TransactionDao = db.transactionDao()

    @Provides
    fun provideAccountDao(db: ExpenseTrackerDatabase): AccountDao = db.accountDao()

    @Provides
    fun provideCategoryDao(db: ExpenseTrackerDatabase): CategoryDao = db.categoryDao()

    @Provides
    fun provideBudgetDao(db: ExpenseTrackerDatabase): BudgetDao = db.budgetDao()

    @Provides
    fun provideSavingsGoalDao(db: ExpenseTrackerDatabase): SavingsGoalDao = db.savingsGoalDao()

    @Provides
    fun provideRecurringTransactionDao(db: ExpenseTrackerDatabase): RecurringTransactionDao = db.recurringTransactionDao()

    @Provides
    fun provideTagDao(db: ExpenseTrackerDatabase): TagDao = db.tagDao()

    @Provides
    fun provideExchangeRateDao(db: ExpenseTrackerDatabase): ExchangeRateDao = db.exchangeRateDao()
}
