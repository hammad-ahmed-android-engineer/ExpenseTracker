package com.example.expensetracker.domain.model.category

data class Subcategory(
    val id: Long = 0,
    val categoryId: Long,
    val name: String,
    val iconName: String? = null
)
