package com.example.expensetracker.data.repository

import com.example.expensetracker.core.export.DataExporter
import com.example.expensetracker.data.local.dao.AccountDao
import com.example.expensetracker.data.local.dao.BudgetDao
import com.example.expensetracker.data.local.dao.CategoryDao
import com.example.expensetracker.data.local.dao.RecurringTransactionDao
import com.example.expensetracker.data.local.dao.SavingsGoalDao
import com.example.expensetracker.data.local.dao.TransactionDao
import com.example.expensetracker.data.local.database.SampleDataGenerator
import com.example.expensetracker.data.local.entity.AccountEntity
import com.example.expensetracker.data.local.entity.BudgetEntity
import com.example.expensetracker.data.local.entity.CategoryEntity
import com.example.expensetracker.data.local.entity.RecurringTransactionEntity
import com.example.expensetracker.data.local.entity.SavingsGoalEntity
import com.example.expensetracker.data.local.entity.TransactionEntity
import com.example.expensetracker.domain.model.BackupPreview
import com.example.expensetracker.domain.model.BackupRestoreMode
import com.example.expensetracker.domain.repository.BackupRepository
import com.example.expensetracker.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.first
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackupRepositoryImpl @Inject constructor(
    private val accountDao: AccountDao,
    private val categoryDao: CategoryDao,
    private val transactionDao: TransactionDao,
    private val budgetDao: BudgetDao,
    private val savingsGoalDao: SavingsGoalDao,
    private val recurringTransactionDao: RecurringTransactionDao,
    private val transactionRepository: TransactionRepository,
    private val dataExporter: DataExporter,
    private val sampleDataGenerator: SampleDataGenerator
) : BackupRepository {

    override suspend fun createBackupJson(): String {
        val accounts = accountDao.getAllAccountsFlow().first()
        val categories = categoryDao.getAllCategoriesFlow().first()
        val transactions = transactionDao.getAllTransactionsFlow().first()
        val budgets = budgetDao.getAllBudgetsFlow().first()
        val goals = savingsGoalDao.getAllGoalsFlow().first()
        val recurring = recurringTransactionDao.getAllRecurringFlow().first()

        return JSONObject().apply {
            put("appVersion", "1.0.0")
            put("exportedAt", System.currentTimeMillis())
            put("accounts", JSONArray().apply {
                accounts.forEach { acc ->
                    put(JSONObject().apply {
                        put("id", acc.id)
                        put("name", acc.name)
                        put("type", acc.type)
                        put("initialBalance", acc.initialBalance)
                        put("currentBalance", acc.currentBalance)
                        put("currency", acc.currency)
                        put("colorHex", acc.colorHex)
                        put("iconName", acc.iconName)
                        put("isArchived", acc.isArchived)
                        put("createdAt", acc.createdAt)
                    })
                }
            })
            put("categories", JSONArray().apply {
                categories.forEach { cat ->
                    put(JSONObject().apply {
                        put("id", cat.id)
                        put("name", cat.name)
                        put("type", cat.type)
                        put("iconName", cat.iconName)
                        put("colorHex", cat.colorHex)
                        put("orderIndex", cat.orderIndex)
                        put("isDefault", cat.isDefault)
                    })
                }
            })
            put("transactions", JSONArray().apply {
                transactions.forEach { tx ->
                    put(JSONObject().apply {
                        put("id", tx.id)
                        put("title", tx.title)
                        put("amount", tx.amount)
                        put("type", tx.type)
                        put("categoryId", tx.categoryId)
                        put("accountId", tx.accountId)
                        put("destinationAccountId", tx.destinationAccountId)
                        put("date", tx.date)
                        put("paymentMethod", tx.paymentMethod)
                        put("note", tx.note)
                        put("createdAt", tx.createdAt)
                    })
                }
            })
            put("budgets", JSONArray().apply {
                budgets.forEach { b ->
                    put(JSONObject().apply {
                        put("id", b.id)
                        put("name", b.name)
                        put("amountLimit", b.amountLimit)
                        put("periodType", b.periodType)
                        put("startDate", b.startDate)
                        put("endDate", b.endDate)
                        put("isRolloverEnabled", b.isRolloverEnabled)
                        put("notifyThreshold", b.notifyThreshold)
                        put("createdAt", b.createdAt)
                    })
                }
            })
            put("savingsGoals", JSONArray().apply {
                goals.forEach { g ->
                    put(JSONObject().apply {
                        put("id", g.id)
                        put("title", g.title)
                        put("targetAmount", g.targetAmount)
                        put("currentAmount", g.currentAmount)
                        put("targetDate", g.targetDate)
                        put("iconName", g.iconName)
                        put("colorHex", g.colorHex)
                        put("description", g.description)
                        put("isCompleted", g.isCompleted)
                        put("createdAt", g.createdAt)
                    })
                }
            })
            put("recurring", JSONArray().apply {
                recurring.forEach { r ->
                    put(JSONObject().apply {
                        put("id", r.id)
                        put("title", r.title)
                        put("amount", r.amount)
                        put("type", r.type)
                        put("categoryId", r.categoryId)
                        put("accountId", r.accountId)
                        put("destinationAccountId", r.destinationAccountId)
                        put("frequency", r.frequency)
                        put("intervalDays", r.intervalDays)
                        put("startDate", r.startDate)
                        put("endDate", r.endDate)
                        put("lastExecutedDate", r.lastExecutedDate)
                        put("nextDueDate", r.nextDueDate)
                        put("isEnabled", r.isEnabled)
                        put("note", r.note)
                    })
                }
            })
        }.toString(2)
    }

    override suspend fun exportTransactionsJson(): String {
        val transactions = transactionRepository.getAllTransactions().first()
        return dataExporter.exportTransactionsToJson(transactions)
    }

    override suspend fun exportTransactionsCsv(): String {
        val transactions = transactionRepository.getAllTransactions().first()
        return dataExporter.exportTransactionsToCsv(transactions)
    }

    override suspend fun inspectBackup(json: String): Result<BackupPreview> = runCatching {
        val obj = JSONObject(json)
        if (!obj.has("transactions") || !obj.has("accounts") || !obj.has("categories")) {
            throw IllegalArgumentException("Invalid backup format")
        }
        BackupPreview(
            exportedAt = obj.optLong("exportedAt", 0L),
            appVersion = obj.optString("appVersion", "1.0.0"),
            accountCount = obj.optJSONArray("accounts")?.length() ?: 0,
            categoryCount = obj.optJSONArray("categories")?.length() ?: 0,
            transactionCount = obj.optJSONArray("transactions")?.length() ?: 0,
            budgetCount = obj.optJSONArray("budgets")?.length() ?: 0,
            savingsGoalCount = obj.optJSONArray("savingsGoals")?.length() ?: 0,
            recurringCount = obj.optJSONArray("recurring")?.length() ?: 0
        )
    }

    override suspend fun restoreBackup(json: String, mode: BackupRestoreMode): Result<BackupPreview> = runCatching {
        val preview = inspectBackup(json).getOrThrow()
        val obj = JSONObject(json)

        if (mode == BackupRestoreMode.REPLACE_ALL) {
            transactionDao.deleteAllTransactions()
            budgetDao.deleteAllBudgetCategories()
            budgetDao.deleteAllBudgets()
            savingsGoalDao.deleteAllContributions()
            savingsGoalDao.deleteAllGoals()
            recurringTransactionDao.deleteAllRecurring()
            accountDao.deleteAllAccounts()
            categoryDao.deleteAllSubcategories()
            categoryDao.deleteAllCategories()
        }

        val categoriesArr = obj.optJSONArray("categories") ?: JSONArray()
            val catList = mutableListOf<CategoryEntity>()
            for (i in 0 until categoriesArr.length()) {
                val c = categoriesArr.getJSONObject(i)
                val id = if (mode == BackupRestoreMode.REPLACE_ALL) c.getLong("id") else 0L
                catList.add(
                    CategoryEntity(
                        id = id,
                        name = c.getString("name"),
                        type = c.getString("type"),
                        iconName = c.optString("iconName", "Category"),
                        colorHex = c.optString("colorHex", "#10B981"),
                        orderIndex = c.optInt("orderIndex", i),
                        isDefault = c.optBoolean("isDefault", false)
                    )
                )
            }
            if (mode == BackupRestoreMode.REPLACE_ALL) {
                categoryDao.insertCategories(catList)
            } else {
                val existingCats = categoryDao.getAllCategoriesFlow().first().map { "${it.name}:${it.type}" }.toSet()
                catList.filterNot { "${it.name}:${it.type}" in existingCats }.forEach {
                    categoryDao.insertCategory(it)
                }
            }

            val accountsArr = obj.optJSONArray("accounts") ?: JSONArray()
            val existingAccountNames = if (mode == BackupRestoreMode.MERGE_SKIP_DUPLICATES) {
                accountDao.getAllAccountsFlow().first().map { it.name }.toSet()
            } else emptySet()

            for (i in 0 until accountsArr.length()) {
                val a = accountsArr.getJSONObject(i)
                val name = a.getString("name")
                if (mode == BackupRestoreMode.MERGE_SKIP_DUPLICATES && name in existingAccountNames) continue
                accountDao.insertAccount(
                    AccountEntity(
                        id = if (mode == BackupRestoreMode.REPLACE_ALL) a.getLong("id") else 0L,
                        name = name,
                        type = a.getString("type"),
                        initialBalance = a.getLong("initialBalance"),
                        currentBalance = a.getLong("currentBalance"),
                        currency = a.optString("currency", "USD"),
                        colorHex = a.optString("colorHex", "#0EA5E9"),
                        iconName = a.optString("iconName", "AccountBalance"),
                        isArchived = a.optBoolean("isArchived", false),
                        createdAt = a.optLong("createdAt", System.currentTimeMillis())
                    )
                )
            }

            val txArr = obj.optJSONArray("transactions") ?: JSONArray()
            val existingTxKeys = if (mode == BackupRestoreMode.MERGE_SKIP_DUPLICATES) {
                transactionDao.getAllTransactionsFlow().first()
                    .map { "${it.title}|${it.amount}|${it.date}" }.toSet()
            } else emptySet()

            for (i in 0 until txArr.length()) {
                val t = txArr.getJSONObject(i)
                val title = t.getString("title")
                val amount = t.getLong("amount")
                val date = t.getLong("date")
                if (mode == BackupRestoreMode.MERGE_SKIP_DUPLICATES && "$title|$amount|$date" in existingTxKeys) continue
                transactionDao.insertTransaction(
                    TransactionEntity(
                        id = if (mode == BackupRestoreMode.REPLACE_ALL) t.getLong("id") else 0L,
                        title = title,
                        amount = amount,
                        type = t.getString("type"),
                        categoryId = t.getLong("categoryId"),
                        accountId = t.getLong("accountId"),
                        destinationAccountId = if (t.isNull("destinationAccountId")) null else t.optLong("destinationAccountId"),
                        date = date,
                        paymentMethod = t.optString("paymentMethod", "CASH"),
                        note = if (t.isNull("note")) null else t.optString("note"),
                        createdAt = t.optLong("createdAt", System.currentTimeMillis())
                    )
                )
            }

            val budgetsArr = obj.optJSONArray("budgets") ?: JSONArray()
            for (i in 0 until budgetsArr.length()) {
                val b = budgetsArr.getJSONObject(i)
                budgetDao.insertBudget(
                    BudgetEntity(
                        id = if (mode == BackupRestoreMode.REPLACE_ALL) b.getLong("id") else 0L,
                        name = b.getString("name"),
                        amountLimit = b.getLong("amountLimit"),
                        periodType = b.getString("periodType"),
                        startDate = b.getLong("startDate"),
                        endDate = b.getLong("endDate"),
                        isRolloverEnabled = b.optBoolean("isRolloverEnabled", false),
                        notifyThreshold = b.optInt("notifyThreshold", 80),
                        createdAt = b.optLong("createdAt", System.currentTimeMillis())
                    )
                )
            }

            val goalsArr = obj.optJSONArray("savingsGoals") ?: JSONArray()
            for (i in 0 until goalsArr.length()) {
                val g = goalsArr.getJSONObject(i)
                savingsGoalDao.insertGoal(
                    SavingsGoalEntity(
                        id = if (mode == BackupRestoreMode.REPLACE_ALL) g.getLong("id") else 0L,
                        title = g.getString("title"),
                        targetAmount = g.getLong("targetAmount"),
                        currentAmount = g.getLong("currentAmount"),
                        targetDate = g.getLong("targetDate"),
                        iconName = g.optString("iconName", "Savings"),
                        colorHex = g.optString("colorHex", "#10B981"),
                        description = if (g.isNull("description")) null else g.optString("description"),
                        isCompleted = g.optBoolean("isCompleted", false),
                        createdAt = g.optLong("createdAt", System.currentTimeMillis())
                    )
                )
            }

            val recArr = obj.optJSONArray("recurring") ?: JSONArray()
            for (i in 0 until recArr.length()) {
                val r = recArr.getJSONObject(i)
                recurringTransactionDao.insertRecurring(
                    RecurringTransactionEntity(
                        id = if (mode == BackupRestoreMode.REPLACE_ALL) r.getLong("id") else 0L,
                        title = r.getString("title"),
                        amount = r.getLong("amount"),
                        type = r.getString("type"),
                        categoryId = r.getLong("categoryId"),
                        accountId = r.getLong("accountId"),
                        destinationAccountId = if (r.isNull("destinationAccountId")) null else r.optLong("destinationAccountId"),
                        frequency = r.getString("frequency"),
                        intervalDays = r.optInt("intervalDays", 1),
                        startDate = r.getLong("startDate"),
                        endDate = if (r.isNull("endDate")) null else r.optLong("endDate"),
                        lastExecutedDate = if (r.isNull("lastExecutedDate")) null else r.optLong("lastExecutedDate"),
                        nextDueDate = r.getLong("nextDueDate"),
                        isEnabled = r.optBoolean("isEnabled", true),
                        note = if (r.isNull("note")) null else r.optString("note")
                    )
                )
            }
        preview
    }

    override suspend fun seedSampleData() {
        sampleDataGenerator.generateSampleData()
    }

    override suspend fun clearAllUserData() {
        sampleDataGenerator.clearAllUserData()
    }
}

