package com.example.expensetracker.domain.model.transaction

enum class TransactionType(val displayName: String) {
    EXPENSE("Expense"),
    INCOME("Income"),
    TRANSFER("Account Transfer"),
    REFUND("Refund"),
    ADJUSTMENT("Adjustment");

    companion object {
        fun fromString(type: String): TransactionType {
            return entries.find { it.name.equals(type, ignoreCase = true) } ?: EXPENSE
        }
    }
}
