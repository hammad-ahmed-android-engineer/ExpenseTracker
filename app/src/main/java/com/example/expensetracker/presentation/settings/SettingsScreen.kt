package com.example.expensetracker.presentation.settings

import android.content.res.Configuration
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.expensetracker.R
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppScaffold
import com.example.expensetracker.core.designsystem.components.AppSnackbarHost
import com.example.expensetracker.core.designsystem.components.AppTopAppBar
import com.example.expensetracker.presentation.components.ConfirmationDialog
import com.example.expensetracker.presentation.settings.components.AboutAppCard
import com.example.expensetracker.presentation.settings.components.BackupRestorePreviewDialog
import com.example.expensetracker.presentation.settings.components.DataBackupCard
import com.example.expensetracker.presentation.settings.components.FinanceSettingsCard
import com.example.expensetracker.presentation.settings.components.NotificationsSettingsCard
import com.example.expensetracker.presentation.settings.components.ThemeAppearanceCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel
) {
    val state by viewModel.uiState.collectAsState()
    val prefs = state.preferences
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var showClearConfirm by remember { mutableStateOf(false) }

    LaunchedEffect(state.statusMessage) {
        state.statusMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearStatusMessage()
        }
    }

    val exportCsvLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/csv")
    ) { uri ->
        uri?.let { targetUri ->
            scope.launch {
                val csv = viewModel.generateCsvExport()
                withContext(Dispatchers.IO) {
                    context.contentResolver.openOutputStream(targetUri)?.use { out ->
                        out.write(csv.toByteArray())
                    }
                }
                viewModel.notifyMessage("Transactions exported as CSV")
            }
        }
    }

    val exportJsonLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        uri?.let { targetUri ->
            scope.launch {
                val json = viewModel.generateJsonExport()
                withContext(Dispatchers.IO) {
                    context.contentResolver.openOutputStream(targetUri)?.use { out ->
                        out.write(json.toByteArray())
                    }
                }
                viewModel.notifyMessage("Transactions exported as JSON")
            }
        }
    }

    val createBackupLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        uri?.let { targetUri ->
            scope.launch {
                val backup = viewModel.generateFullBackup()
                withContext(Dispatchers.IO) {
                    context.contentResolver.openOutputStream(targetUri)?.use { out ->
                        out.write(backup.toByteArray())
                    }
                }
                viewModel.notifyMessage("Offline backup saved")
            }
        }
    }

    val restoreBackupLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let { targetUri ->
            scope.launch {
                val json = withContext(Dispatchers.IO) {
                    context.contentResolver.openInputStream(targetUri)?.bufferedReader()?.use { it.readText() }
                }
                if (json != null) {
                    viewModel.inspectRestoreJson(json)
                } else {
                    viewModel.notifyMessage("Could not read selected file")
                }
            }
        }
    }

    AppScaffold(
        topBar = {
            AppTopAppBar(
                title = { AppTypography.TitleLarge(text = stringResource(R.string.settings_title)) }
            )
        },
        snackbarHost = { AppSnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        if (isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = AppDimensions.paddingDefault),
                horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingDefault)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentPadding = PaddingValues(vertical = AppDimensions.paddingSmall),
                    verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
                ) {
                    item {
                        AppTypography.TitleMedium(
                            text = stringResource(R.string.appearance_theme),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    item {
                        ThemeAppearanceCard(
                            preferences = prefs,
                            onThemeModeChange = { viewModel.updateThemeMode(it) },
                            onColorPaletteChange = { viewModel.updateColorPalette(it) },
                            onDynamicColorChange = { viewModel.updateDynamicColor(it) }
                        )
                    }
                    item {
                        AppTypography.TitleMedium(
                            text = stringResource(R.string.finance_currency),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    item {
                        FinanceSettingsCard(
                            defaultCurrency = prefs.defaultCurrency,
                            onCurrencyChange = { viewModel.updateCurrency(it) }
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentPadding = PaddingValues(vertical = AppDimensions.paddingSmall),
                    verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
                ) {
                    item {
                        AppTypography.TitleMedium(
                            text = "Notifications & Alerts",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    item {
                        NotificationsSettingsCard(
                            preferences = prefs,
                            onBudgetAlertChange = { viewModel.updateBudgetAlert(it) },
                            onRecurringReminderChange = { viewModel.updateRecurringReminder(it) },
                            onBudgetThresholdChange = { viewModel.updateBudgetThreshold(it) }
                        )
                    }
                    item {
                        AppTypography.TitleMedium(
                            text = stringResource(R.string.sample_data_storage),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    item {
                        DataBackupCard(
                            onExportCsv = { exportCsvLauncher.launch("expenses_export.csv") },
                            onExportJson = { exportJsonLauncher.launch("expenses_export.json") },
                            onCreateBackup = { createBackupLauncher.launch("expense_tracker_backup.json") },
                            onRestoreBackup = { restoreBackupLauncher.launch(arrayOf("application/json", "*/*")) },
                            onSeedSampleData = { viewModel.seedSampleData() },
                            onClearAllData = { showClearConfirm = true }
                        )
                    }
                    item { AboutAppCard() }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = AppDimensions.paddingDefault),
                contentPadding = PaddingValues(vertical = AppDimensions.paddingSmall),
                verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
            ) {
                item {
                    AppTypography.TitleMedium(
                        text = stringResource(R.string.appearance_theme),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                item {
                    ThemeAppearanceCard(
                        preferences = prefs,
                        onThemeModeChange = { viewModel.updateThemeMode(it) },
                        onColorPaletteChange = { viewModel.updateColorPalette(it) },
                        onDynamicColorChange = { viewModel.updateDynamicColor(it) }
                    )
                }
                item {
                    AppTypography.TitleMedium(
                        text = stringResource(R.string.finance_currency),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                item {
                    FinanceSettingsCard(
                        defaultCurrency = prefs.defaultCurrency,
                        onCurrencyChange = { viewModel.updateCurrency(it) }
                    )
                }
                item {
                    AppTypography.TitleMedium(
                        text = "Notifications & Alerts",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                item {
                    NotificationsSettingsCard(
                        preferences = prefs,
                        onBudgetAlertChange = { viewModel.updateBudgetAlert(it) },
                        onRecurringReminderChange = { viewModel.updateRecurringReminder(it) },
                        onBudgetThresholdChange = { viewModel.updateBudgetThreshold(it) }
                    )
                }
                item {
                    AppTypography.TitleMedium(
                        text = stringResource(R.string.sample_data_storage),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                item {
                    DataBackupCard(
                        onExportCsv = { exportCsvLauncher.launch("expenses_export.csv") },
                        onExportJson = { exportJsonLauncher.launch("expenses_export.json") },
                        onCreateBackup = { createBackupLauncher.launch("expense_tracker_backup.json") },
                        onRestoreBackup = { restoreBackupLauncher.launch(arrayOf("application/json", "*/*")) },
                        onSeedSampleData = { viewModel.seedSampleData() },
                        onClearAllData = { showClearConfirm = true }
                    )
                }
                item { AboutAppCard() }
            }
        }
    }

    state.pendingRestore?.let { pending ->
        BackupRestorePreviewDialog(
            preview = pending.preview,
            onConfirm = { mode -> viewModel.confirmRestore(mode) },
            onDismiss = { viewModel.dismissRestorePreview() }
        )
    }

    if (showClearConfirm) {
        ConfirmationDialog(
            title = "Clear All Data",
            message = "Delete all transactions, budgets, savings goals, and recurring rules and restore default accounts?",
            confirmText = "Reset All",
            onConfirm = {
                viewModel.clearAllData()
                showClearConfirm = false
            },
            onDismiss = { showClearConfirm = false }
        )
    }
}
