package com.example.expensetracker.domain.model.reports

data class CategorySpendingSummary(
    val categoryId: Long,
    val categoryName: String,
    val categoryColor: String,
    val categoryIcon: String,
    val totalAmount: Long,
    val percentage: Float
)
