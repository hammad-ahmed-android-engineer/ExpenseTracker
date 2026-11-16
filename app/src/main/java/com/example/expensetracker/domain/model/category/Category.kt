package com.example.expensetracker.domain.model.category

import com.example.expensetracker.domain.model.transaction.TransactionType

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
