package com.example.expensetracker.presentation.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.domain.model.Account
import com.example.expensetracker.domain.model.Category
import com.example.expensetracker.domain.model.Transaction
import com.example.expensetracker.domain.model.TransactionFilter
import com.example.expensetracker.domain.model.TransactionSortOrder
import com.example.expensetracker.domain.model.TransactionType
import com.example.expensetracker.domain.usecase.DeleteTransactionUseCase
import com.example.expensetracker.domain.usecase.GetAccountsUseCase
import com.example.expensetracker.domain.usecase.GetCategoriesUseCase
import com.example.expensetracker.domain.usecase.GetUserPreferencesUseCase
import com.example.expensetracker.domain.usecase.SaveTransactionUseCase
import com.example.expensetracker.domain.usecase.SearchTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TransactionsUiState(
    val isLoading: Boolean = true,
    val transactions: List<Transaction> = emptyList(),
    val categories: List<Category> = emptyList(),
    val accounts: List<Account> = emptyList(),
    val filter: TransactionFilter = TransactionFilter(),
    val currencyCode: String = "USD",
    val totalIncome: Long = 0L,
    val totalExpense: Long = 0L
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val searchTransactionsUseCase: SearchTransactionsUseCase,
    private val saveTransactionUseCase: SaveTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    getCategoriesUseCase: GetCategoriesUseCase,
    getAccountsUseCase: GetAccountsUseCase,
    getUserPreferencesUseCase: GetUserPreferencesUseCase
) : ViewModel() {

    private val _filter = MutableStateFlow(TransactionFilter())

    val uiState: StateFlow<TransactionsUiState> = combine(
        _filter.flatMapLatest { searchTransactionsUseCase(it) },
        getCategoriesUseCase(),
        getAccountsUseCase.getActiveAccounts(),
        getUserPreferencesUseCase(),
        _filter
    ) { txs, cats, accs, prefs, f ->
        val inc = txs.filter { it.type == TransactionType.INCOME || it.type == TransactionType.REFUND }
            .sumOf { it.amount }
        val exp = txs.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
        TransactionsUiState(
            isLoading = false,
            transactions = txs,
            categories = cats,
            accounts = accs,
            filter = f,
            currencyCode = prefs.defaultCurrency,
            totalIncome = inc,
            totalExpense = exp
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TransactionsUiState()
    )

    fun updateSearchQuery(query: String?) {
        _filter.update { it.copy(query = query) }
    }

    fun updateTypeFilter(type: TransactionType?) {
        _filter.update { it.copy(type = type) }
    }

    fun updateCategoryFilter(categoryId: Long?) {
        _filter.update { it.copy(categoryId = categoryId) }
    }

    fun updateAccountFilter(accountId: Long?) {
        _filter.update { it.copy(accountId = accountId) }
    }

    fun updatePaymentMethodFilter(method: String?) {
        _filter.update { it.copy(paymentMethod = method) }
    }

    fun updateSortOrder(sortOrder: TransactionSortOrder) {
        _filter.update { it.copy(sortOrder = sortOrder) }
    }

    fun clearFilters() {
        _filter.value = TransactionFilter()
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            deleteTransactionUseCase(transaction)
        }
    }

    fun duplicateTransaction(transaction: Transaction) {
        viewModelScope.launch {
            saveTransactionUseCase(transaction.copy(id = 0L, date = System.currentTimeMillis()))
        }
    }
}
