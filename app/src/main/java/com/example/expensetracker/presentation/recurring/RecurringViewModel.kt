package com.example.expensetracker.presentation.recurring

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.core.common.toMinorUnits
import com.example.expensetracker.domain.model.Account
import com.example.expensetracker.domain.model.Category
import com.example.expensetracker.domain.model.RecurringFrequency
import com.example.expensetracker.domain.model.RecurringTransaction
import com.example.expensetracker.domain.model.TransactionType
import com.example.expensetracker.domain.repository.AccountRepository
import com.example.expensetracker.domain.repository.CategoryRepository
import com.example.expensetracker.domain.repository.RecurringTransactionRepository
import com.example.expensetracker.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecurringUiState(
    val isLoading: Boolean = true,
    val recurringList: List<RecurringTransaction> = emptyList(),
    val categories: List<Category> = emptyList(),
    val accounts: List<Account> = emptyList(),
    val currencyCode: String = "USD",
    val message: String? = null
)

@HiltViewModel
class RecurringViewModel @Inject constructor(
    private val recurringRepository: RecurringTransactionRepository,
    categoryRepository: CategoryRepository,
    accountRepository: AccountRepository,
    userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _message = MutableStateFlow<String?>(null)

    val uiState: StateFlow<RecurringUiState> = combine(
        recurringRepository.getAllRecurring(),
        categoryRepository.getAllCategories(),
        accountRepository.getAllActiveAccounts(),
        userPreferencesRepository.userPreferencesFlow,
        _message
    ) { list, cats, accs, prefs, msg ->
        RecurringUiState(
            isLoading = false,
            recurringList = list,
            categories = cats,
            accounts = accs,
            currencyCode = prefs.defaultCurrency,
            message = msg
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RecurringUiState()
    )

    fun addRecurring(
        title: String,
        amountString: String,
        type: TransactionType,
        frequency: RecurringFrequency,
        categoryId: Long,
        accountId: Long
    ) {
        val amount = amountString.toMinorUnits()
        if (amount <= 0L) return

        val now = System.currentTimeMillis()
        val recurring = RecurringTransaction(
            title = title.trim().ifEmpty { "Subscription / Bill" },
            amount = amount,
            type = type,
            categoryId = categoryId,
            accountId = accountId,
            frequency = frequency,
            startDate = now,
            nextDueDate = now
        )

        viewModelScope.launch {
            recurringRepository.addRecurring(recurring)
        }
    }

    fun toggleEnable(recurring: RecurringTransaction) {
        viewModelScope.launch {
            recurringRepository.updateRecurring(recurring.copy(isEnabled = !recurring.isEnabled))
        }
    }

    fun deleteRecurring(recurring: RecurringTransaction) {
        viewModelScope.launch {
            recurringRepository.deleteRecurring(recurring)
        }
    }

    fun triggerProcessDueNow() {
        viewModelScope.launch {
            val count = recurringRepository.processDueRecurringTransactions()
            _message.value = if (count > 0) {
                "$count scheduled transaction(s) generated"
            } else {
                "No due recurring transactions right now"
            }
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}
