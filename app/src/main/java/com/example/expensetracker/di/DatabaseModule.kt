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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        databaseProvider: Provider<ExpenseTrackerDatabase>
    ): ExpenseTrackerDatabase {
        return Room.databaseBuilder(
            context,
            ExpenseTrackerDatabase::class.java,
            "expense_tracker_db"
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    CoroutineScope(Dispatchers.IO).launch {
                        val database = databaseProvider.get()
                        database.categoryDao().insertCategories(DefaultData.defaultExpenseCategories)
                        database.categoryDao().insertCategories(DefaultData.defaultIncomeCategories)
                        for (account in DefaultData.defaultAccounts) {
                            database.accountDao().insertAccount(account)
                        }
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