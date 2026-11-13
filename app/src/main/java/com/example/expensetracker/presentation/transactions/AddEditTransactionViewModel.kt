package com.example.expensetracker.presentation.transactions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.core.common.toDecimalString
import com.example.expensetracker.core.common.toMinorUnits
import com.example.expensetracker.domain.model.Account
import com.example.expensetracker.domain.model.Category
import com.example.expensetracker.domain.model.Subcategory
import com.example.expensetracker.domain.model.Transaction
import com.example.expensetracker.domain.model.TransactionType
import com.example.expensetracker.domain.usecase.GetAccountsUseCase
import com.example.expensetracker.domain.usecase.GetCategoriesUseCase
import com.example.expensetracker.domain.usecase.GetTransactionByIdUseCase
import com.example.expensetracker.domain.usecase.SaveTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddEditTransactionUiState(
    val id: Long = 0,
    val title: String = "",
    val amountString: String = "0",
    val type: TransactionType = TransactionType.EXPENSE,
    val selectedCategory: Category? = null,
    val selectedSubcategory: Subcategory? = null,
    val selectedAccount: Account? = null,
    val selectedDestAccount: Account? = null,
    val date: Long = System.currentTimeMillis(),
    val paymentMethod: String = "CASH",
    val note: String = "",
    val tagsText: String = "",
    val categories: List<Category> = emptyList(),
    val accounts: List<Account> = emptyList(),
    val isSaved: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class AddEditTransactionViewModel @Inject constructor(
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val saveTransactionUseCase: SaveTransactionUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val transactionId: Long = savedStateHandle.get<Long>("transactionId") ?: 0L

    private val _uiState = MutableStateFlow(AddEditTransactionUiState(id = transactionId))
    val uiState: StateFlow<AddEditTransactionUiState> = _uiState.asStateFlow()

    init {
        loadCategoriesAndAccounts()
        if (transactionId > 0L) {
            loadTransaction(transactionId)
        }
    }

    private fun loadCategoriesAndAccounts() {
        viewModelScope.launch {
            combine(
                getCategoriesUseCase(),
                getAccountsUseCase.getActiveAccounts()
            ) { cats, accs -> cats to accs }
                .collect { (cats, accs) ->
                    _uiState.update { state ->
                        val defaultCat = state.selectedCategory
                            ?: cats.firstOrNull { it.type == state.type }
                            ?: cats.firstOrNull()
                        val defaultAcc = state.selectedAccount ?: accs.firstOrNull()
                        val defaultDest = state.selectedDestAccount
                            ?: accs.firstOrNull { it.id != defaultAcc?.id }
                        state.copy(
                            categories = cats,
                            accounts = accs,
                            selectedCategory = defaultCat,
                            selectedAccount = defaultAcc,
                            selectedDestAccount = defaultDest
                        )
                    }
                }
        }
    }

    private fun loadTransaction(id: Long) {
        viewModelScope.launch {
            val tx = getTransactionByIdUseCase(id) ?: return@launch
            val cat = getCategoriesUseCase.getCategoryById(tx.categoryId)
            val acc = getAccountsUseCase.getAccountById(tx.accountId)
            val destAcc = tx.destinationAccountId?.let { getAccountsUseCase.getAccountById(it) }
            val sub = cat?.subcategories?.find { it.id == tx.subcategoryId }

            _uiState.update {
                it.copy(
                    id = tx.id,
                    title = tx.title,
                    amountString = tx.amount.toDecimalString(),
                    type = tx.type,
                    selectedCategory = cat,
                    selectedSubcategory = sub,
                    selectedAccount = acc,
                    selectedDestAccount = destAcc,
                    date = tx.date,
                    paymentMethod = tx.paymentMethod,
                    note = tx.note ?: "",
                    tagsText = tx.tags.joinToString(", ")
                )
            }
        }
    }

    fun onTitleChange(newTitle: String) {
        _uiState.update { it.copy(title = newTitle) }
    }

    fun onDigitClick(digit: String) {
        _uiState.update { state ->
            if (digit == "." && state.amountString.contains(".")) return@update state
            val current = if (state.amountString == "0" && digit != ".") "" else state.amountString
            if (current.length >= 12) return@update state
            state.copy(amountString = current + digit)
        }
    }

    fun onBackspaceClick() {
        _uiState.update { state ->
            val current = state.amountString
            val updated = if (current.length <= 1) "0" else current.dropLast(1)
            state.copy(amountString = updated)
        }
    }

    fun onClearClick() {
        _uiState.update { it.copy(amountString = "0") }
    }

    fun onTypeChange(type: TransactionType) {
        _uiState.update { state ->
            val cat = state.categories.firstOrNull { it.type == type } ?: state.selectedCategory
            state.copy(type = type, selectedCategory = cat, selectedSubcategory = null)
        }
    }

    fun onCategorySelect(category: Category) {
        _uiState.update { it.copy(selectedCategory = category, selectedSubcategory = null) }
    }

    fun onSubcategorySelect(subcategory: Subcategory?) {
        _uiState.update { it.copy(selectedSubcategory = subcategory) }
    }

    fun onAccountSelect(account: Account) {
        _uiState.update { state ->
            val updatedDest = if (state.selectedDestAccount?.id == account.id) {
                state.accounts.firstOrNull { it.id != account.id }
            } else state.selectedDestAccount
            state.copy(selectedAccount = account, selectedDestAccount = updatedDest)
        }
    }

    fun onDestAccountSelect(account: Account) {
        _uiState.update { it.copy(selectedDestAccount = account) }
    }

    fun onPaymentMethodSelect(method: String) {
        _uiState.update { it.copy(paymentMethod = method) }
    }

    fun onDateChange(date: Long) {
        _uiState.update { it.copy(date = date) }
    }

    fun onNoteChange(note: String) {
        _uiState.update { it.copy(note = note) }
    }

    fun onTagsChange(tags: String) {
        _uiState.update { it.copy(tagsText = tags) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun saveTransaction() {
        val state = _uiState.value
        val amountMinor = state.amountString.toMinorUnits()
        val categoryId = state.selectedCategory?.id ?: 1L
        val accountId = state.selectedAccount?.id ?: 1L
        val title = state.title.trim().ifEmpty {
            if (state.type == TransactionType.TRANSFER) "Account Transfer"
            else state.selectedCategory?.name ?: "Transaction"
        }
        val tags = state.tagsText.split(",").map { it.trim() }.filter { it.isNotEmpty() }

        val tx = Transaction(
            id = state.id,
            title = title,
            amount = amountMinor,
            type = state.type,
            categoryId = categoryId,
            subcategoryId = state.selectedSubcategory?.id,
            accountId = accountId,
            destinationAccountId = if (state.type == TransactionType.TRANSFER) state.selectedDestAccount?.id else null,
            date = state.date,
            paymentMethod = state.paymentMethod,
            note = state.note.trim().ifEmpty { null },
            tags = tags
        )

        viewModelScope.launch {
            saveTransactionUseCase(tx)
                .onSuccess { _uiState.update { it.copy(isSaved = true) } }
                .onFailure { err -> _uiState.update { it.copy(errorMessage = err.message) } }
        }
    }
}
