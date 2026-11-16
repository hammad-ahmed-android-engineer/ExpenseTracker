package com.example.expensetracker.domain.model.account

enum class AccountType(val displayName: String, val defaultIcon: String) {
    CASH("Cash", "Payments"),
    BANK_ACCOUNT("Bank Account", "AccountBalance"),
    SAVINGS_ACCOUNT("Savings Account", "Savings"),
    CREDIT_CARD("Credit Card", "CreditCard"),
    DEBIT_CARD("Debit Card", "CreditCard"),
    DIGITAL_WALLET("Digital Wallet", "AccountBalanceWallet"),
    OTHER("Other", "Category");

    companion object {
        fun fromString(type: String): AccountType {
            return entries.find { it.name.equals(type, ignoreCase = true) } ?: CASH
        }
    }
}
