package com.example.expensetracker.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.domain.model.BackupPreview
import com.example.expensetracker.domain.model.BackupRestoreMode
import com.example.expensetracker.domain.model.ColorPalette
import com.example.expensetracker.domain.model.ThemeMode
import com.example.expensetracker.domain.model.UserPreferences
import com.example.expensetracker.domain.usecase.GetUserPreferencesUseCase
import com.example.expensetracker.domain.usecase.ManageDataBackupUseCase
import com.example.expensetracker.domain.usecase.UpdateUserPreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PendingRestoreBackup(
    val rawJson: String,
    val preview: BackupPreview
)

data class SettingsUiState(
    val preferences: UserPreferences = UserPreferences(),
    val pendingRestore: PendingRestoreBackup? = null,
    val statusMessage: String? = null
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    getUserPreferencesUseCase: GetUserPreferencesUseCase,
    private val updateUserPreferencesUseCase: UpdateUserPreferencesUseCase,
    private val manageDataBackupUseCase: ManageDataBackupUseCase
) : ViewModel() {

    private val _pendingRestore = MutableStateFlow<PendingRestoreBackup?>(null)
    private val _statusMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<SettingsUiState> = combine(
        getUserPreferencesUseCase(),
        _pendingRestore,
        _statusMessage
    ) { prefs, pending, msg ->
        SettingsUiState(
            preferences = prefs,
            pendingRestore = pending,
            statusMessage = msg
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState()
    )

    fun updateThemeMode(mode: ThemeMode) {
        viewModelScope.launch { updateUserPreferencesUseCase.updateThemeMode(mode) }
    }

    fun updateColorPalette(palette: ColorPalette) {
        viewModelScope.launch { updateUserPreferencesUseCase.updateColorPalette(palette) }
    }

    fun updateDynamicColor(enabled: Boolean) {
        viewModelScope.launch { updateUserPreferencesUseCase.updateDynamicColor(enabled) }
    }

    fun updateCurrency(currency: String) {
        viewModelScope.launch { updateUserPreferencesUseCase.updateDefaultCurrency(currency) }
    }

    fun updateBudgetAlert(enabled: Boolean) {
        viewModelScope.launch { updateUserPreferencesUseCase.updateBudgetAlert(enabled) }
    }

    fun updateRecurringReminder(enabled: Boolean) {
        viewModelScope.launch { updateUserPreferencesUseCase.updateRecurringReminder(enabled) }
    }

    fun updateBudgetThreshold(threshold: Int) {
        viewModelScope.launch { updateUserPreferencesUseCase.updateBudgetWarningThreshold(threshold) }
    }

    fun seedSampleData() {
        viewModelScope.launch {
            manageDataBackupUseCase.seedSampleData()
            updateUserPreferencesUseCase.updateSampleDataSeeded(true)
            _statusMessage.value = "Sample transactions, budgets, and goals added"
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            manageDataBackupUseCase.clearAllUserData()
            updateUserPreferencesUseCase.updateSampleDataSeeded(false)
            _statusMessage.value = "All data reset to defaults"
        }
    }

    fun exportCsvToUri(uriString: String) {
        viewModelScope.launch {
            manageDataBackupUseCase.exportCsvToUri(uriString)
                .onSuccess { _statusMessage.value = "Transactions exported as CSV" }
                .onFailure { err -> _statusMessage.value = err.message ?: "Failed to export CSV" }
        }
    }

    fun exportJsonToUri(uriString: String) {
        viewModelScope.launch {
            manageDataBackupUseCase.exportJsonToUri(uriString)
                .onSuccess { _statusMessage.value = "Transactions exported as JSON" }
                .onFailure { err -> _statusMessage.value = err.message ?: "Failed to export JSON" }
        }
    }

    fun importCsvFromUri(uriString: String) {
        viewModelScope.launch {
            manageDataBackupUseCase.importCsvFromUri(uriString)
                .onSuccess { count ->
                    _statusMessage.value = "Successfully imported $count transactions from CSV"
                }
                .onFailure { err ->
                    _statusMessage.value = err.message ?: "Failed to import CSV"
                }
        }
    }

    fun createBackupToUri(uriString: String) {
        viewModelScope.launch {
            manageDataBackupUseCase.createBackupToUri(uriString)
                .onSuccess { _statusMessage.value = "Offline backup saved via Android SAF" }
                .onFailure { err -> _statusMessage.value = err.message ?: "Failed to save backup" }
        }
    }

    fun inspectRestoreUri(uriString: String) {
        viewModelScope.launch {
            manageDataBackupUseCase.inspectBackupFromUri(uriString)
                .onSuccess { (rawJson, preview) ->
                    _pendingRestore.value = PendingRestoreBackup(rawJson = rawJson, preview = preview)
                }
                .onFailure { err ->
                    _statusMessage.value = err.message ?: "Could not read selected file"
                }
        }
    }

    fun confirmRestore(mode: BackupRestoreMode) {
        val pending = _pendingRestore.value ?: return
        viewModelScope.launch {
            manageDataBackupUseCase.restoreBackup(pending.rawJson, mode)
                .onSuccess { preview ->
                    _pendingRestore.value = null
                    _statusMessage.value = "Restored ${preview.transactionCount} transactions & ${preview.accountCount} accounts"
                }
                .onFailure { err ->
                    _pendingRestore.value = null
                    _statusMessage.value = err.message ?: "Failed to restore backup"
                }
        }
    }

    fun dismissRestorePreview() {
        _pendingRestore.value = null
    }

    fun notifyMessage(message: String) {
        _statusMessage.value = message
    }

    fun clearStatusMessage() {
        _statusMessage.value = null
    }
}
