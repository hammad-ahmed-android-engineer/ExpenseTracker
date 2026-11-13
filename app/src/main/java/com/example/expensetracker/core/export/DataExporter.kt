package com.example.expensetracker.core.export

import com.example.expensetracker.core.common.toIsoDate
import com.example.expensetracker.domain.model.Transaction
import org.json.JSONArray
import org.json.JSONObject
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
