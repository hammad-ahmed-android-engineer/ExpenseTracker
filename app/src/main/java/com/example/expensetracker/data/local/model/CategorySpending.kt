package com.example.expensetracker.data.local.model

data class CategorySpending(
    val categoryId: Long,
    val categoryName: String,
    val categoryColor: String,
    val categoryIcon: String,
    val totalAmount: Long
)
