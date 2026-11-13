package com.example.expensetracker.domain.usecase

import com.example.expensetracker.domain.model.Category
import com.example.expensetracker.domain.model.TransactionType
import com.example.expensetracker.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    operator fun invoke(): Flow<List<Category>> = categoryRepository.getAllCategories()

    fun getByType(type: TransactionType): Flow<List<Category>> = categoryRepository.getCategoriesByType(type)

    suspend fun getCategoryById(id: Long): Category? = categoryRepository.getCategoryById(id)
}

