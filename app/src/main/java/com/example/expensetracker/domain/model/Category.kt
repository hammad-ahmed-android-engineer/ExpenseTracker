package com.example.expensetracker.domain.model

data class Subcategory(
    val id: Long = 0,
    val categoryId: Long,
    val name: String,
    val iconName: String? = null
)

data class Category(
    val id: Long = 0,
    val name: String,
    val type: TransactionType,
    val iconName: String,
    val colorHex: String,
    val orderIndex: Int = 0,
    val isDefault: Boolean = false,
    val subcategories: List<Subcategory> = emptyList()
)