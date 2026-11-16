package com.example.expensetracker.presentation.transactions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.core.common.toDecimalString
import com.example.expensetracker.core.common.toMinorUnits
import com.example.expensetracker.domain.model.account.Account
import com.example.expensetracker.domain.model.category.Category
import com.example.expensetracker.domain.model.category.Subcategory
import com.example.expensetracker.domain.model.transaction.Transaction
import com.example.expensetracker.domain.model.transaction.TransactionType
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
    val errorMessage: String? = null,
    val hasUnsavedChanges: Boolean = false
)

private data class TransactionFormSnapshot(
    val title: String,
    val amountString: String,
    val type: TransactionType,
    val categoryId: Long?,
    val subcategoryId: Long?,
    val accountId: Long?,
    val destAccountId: Long?,
    val date: Long,
    val paymentMethod: String,
    val note: String,
    val tagsText: String
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

    private var initialSnapshot: TransactionFormSnapshot? = null

    init {
        loadCategoriesAndAccounts()
        if (transactionId > 0L) {
            loadTransaction(transactionId)
        }
    }

    private fun AddEditTransactionUiState.toSnapshot(): TransactionFormSnapshot =
        TransactionFormSnapshot(
            title = title.trim(),
            amountString = amountString.trim(),
            type = type,
            categoryId = selectedCategory?.id,
            subcategoryId = selectedSubcategory?.id,
            accountId = selectedAccount?.id,
            destAccountId = selectedDestAccount?.id,
            date = date,
            paymentMethod = paymentMethod,
            note = note.trim(),
            tagsText = tagsText.trim()
        )

    private fun checkHasChanges(state: AddEditTransactionUiState): Boolean {
        val initial = initialSnapshot ?: return if (state.id == 0L) {
            state.title.isNotBlank() ||
                (state.amountString != "0" && state.amountString.isNotEmpty()) ||
                state.note.isNotBlank() ||
                state.tagsText.isNotBlank() ||
                state.type != TransactionType.EXPENSE
        } else false

        return initial != state.toSnapshot()
    }

    private fun updateFormState(transform: (AddEditTransactionUiState) -> AddEditTransactionUiState) {
        _uiState.update { current ->
            val updated = transform(current)
            updated.copy(hasUnsavedChanges = checkHasChanges(updated))
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
                        val newState = state.copy(
                            categories = cats,
                            accounts = accs,
                            selectedCategory = defaultCat,
                            selectedAccount = defaultAcc,
                            selectedDestAccount = defaultDest
                        )
                        if (initialSnapshot == null && transactionId == 0L) {
                            initialSnapshot = newState.toSnapshot()
                        }
                        newState.copy(hasUnsavedChanges = checkHasChanges(newState))
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

            _uiState.update { current ->
                val loaded = current.copy(
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
                initialSnapshot = loaded.toSnapshot()
                loaded.copy(hasUnsavedChanges = false)
            }
        }
    }

    fun onTitleChange(newTitle: String) {
        updateFormState { it.copy(title = newTitle) }
    }

    fun onDigitClick(digit: String) {
        updateFormState { state ->
            if (digit == "." && state.amountString.contains(".")) return@updateFormState state
            val current = if (state.amountString == "0" && digit != ".") "" else state.amountString
            if (current.length >= 12) return@updateFormState state
            state.copy(amountString = current + digit)
        }
    }

    fun onBackspaceClick() {
        updateFormState { state ->
            val current = state.amountString
            val updated = if (current.length <= 1) "0" else current.dropLast(1)
            state.copy(amountString = updated)
        }
    }

    fun onClearClick() {
        updateFormState { it.copy(amountString = "0") }
    }

    fun onTypeChange(type: TransactionType) {
        updateFormState { state ->
            val cat = state.categories.firstOrNull { it.type == type } ?: state.selectedCategory
            state.copy(type = type, selectedCategory = cat, selectedSubcategory = null)
        }
    }

    fun onCategorySelect(category: Category) {
        updateFormState { it.copy(selectedCategory = category, selectedSubcategory = null) }
    }

    fun onSubcategorySelect(subcategory: Subcategory?) {
        updateFormState { it.copy(selectedSubcategory = subcategory) }
    }

    fun onAccountSelect(account: Account) {
        updateFormState { state ->
            val updatedDest = if (state.selectedDestAccount?.id == account.id) {
                state.accounts.firstOrNull { it.id != account.id }
            } else state.selectedDestAccount
            state.copy(selectedAccount = account, selectedDestAccount = updatedDest)
        }
    }

    fun onDestAccountSelect(account: Account) {
        updateFormState { it.copy(selectedDestAccount = account) }
    }

    fun onPaymentMethodSelect(method: String) {
        updateFormState { it.copy(paymentMethod = method) }
    }

    fun onDateChange(date: Long) {
        updateFormState { it.copy(date = date) }
    }

    fun onNoteChange(note: String) {
        updateFormState { it.copy(note = note) }
    }

    fun onTagsChange(tags: String) {
        updateFormState { it.copy(tagsText = tags) }
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
                .onSuccess { _uiState.update { it.copy(isSaved = true, hasUnsavedChanges = false) } }
                .onFailure { err -> _uiState.update { it.copy(errorMessage = err.message) } }
        }
    }
}
