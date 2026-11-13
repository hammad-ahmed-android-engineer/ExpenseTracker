package com.example.expensetracker.presentation.settings

import android.content.res.Configuration
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.expensetracker.R
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppAlertDialog
import com.example.expensetracker.core.designsystem.components.AppScaffold
import com.example.expensetracker.core.designsystem.components.AppSnackbarHost
import com.example.expensetracker.core.designsystem.components.AppTopAppBar
import com.example.expensetracker.presentation.settings.components.AboutAppCard
import com.example.expensetracker.presentation.settings.components.BackupRestorePreviewDialog
import com.example.expensetracker.presentation.settings.components.DataExportSafCard
import com.example.expensetracker.presentation.settings.components.FinanceSettingsCard
import com.example.expensetracker.presentation.settings.components.NotificationsSettingsCard
import com.example.expensetracker.presentation.settings.components.PalettePickerCard
import com.example.expensetracker.presentation.settings.components.RealisticSampleDataCard
import com.example.expensetracker.presentation.settings.components.ThemeModesCard
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
    var showClearConfirm by rememberSaveable { mutableStateOf(false) }

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
                viewModel.notifyMessage("Offline backup saved via Android SAF")
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
                title = {
                    AppTypography.TitleLarge(
                        text = stringResource(R.string.settings_tools_title),
                        fontWeight = FontWeight.Bold
                    )
                }
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
                        PalettePickerCard(
                            selectedPalette = prefs.colorPalette,
                            onColorPaletteChange = { viewModel.updateColorPalette(it) }
                        )
                    }
                    item {
                        ThemeModesCard(
                            selectedMode = prefs.themeMode,
                            onThemeModeChange = { viewModel.updateThemeMode(it) }
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
                        DataExportSafCard(
                            onExportJson = { createBackupLauncher.launch("spendwise_backup.json") },
                            onExportCsv = { exportCsvLauncher.launch("spendwise_transactions.csv") },
                            onImportJson = { restoreBackupLauncher.launch(arrayOf("application/json", "*/*")) }
                        )
                    }
                    item {
                        RealisticSampleDataCard(
                            onSeedSampleData = { viewModel.seedSampleData() }
                        )
                    }
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
                    PalettePickerCard(
                        selectedPalette = prefs.colorPalette,
                        onColorPaletteChange = { viewModel.updateColorPalette(it) }
                    )
                }

                item {
                    ThemeModesCard(
                        selectedMode = prefs.themeMode,
                        onThemeModeChange = { viewModel.updateThemeMode(it) }
                    )
                }

                item {
                    DataExportSafCard(
                        onExportJson = { createBackupLauncher.launch("spendwise_backup.json") },
                        onExportCsv = { exportCsvLauncher.launch("spendwise_transactions.csv") },
                        onImportJson = { restoreBackupLauncher.launch(arrayOf("application/json", "*/*")) }
                    )
                }

                item {
                    RealisticSampleDataCard(
                        onSeedSampleData = { viewModel.seedSampleData() }
                    )
                }

                item { Spacer(modifier = Modifier.height(AppDimensions.paddingLarge)) }
            }
        }
    }

    state.pendingRestore?.let { pending ->
        BackupRestorePreviewDialog(
            preview = pending.preview,
            onDismiss = { viewModel.dismissRestorePreview() },
            onConfirm = { mode ->
                viewModel.confirmRestore(mode)
            }
        )
    }

    if (showClearConfirm) {
        AppAlertDialog(
            onDismissRequest = { showClearConfirm = false },
            title = stringResource(R.string.confirm_clear_data),
            text = { AppTypography.BodyMedium(text = stringResource(R.string.clear_data_warning)) },
            confirmButtonText = stringResource(R.string.clear_all),
            onConfirm = {
                viewModel.clearAllData()
                showClearConfirm = false
            },
            dismissButtonText = stringResource(R.string.action_cancel),
            onDismiss = { showClearConfirm = false },
            isDestructive = true
        )
    }
}
