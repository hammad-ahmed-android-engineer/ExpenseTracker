package com.example.expensetracker.core.export

import com.example.expensetracker.core.common.toIsoDate
import com.example.expensetracker.domain.model.transaction.Transaction
import com.example.expensetracker.domain.model.transaction.TransactionType
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataExporter @Inject constructor() {

    fun exportTransactionsToCsv(transactions: List<Transaction>): String {
        val sb = StringBuilder()
        sb.append("ID,Title,Amount,Type,Category,Account,Date,PaymentMethod,Note\n")

        for (tx in transactions) {
            val dateStr = tx.date.toIsoDate()
            val safeTitle = tx.title.replace("\"", "\"\"")
            val safeCat = tx.categoryName.replace("\"", "\"\"")
            val safeAcc = tx.accountName.replace("\"", "\"\"")
            val noteStr = (tx.note ?: "").replace("\"", "\"\"").replace(",", ";")
            sb.append("${tx.id},\"$safeTitle\",${tx.amount},${tx.type.name},\"$safeCat\",\"$safeAcc\",$dateStr,${tx.paymentMethod},\"$noteStr\"\n")
        }
        return sb.toString()
    }

    fun parseTransactionsFromCsv(csv: String): List<Transaction> {
        val lines = csv.lineSequence().filter { it.isNotBlank() }.toList()
        if (lines.isEmpty()) return emptyList()

        val results = mutableListOf<Transaction>()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        for ((index, line) in lines.withIndex()) {
            if (index == 0 && line.contains("Title", ignoreCase = true)) {
                continue // Skip CSV header
            }
            val tokens = parseCsvLine(line)
            if (tokens.size >= 4) {
                val title = tokens.getOrNull(1)?.trim().orEmpty().ifEmpty { "Transaction" }
                val amount = tokens.getOrNull(2)?.trim()?.toLongOrNull() ?: continue
                val typeStr = tokens.getOrNull(3)?.trim()?.uppercase() ?: "EXPENSE"
                val type = runCatching { TransactionType.valueOf(typeStr) }.getOrDefault(TransactionType.EXPENSE)
                val catName = tokens.getOrNull(4)?.trim().orEmpty().ifEmpty { "General" }
                val accName = tokens.getOrNull(5)?.trim().orEmpty().ifEmpty { "Main Account" }
                val dateStr = tokens.getOrNull(6)?.trim().orEmpty()
                val date = runCatching { dateFormat.parse(dateStr)?.time }.getOrNull() ?: System.currentTimeMillis()
                val paymentMethod = tokens.getOrNull(7)?.trim().orEmpty().ifEmpty { "CASH" }
                val note = tokens.getOrNull(8)?.trim()

                results.add(
                    Transaction(
                        id = 0L,
                        title = title,
                        amount = amount,
                        type = type,
                        categoryId = 1L,
                        accountId = 1L,
                        categoryName = catName,
                        accountName = accName,
                        date = date,
                        paymentMethod = paymentMethod,
                        note = note
                    )
                )
            }
        }
        return results
    }

    private fun parseCsvLine(line: String): List<String> {
        val tokens = mutableListOf<String>()
        val sb = StringBuilder()
        var inQuotes = false
        var i = 0
        while (i < line.length) {
            val c = line[i]
            when {
                c == '"' -> {
                    if (inQuotes && i + 1 < line.length && line[i + 1] == '"') {
                        sb.append('"')
                        i++
                    } else {
                        inQuotes = !inQuotes
                    }
                }
                c == ',' && !inQuotes -> {
                    tokens.add(sb.toString())
                    sb.clear()
                }
                else -> {
                    sb.append(c)
                }
            }
            i++
        }
        tokens.add(sb.toString())
        return tokens
    }

    fun exportTransactionsToJson(transactions: List<Transaction>): String {
        val root = JSONObject()
        root.put("exportedAt", System.currentTimeMillis())
        root.put("count", transactions.size)
        val array = JSONArray()
        for (tx in transactions) {
            array.put(
                JSONObject().apply {
                    put("id", tx.id)
                    put("title", tx.title)
                    put("amountMinorUnits", tx.amount)
                    put("type", tx.type.name)
                    put("category", tx.categoryName)
                    put("account", tx.accountName)
                    put("date", tx.date.toIsoDate())
                    put("timestamp", tx.date)
                    put("paymentMethod", tx.paymentMethod)
                    put("note", tx.note ?: "")
                }
            )
        }
        root.put("transactions", array)
        return root.toString(2)
    }
}
