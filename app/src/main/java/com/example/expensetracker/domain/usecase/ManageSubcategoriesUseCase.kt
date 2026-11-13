package com.example.expensetracker.domain.usecase

import com.example.expensetracker.domain.model.Subcategory
import com.example.expensetracker.domain.repository.CategoryRepository
import javax.inject.Inject

class ManageSubcategoriesUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend fun addSubcategory(subcategory: Subcategory): Long =
        categoryRepository.addSubcategory(subcategory)

    suspend fun deleteSubcategory(subcategory: Subcategory) =
        categoryRepository.deleteSubcategory(subcategory)
}
