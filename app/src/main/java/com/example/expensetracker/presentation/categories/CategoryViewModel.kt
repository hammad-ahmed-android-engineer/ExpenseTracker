package com.example.expensetracker.presentation.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.domain.model.Category
import com.example.expensetracker.domain.model.Subcategory
import com.example.expensetracker.domain.model.TransactionType
import com.example.expensetracker.domain.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CategoriesUiState(
    val isLoading: Boolean = true,
    val categories: List<Category> = emptyList(),
    val selectedType: TransactionType = TransactionType.EXPENSE,
    val errorMessage: String? = null
)

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _selectedType = MutableStateFlow(TransactionType.EXPENSE)
    private val _errorMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<CategoriesUiState> = combine(
        categoryRepository.getAllCategories(),
        _selectedType,
        _errorMessage
    ) { cats, type, err ->
        CategoriesUiState(
            isLoading = false,
            categories = cats.filter { it.type == type },
            selectedType = type,
            errorMessage = err
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CategoriesUiState()
    )

    fun selectType(type: TransactionType) {
        _selectedType.value = type
    }

    fun addCategory(name: String, iconName: String, colorHex: String) {
        if (name.trim().isEmpty()) return
        val cat = Category(
            name = name.trim(),
            type = _selectedType.value,
            iconName = iconName,
            colorHex = colorHex
        )
        viewModelScope.launch {
            categoryRepository.addCategory(cat)
        }
    }

    fun addSubcategory(categoryId: Long, name: String) {
        if (name.trim().isEmpty()) return
        viewModelScope.launch {
            categoryRepository.addSubcategory(
                Subcategory(categoryId = categoryId, name = name.trim())
            )
        }
    }

    fun deleteSubcategory(subcategory: Subcategory) {
        viewModelScope.launch {
            categoryRepository.deleteSubcategory(subcategory)
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            val deleted = categoryRepository.deleteCategory(category)
            if (!deleted) {
                _errorMessage.value = "Cannot delete '${category.name}' because existing transactions use it."
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
