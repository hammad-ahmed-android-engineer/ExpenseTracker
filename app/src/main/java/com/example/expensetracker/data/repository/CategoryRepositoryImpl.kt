package com.example.expensetracker.data.repository

import com.example.expensetracker.data.local.dao.CategoryDao
import com.example.expensetracker.data.local.dao.TransactionDao
import com.example.expensetracker.data.mapper.toDomain
import com.example.expensetracker.data.mapper.toEntity
import com.example.expensetracker.domain.model.Category
import com.example.expensetracker.domain.model.Subcategory
import com.example.expensetracker.domain.model.TransactionType
import com.example.expensetracker.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,
    private val transactionDao: TransactionDao
) : CategoryRepository {

    override fun getAllCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategoriesFlow().map { list ->
            list.map { entity ->
                val sub = categoryDao.getSubcategoriesForCategory(entity.id)
                entity.toDomain(sub)
            }
        }
    }

    override fun getCategoriesByType(type: TransactionType): Flow<List<Category>> {
        return categoryDao.getCategoriesByTypeFlow(type.name).map { list ->
            list.map { entity ->
                val sub = categoryDao.getSubcategoriesForCategory(entity.id)
                entity.toDomain(sub)
            }
        }
    }

    override suspend fun getCategoryById(id: Long): Category? {
        val entity = categoryDao.getCategoryById(id) ?: return null
        val sub = categoryDao.getSubcategoriesForCategory(id)
        return entity.toDomain(sub)
    }

    override suspend fun addCategory(category: Category): Long {
        val categoryId = categoryDao.insertCategory(category.toEntity())
        for (sub in category.subcategories) {
            categoryDao.insertSubcategory(sub.copy(categoryId = categoryId).toEntity())
        }
        return categoryId
    }

    override suspend fun updateCategory(category: Category) {
        categoryDao.updateCategory(category.toEntity())
    }

    override suspend fun deleteCategory(category: Category): Boolean {
        // Prevent deletion if transactions exist for this category
        val txCount = transactionDao.getTransactionCountForCategory(category.id)
        if (txCount > 0) {
            return false
        }
        categoryDao.deleteCategory(category.toEntity())
        return true
    }

    override fun getSubcategoriesForCategory(categoryId: Long): Flow<List<Subcategory>> {
        return categoryDao.getSubcategoriesForCategoryFlow(categoryId).map { list -> list.map { it.toDomain() } }
    }

    override suspend fun addSubcategory(subcategory: Subcategory): Long {
        return categoryDao.insertSubcategory(subcategory.toEntity())
    }

    override suspend fun deleteSubcategory(subcategory: Subcategory) {
        categoryDao.deleteSubcategory(subcategory.toEntity())
    }
}