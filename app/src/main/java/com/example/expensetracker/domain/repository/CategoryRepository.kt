package com.example.expensetracker.domain.repository

import com.example.expensetracker.domain.model.Category
import com.example.expensetracker.domain.model.Subcategory
import com.example.expensetracker.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getAllCategories(): Flow<List<Category>>
    fun getCategoriesByType(type: TransactionType): Flow<List<Category>>
    suspend fun getCategoryById(id: Long): Category?
    suspend fun addCategory(category: Category): Long
    suspend fun updateCategory(category: Category)
    suspend fun deleteCategory(category: Category): Boolean // returns false if linked transactions exist
    fun getSubcategoriesForCategory(categoryId: Long): Flow<List<Subcategory>>
    suspend fun addSubcategory(subcategory: Subcategory): Long
    suspend fun deleteSubcategory(subcategory: Subcategory)
}