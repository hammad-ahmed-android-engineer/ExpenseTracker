package com.example.expensetracker.domain.model

enum class TransactionSortOrder(val displayName: String) {
    NEWEST_FIRST("Newest"),
    OLDEST_FIRST("Oldest"),
    HIGHEST_AMOUNT("Highest"),
    LOWEST_AMOUNT("Lowest")
}

data class TransactionFilter(
    val query: String? = null,
    val type: TransactionType? = null,
    val accountId: Long? = null,
    val categoryId: Long? = null,
    val startDate: Long? = null,
    val endDate: Long? = null,
    val paymentMethod: String? = null,
    val tag: String? = null,
    val sortOrder: TransactionSortOrder = TransactionSortOrder.NEWEST_FIRST
)

