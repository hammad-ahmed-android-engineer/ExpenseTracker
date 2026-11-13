package com.example.expensetracker.data.local.database

import androidx.room.withTransaction
import com.example.expensetracker.core.common.endOfMonthMillis
import com.example.expensetracker.core.common.startOfMonthMillis
import com.example.expensetracker.data.local.entity.BudgetEntity
import com.example.expensetracker.data.local.entity.RecurringTransactionEntity
import com.example.expensetracker.data.local.entity.SavingsGoalEntity
import com.example.expensetracker.data.local.entity.TransactionEntity
import com.example.expensetracker.domain.model.RecurringFrequency
import com.example.expensetracker.domain.model.TransactionType
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SampleDataGenerator @Inject constructor(
    private val database: ExpenseTrackerDatabase
) {
    suspend fun generateSampleData() {
        val transactionDao = database.transactionDao()
        val accountDao = database.accountDao()
        val categoryDao = database.categoryDao()
        val budgetDao = database.budgetDao()
        val savingsDao = database.savingsGoalDao()
        val recurringDao = database.recurringTransactionDao()

        if (categoryDao.getCategoryCount() == 0) {
            categoryDao.insertCategories(DefaultData.defaultExpenseCategories)
            categoryDao.insertCategories(DefaultData.defaultIncomeCategories)
        }
        val existingAccounts = accountDao.getAccountById(1L)
        if (existingAccounts == null) {
            for (account in DefaultData.defaultAccounts) {
                accountDao.insertAccount(account)
            }
        }

        val now = System.currentTimeMillis()
        val dayMillis = 24 * 60 * 60 * 1000L

        val samples = listOf(
            TransactionEntity(title = "Monthly Salary Deposit", amount = 450000L, type = TransactionType.INCOME.name, categoryId = 17, accountId = 2, date = now - (dayMillis * 1), paymentMethod = "BANK_TRANSFER", note = "Base Salary"),
            TransactionEntity(title = "Whole Foods Market", amount = 14550L, type = TransactionType.EXPENSE.name, categoryId = 2, accountId = 1, date = now - (dayMillis * 2), paymentMethod = "CARD", note = "Weekly groceries"),
            TransactionEntity(title = "Bistro Olive Dinner", amount = 8900L, type = TransactionType.EXPENSE.name, categoryId = 1, accountId = 1, date = now - (dayMillis * 3), paymentMethod = "CARD", note = "Dinner with friends"),
            TransactionEntity(title = "Metro & Uber Rides", amount = 3450L, type = TransactionType.EXPENSE.name, categoryId = 3, accountId = 1, date = now - (dayMillis * 3), paymentMethod = "DIGITAL_WALLET"),
            TransactionEntity(title = "Apartment Rent", amount = 120000L, type = TransactionType.EXPENSE.name, categoryId = 8, accountId = 2, date = now - (dayMillis * 5), paymentMethod = "BANK_TRANSFER", note = "Monthly rent"),
            TransactionEntity(title = "Electric & Fiber Internet", amount = 11500L, type = TransactionType.EXPENSE.name, categoryId = 9, accountId = 2, date = now - (dayMillis * 6), paymentMethod = "BANK_TRANSFER"),
            TransactionEntity(title = "Freelance Design Sprint", amount = 95000L, type = TransactionType.INCOME.name, categoryId = 18, accountId = 2, date = now - (dayMillis * 7), paymentMethod = "BANK_TRANSFER"),
            TransactionEntity(title = "Streaming Bundle", amount = 1999L, type = TransactionType.EXPENSE.name, categoryId = 11, accountId = 2, date = now - (dayMillis * 8), paymentMethod = "CARD"),
            TransactionEntity(title = "City Pharmacy", amount = 4200L, type = TransactionType.EXPENSE.name, categoryId = 6, accountId = 1, date = now - (dayMillis * 9), paymentMethod = "CASH"),
            TransactionEntity(title = "Kotlin Architecture Book", amount = 5400L, type = TransactionType.EXPENSE.name, categoryId = 7, accountId = 2, date = now - (dayMillis * 10), paymentMethod = "CARD"),
            TransactionEntity(title = "Apparel & Footwear", amount = 18500L, type = TransactionType.EXPENSE.name, categoryId = 4, accountId = 2, date = now - (dayMillis * 12), paymentMethod = "CARD"),
            TransactionEntity(title = "Coffee & Bakery", amount = 1650L, type = TransactionType.EXPENSE.name, categoryId = 1, accountId = 1, date = now - (dayMillis * 14), paymentMethod = "CASH")
        )

        database.withTransaction {
            samples.forEach { tx ->
                transactionDao.insertTransaction(tx)
                val delta = if (tx.type == TransactionType.INCOME.name) tx.amount else -tx.amount
                accountDao.updateAccountBalance(tx.accountId, delta)
            }

            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            budgetDao.insertBudget(
                BudgetEntity(
                    name = "Monthly Living Budget",
                    amountLimit = 250000L,
                    periodType = "MONTHLY",
                    startDate = month.startOfMonthMillis(year),
                    endDate = month.endOfMonthMillis(year),
                    notifyThreshold = 80
                )
            )

            val targetCal = (cal.clone() as Calendar).apply { add(Calendar.MONTH, 6) }
            savingsDao.insertGoal(
                SavingsGoalEntity(
                    title = "Emergency Reserve",
                    targetAmount = 500000L,
                    currentAmount = 175000L,
                    targetDate = targetCal.timeInMillis,
                    iconName = "Security",
                    colorHex = "#0EA5E9",
                    description = "6-month safety cushion"
                )
            )
            savingsDao.insertGoal(
                SavingsGoalEntity(
                    title = "Kyoto Autumn Trip",
                    targetAmount = 240000L,
                    currentAmount = 92000L,
                    targetDate = targetCal.timeInMillis,
                    iconName = "Flight",
                    colorHex = "#10B981",
                    description = "Flights and rail pass"
                )
            )

            val nextMonth = (cal.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, 7) }
            recurringDao.insertRecurring(
                RecurringTransactionEntity(
                    title = "Apartment Rent",
                    amount = 120000L,
                    type = TransactionType.EXPENSE.name,
                    categoryId = 8,
                    accountId = 2,
                    frequency = RecurringFrequency.MONTHLY.name,
                    startDate = now,
                    nextDueDate = nextMonth.timeInMillis,
                    isEnabled = true
                )
            )
            recurringDao.insertRecurring(
                RecurringTransactionEntity(
                    title = "Monthly Salary",
                    amount = 450000L,
                    type = TransactionType.INCOME.name,
                    categoryId = 17,
                    accountId = 2,
                    frequency = RecurringFrequency.MONTHLY.name,
                    startDate = now,
                    nextDueDate = nextMonth.timeInMillis,
                    isEnabled = true
                )
            )
        }
    }

    suspend fun clearAllUserData() {
        database.withTransaction {
            database.clearAllTables()
            database.categoryDao().insertCategories(DefaultData.defaultExpenseCategories)
            database.categoryDao().insertCategories(DefaultData.defaultIncomeCategories)
            for (account in DefaultData.defaultAccounts) {
                database.accountDao().insertAccount(account)
            }
        }
    }
}

