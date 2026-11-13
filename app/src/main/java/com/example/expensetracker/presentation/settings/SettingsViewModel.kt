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

    suspend fun generateCsvExport(): String = manageDataBackupUseCase.exportTransactionsCsv()
    suspend fun generateJsonExport(): String = manageDataBackupUseCase.exportTransactionsJson()
    suspend fun generateFullBackup(): String = manageDataBackupUseCase.createBackupJson()

    fun inspectRestoreJson(json: String) {
        viewModelScope.launch {
            manageDataBackupUseCase.inspectBackup(json)
                .onSuccess { preview ->
                    _pendingRestore.value = PendingRestoreBackup(rawJson = json, preview = preview)
                }
                .onFailure { err ->
                    _statusMessage.value = err.message ?: "Invalid backup file"
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
