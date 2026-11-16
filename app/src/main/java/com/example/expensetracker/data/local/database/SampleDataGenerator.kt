package com.example.expensetracker.data.local.database

import androidx.room.withTransaction
import com.example.expensetracker.core.common.endOfMonthMillis
import com.example.expensetracker.core.common.startOfMonthMillis
import com.example.expensetracker.data.local.dao.AccountDao
import com.example.expensetracker.data.local.dao.BudgetDao
import com.example.expensetracker.data.local.dao.CategoryDao
import com.example.expensetracker.data.local.dao.RecurringTransactionDao
import com.example.expensetracker.data.local.dao.SavingsGoalDao
import com.example.expensetracker.data.local.dao.TransactionDao
import com.example.expensetracker.data.local.entity.budget.BudgetCategoryEntity
import com.example.expensetracker.data.local.entity.budget.BudgetEntity
import com.example.expensetracker.data.local.entity.recurring.RecurringTransactionEntity
import com.example.expensetracker.data.local.entity.savings.SavingsGoalEntity
import com.example.expensetracker.data.local.entity.transaction.TransactionEntity
import com.example.expensetracker.domain.model.recurring.RecurringFrequency
import com.example.expensetracker.domain.model.transaction.TransactionType
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SampleDataGenerator @Inject constructor(
    private val database: ExpenseTrackerDatabase,
    private val transactionDao: TransactionDao,
    private val accountDao: AccountDao,
    private val categoryDao: CategoryDao,
    private val budgetDao: BudgetDao,
    private val savingsDao: SavingsGoalDao,
    private val recurringDao: RecurringTransactionDao
) {

    suspend fun generateSampleData() {
        database.withTransaction {
            if (categoryDao.getCategoryCount() == 0) {
                categoryDao.insertCategories(DefaultData.defaultExpenseCategories)
                categoryDao.insertCategories(DefaultData.defaultIncomeCategories)
            }
            val existingAccounts = accountDao.getAllAccounts()
            if (existingAccounts.isEmpty()) {
                for (account in DefaultData.defaultAccounts) {
                    accountDao.insertAccount(account)
                }
            }

            val accounts = accountDao.getAllAccounts()
            val categories = categoryDao.getAllCategories()
            if (accounts.isEmpty() || categories.isEmpty()) return@withTransaction

        val checkingAcc = accounts.find { it.name.contains("Checking", ignoreCase = true) }?.id ?: accounts.first().id
        val cashAcc = accounts.find { it.name.contains("Cash", ignoreCase = true) }?.id ?: accounts.getOrNull(1)?.id ?: checkingAcc
        val savingsAcc = accounts.find { it.name.contains("Savings", ignoreCase = true) }?.id ?: accounts.getOrNull(2)?.id ?: checkingAcc

        val foodCat = categories.find { it.name.contains("Food", ignoreCase = true) }?.id ?: categories.first().id
        val utilCat = categories.find { it.name.contains("Utilities", ignoreCase = true) }?.id ?: categories.first().id
        val entCat = categories.find { it.name.contains("Entertainment", ignoreCase = true) }?.id ?: categories.first().id
        val transCat = categories.find { it.name.contains("Transport", ignoreCase = true) }?.id ?: categories.first().id
        val rentCat = categories.find { it.name.contains("Housing", ignoreCase = true) || it.name.contains("Rent", ignoreCase = true) }?.id ?: categories.first().id
        val otherCat = categories.find { it.name.contains("Other", ignoreCase = true) || it.name.contains("Bills", ignoreCase = true) }?.id ?: categories.first().id
        val salaryCat = categories.find { it.name.contains("Salary", ignoreCase = true) }?.id ?: categories.first().id
        val freelanceCat = categories.find { it.name.contains("Freelanc", ignoreCase = true) }?.id ?: salaryCat
        val investCat = categories.find { it.name.contains("Invest", ignoreCase = true) }?.id ?: salaryCat

        val now = System.currentTimeMillis()
        val dayMillis = 24 * 60 * 60 * 1000L

        val samples = listOf(
            TransactionEntity(title = "Salary", amount = 435000L, type = TransactionType.INCOME.name, categoryId = salaryCat, accountId = checkingAcc, date = now - (dayMillis * 1), paymentMethod = "BANK_TRANSFER", note = "Tech Corp Payroll"),
            TransactionEntity(title = "Food", amount = 4550L, type = TransactionType.EXPENSE.name, categoryId = foodCat, accountId = cashAcc, date = now - (dayMillis * 1), paymentMethod = "CASH", note = "Oct • Savoir"),
            TransactionEntity(title = "Utilities", amount = 4250L, type = TransactionType.EXPENSE.name, categoryId = utilCat, accountId = checkingAcc, date = now - (dayMillis * 2), paymentMethod = "CARD", note = "Power & gas"),
            TransactionEntity(title = "Freelance Project", amount = 85000L, type = TransactionType.INCOME.name, categoryId = freelanceCat, accountId = checkingAcc, date = now - (dayMillis * 3), paymentMethod = "BANK_TRANSFER", note = "Mobile App Design"),
            TransactionEntity(title = "Entertainment", amount = 9000L, type = TransactionType.EXPENSE.name, categoryId = entCat, accountId = checkingAcc, date = now - (dayMillis * 3), paymentMethod = "CARD", note = "Emriction inanitlogs"),
            TransactionEntity(title = "Transport", amount = 4250L, type = TransactionType.EXPENSE.name, categoryId = transCat, accountId = cashAcc, date = now - (dayMillis * 4), paymentMethod = "CARD"),
            TransactionEntity(title = "Transfers", amount = 1050L, type = TransactionType.EXPENSE.name, categoryId = otherCat, accountId = cashAcc, date = now - (dayMillis * 5), paymentMethod = "BANK_TRANSFER"),
            TransactionEntity(title = "Transfer", amount = 30000L, type = TransactionType.TRANSFER.name, categoryId = otherCat, accountId = savingsAcc, destinationAccountId = checkingAcc, date = now - (dayMillis * 6), paymentMethod = "BANK_TRANSFER", note = "checking"),
            TransactionEntity(title = "Transfer", amount = 30000L, type = TransactionType.TRANSFER.name, categoryId = otherCat, accountId = savingsAcc, destinationAccountId = checkingAcc, date = now - (dayMillis * 7), paymentMethod = "BANK_TRANSFER", note = "checking"),
            TransactionEntity(title = "Rent", amount = 120000L, type = TransactionType.EXPENSE.name, categoryId = rentCat, accountId = checkingAcc, date = now - (dayMillis * 8), paymentMethod = "BANK_TRANSFER"),
            TransactionEntity(title = "Investment Dividend", amount = 15000L, type = TransactionType.INCOME.name, categoryId = investCat, accountId = savingsAcc, date = now - (dayMillis * 10), paymentMethod = "BANK_TRANSFER", note = "Quarterly dividend")
        )

        samples.forEach { tx ->
            transactionDao.insertTransaction(tx)
            when (tx.type) {
                TransactionType.INCOME.name -> accountDao.updateAccountBalance(tx.accountId, tx.amount)
                TransactionType.EXPENSE.name -> accountDao.updateAccountBalance(tx.accountId, -tx.amount)
                TransactionType.TRANSFER.name -> {
                    accountDao.updateAccountBalance(tx.accountId, -tx.amount)
                    tx.destinationAccountId?.let { destId ->
                        accountDao.updateAccountBalance(destId, tx.amount)
                    }
                }
            }
        }

        val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val b1Id = budgetDao.insertBudget(
                BudgetEntity(
                    name = "Groceries",
                    amountLimit = 35000L,
                    periodType = "MONTHLY",
                    startDate = month.startOfMonthMillis(year),
                    endDate = month.endOfMonthMillis(year),
                    notifyThreshold = 80
                )
            )
            budgetDao.insertBudgetCategories(
                listOf(
                    BudgetCategoryEntity(
                        budgetId = b1Id,
                        categoryId = foodCat,
                        categoryLimit = 35000L
                    )
                )
            )

            val b2Id = budgetDao.insertBudget(
                BudgetEntity(
                    name = "Entertainment",
                    amountLimit = 10000L,
                    periodType = "MONTHLY",
                    startDate = month.startOfMonthMillis(year),
                    endDate = month.endOfMonthMillis(year),
                    notifyThreshold = 90
                )
            )
            budgetDao.insertBudgetCategories(
                listOf(
                    BudgetCategoryEntity(
                        budgetId = b2Id,
                        categoryId = entCat,
                        categoryLimit = 10000L
                    )
                )
            )

            val targetCal = (cal.clone() as Calendar).apply { add(Calendar.MONTH, 6) }
            savingsDao.insertGoal(
                SavingsGoalEntity(
                    title = "New Laptop",
                    targetAmount = 100000L,
                    currentAmount = 80000L,
                    targetDate = targetCal.timeInMillis,
                    iconName = "Laptop",
                    colorHex = "#2DD4BF",
                    description = "Work machine upgrade"
                )
            )
            savingsDao.insertGoal(
                SavingsGoalEntity(
                    title = "Emergency Fund",
                    targetAmount = 1000000L,
                    currentAmount = 500000L,
                    targetDate = targetCal.timeInMillis,
                    iconName = "Security",
                    colorHex = "#F87171",
                    description = "Safety reserve"
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

    suspend fun seedIncomeIfEmpty() {
        val accounts = accountDao.getAllAccounts()
        val categories = categoryDao.getAllCategories()
        if (accounts.isEmpty() || categories.isEmpty()) return

        val checkingAcc = accounts.find { it.name.contains("Checking", ignoreCase = true) }?.id ?: accounts.first().id
        val savingsAcc = accounts.find { it.name.contains("Savings", ignoreCase = true) }?.id ?: accounts.getOrNull(2)?.id ?: checkingAcc

        val salaryCat = categories.find { it.name.contains("Salary", ignoreCase = true) }?.id ?: categories.first().id
        val freelanceCat = categories.find { it.name.contains("Freelanc", ignoreCase = true) }?.id ?: salaryCat
        val investCat = categories.find { it.name.contains("Invest", ignoreCase = true) }?.id ?: salaryCat

        val now = System.currentTimeMillis()
        val dayMillis = 24 * 60 * 60 * 1000L

        val incomeCount = transactionDao.getTransactionCountByType(TransactionType.INCOME.name)
        if (incomeCount == 0) {
            val incomeSamples = listOf(
                TransactionEntity(
                    title = "Salary",
                    amount = 435000L,
                    type = TransactionType.INCOME.name,
                    categoryId = salaryCat,
                    accountId = checkingAcc,
                    date = now - (dayMillis * 1),
                    paymentMethod = "BANK_TRANSFER",
                    note = "Tech Corp Payroll"
                ),
                TransactionEntity(
                    title = "Freelance Project",
                    amount = 85000L,
                    type = TransactionType.INCOME.name,
                    categoryId = freelanceCat,
                    accountId = checkingAcc,
                    date = now - (dayMillis * 3),
                    paymentMethod = "BANK_TRANSFER",
                    note = "Mobile App Design"
                ),
                TransactionEntity(
                    title = "Investment Dividend",
                    amount = 15000L,
                    type = TransactionType.INCOME.name,
                    categoryId = investCat,
                    accountId = savingsAcc,
                    date = now - (dayMillis * 10),
                    paymentMethod = "BANK_TRANSFER",
                    note = "Quarterly dividend"
                )
            )

            incomeSamples.forEach { tx ->
                transactionDao.insertTransaction(tx)
                accountDao.updateAccountBalance(tx.accountId, tx.amount)
            }
        }
    }

    suspend fun clearAllUserData() {
        database.withTransaction {
            transactionDao.deleteAllTransactions()
            budgetDao.deleteAllBudgetCategories()
            budgetDao.deleteAllBudgets()
            savingsDao.deleteAllContributions()
            savingsDao.deleteAllGoals()
            recurringDao.deleteAllRecurring()
            accountDao.deleteAllAccounts()
            categoryDao.deleteAllSubcategories()
            categoryDao.deleteAllCategories()

            categoryDao.insertCategories(DefaultData.defaultExpenseCategories)
            categoryDao.insertCategories(DefaultData.defaultIncomeCategories)
            for (account in DefaultData.defaultAccounts) {
                accountDao.insertAccount(account)
            }
        }
    }
}

