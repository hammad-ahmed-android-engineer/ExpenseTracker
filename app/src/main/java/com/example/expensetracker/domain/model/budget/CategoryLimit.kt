package com.example.expensetracker.domain.model.budget

data class CategoryLimit(
    val categoryId: Long,
    val categoryName: String,
    val categoryColor: String,
    val limitAmount: Long,
    val spentAmount: Long
)
