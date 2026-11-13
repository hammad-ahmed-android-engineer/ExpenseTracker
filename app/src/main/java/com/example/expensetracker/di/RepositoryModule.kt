package com.example.expensetracker.di

import com.example.expensetracker.data.datastore.UserPreferencesRepositoryImpl
import com.example.expensetracker.data.repository.AccountRepositoryImpl
import com.example.expensetracker.data.repository.BackupRepositoryImpl
import com.example.expensetracker.data.repository.BudgetRepositoryImpl
import com.example.expensetracker.data.repository.CategoryRepositoryImpl
import com.example.expensetracker.data.repository.RecurringTransactionRepositoryImpl
import com.example.expensetracker.data.repository.SavingsGoalRepositoryImpl
import com.example.expensetracker.data.repository.TransactionRepositoryImpl
import com.example.expensetracker.domain.repository.AccountRepository
import com.example.expensetracker.domain.repository.BackupRepository
import com.example.expensetracker.domain.repository.BudgetRepository
import com.example.expensetracker.domain.repository.CategoryRepository
import com.example.expensetracker.domain.repository.RecurringTransactionRepository
import com.example.expensetracker.domain.repository.SavingsGoalRepository
import com.example.expensetracker.domain.repository.TransactionRepository
import com.example.expensetracker.domain.repository.UserPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTransactionRepository(impl: TransactionRepositoryImpl): TransactionRepository

    @Binds
    @Singleton
    abstract fun bindAccountRepository(impl: AccountRepositoryImpl): AccountRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(impl: CategoryRepositoryImpl): CategoryRepository

    @Binds
    @Singleton
    abstract fun bindBudgetRepository(impl: BudgetRepositoryImpl): BudgetRepository

    @Binds
    @Singleton
    abstract fun bindSavingsGoalRepository(impl: SavingsGoalRepositoryImpl): SavingsGoalRepository

    @Binds
    @Singleton
    abstract fun bindRecurringTransactionRepository(impl: RecurringTransactionRepositoryImpl): RecurringTransactionRepository

    @Binds
    @Singleton
    abstract fun bindUserPreferencesRepository(impl: UserPreferencesRepositoryImpl): UserPreferencesRepository

    @Binds
    @Singleton
    abstract fun bindBackupRepository(impl: BackupRepositoryImpl): BackupRepository
}
