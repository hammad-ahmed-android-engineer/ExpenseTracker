package com.example.expensetracker.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.domain.model.BackupPreview
import com.example.expensetracker.domain.model.BackupRestoreMode
import com.example.expensetracker.domain.model.ColorPalette
import com.example.expensetracker.domain.model.ThemeMode
import com.example.expensetracker.domain.model.UserPreferences
import com.example.expensetracker.domain.repository.BackupRepository
import com.example.expensetracker.domain.repository.UserPreferencesRepository
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
    private val userPreferencesRepository: UserPreferencesRepository,
    private val backupRepository: BackupRepository
) : ViewModel() {

    private val _pendingRestore = MutableStateFlow<PendingRestoreBackup?>(null)
    private val _statusMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<SettingsUiState> = combine(
        userPreferencesRepository.userPreferencesFlow,
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
        viewModelScope.launch { userPreferencesRepository.updateThemeMode(mode) }
    }

    fun updateColorPalette(palette: ColorPalette) {
        viewModelScope.launch { userPreferencesRepository.updateColorPalette(palette) }
    }

    fun updateDynamicColor(enabled: Boolean) {
        viewModelScope.launch { userPreferencesRepository.updateDynamicColor(enabled) }
    }

    fun updateCurrency(currency: String) {
        viewModelScope.launch { userPreferencesRepository.updateDefaultCurrency(currency) }
    }

    fun updateBudgetAlert(enabled: Boolean) {
        viewModelScope.launch { userPreferencesRepository.updateBudgetAlert(enabled) }
    }

    fun updateRecurringReminder(enabled: Boolean) {
        viewModelScope.launch { userPreferencesRepository.updateRecurringReminder(enabled) }
    }

    fun updateBudgetThreshold(threshold: Int) {
        viewModelScope.launch { userPreferencesRepository.updateBudgetWarningThreshold(threshold) }
    }

    fun seedSampleData() {
        viewModelScope.launch {
            backupRepository.seedSampleData()
            userPreferencesRepository.updateSampleDataSeeded(true)
            _statusMessage.value = "Sample transactions, budgets, and goals added"
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            backupRepository.clearAllUserData()
            userPreferencesRepository.updateSampleDataSeeded(false)
            _statusMessage.value = "All data reset to defaults"
        }
    }

    suspend fun generateCsvExport(): String = backupRepository.exportTransactionsCsv()
    suspend fun generateJsonExport(): String = backupRepository.exportTransactionsJson()
    suspend fun generateFullBackup(): String = backupRepository.createBackupJson()

    fun inspectRestoreJson(json: String) {
        viewModelScope.launch {
            backupRepository.inspectBackup(json)
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
            backupRepository.restoreBackup(pending.rawJson, mode)
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
