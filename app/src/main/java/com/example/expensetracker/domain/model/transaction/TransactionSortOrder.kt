package com.example.expensetracker.domain.model.transaction

enum class TransactionSortOrder(val displayName: String) {
    NEWEST_FIRST("Newest"),
    OLDEST_FIRST("Oldest"),
    HIGHEST_AMOUNT("Highest"),
    LOWEST_AMOUNT("Lowest")
}
