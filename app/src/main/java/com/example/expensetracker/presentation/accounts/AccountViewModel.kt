package com.example.expensetracker.presentation.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.core.common.toMinorUnits
import com.example.expensetracker.domain.model.Account
import com.example.expensetracker.domain.model.AccountType
import com.example.expensetracker.domain.usecase.DeleteAccountUseCase
import com.example.expensetracker.domain.usecase.GetAccountsUseCase
import com.example.expensetracker.domain.usecase.GetUserPreferencesUseCase
import com.example.expensetracker.domain.usecase.SaveAccountUseCase
import com.example.expensetracker.domain.usecase.TransferMoneyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AccountsUiState(
    val isLoading: Boolean = true,
    val accounts: List<Account> = emptyList(),
    val totalBalance: Long = 0L,
    val currencyCode: String = "USD",
    val message: String? = null
)

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val getAccountsUseCase: GetAccountsUseCase,
    private val saveAccountUseCase: SaveAccountUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val transferMoneyUseCase: TransferMoneyUseCase,
    getUserPreferencesUseCase: GetUserPreferencesUseCase
) : ViewModel() {

    private val _message = MutableStateFlow<String?>(null)

    val uiState: StateFlow<AccountsUiState> = combine(
        getAccountsUseCase(),
        getAccountsUseCase.getTotalBalance(),
        getUserPreferencesUseCase(),
        _message
    ) { accs, total, prefs, msg ->
        AccountsUiState(
            isLoading = false,
            accounts = accs,
            totalBalance = total,
            currencyCode = prefs.defaultCurrency,
            message = msg
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AccountsUiState()
    )

    fun addOrUpdateAccount(
        id: Long = 0,
        name: String,
        type: AccountType,
        initialBalanceString: String,
        currency: String = "USD",
        colorHex: String = "#0EA5E9"
    ) {
        val balance = initialBalanceString.toMinorUnits()
        val acc = Account(
            id = id,
            name = name.trim().ifEmpty { type.displayName },
            type = type,
            initialBalance = balance,
            currentBalance = balance,
            currency = currency,
            colorHex = colorHex,
            iconName = type.defaultIcon
        )

        viewModelScope.launch {
            saveAccountUseCase(acc)
        }
    }

    fun deleteAccount(account: Account) {
        viewModelScope.launch {
            deleteAccountUseCase(account)
        }
    }

    fun transferMoney(
        sourceId: Long,
        destId: Long,
        amountString: String,
        note: String?
    ) {
        val amount = amountString.toMinorUnits()
        viewModelScope.launch {
            transferMoneyUseCase(sourceId, destId, amount, note)
                .onSuccess { _message.value = "Transfer completed" }
                .onFailure { _message.value = it.message ?: "Transfer failed" }
        }
    }

    fun archiveAccount(accountId: Long, archive: Boolean) {
        viewModelScope.launch {
            saveAccountUseCase.archiveAccount(accountId, archive)
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}
