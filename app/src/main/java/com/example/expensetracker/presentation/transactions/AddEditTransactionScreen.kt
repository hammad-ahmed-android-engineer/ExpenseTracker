package com.example.expensetracker.presentation.transactions

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.expensetracker.R
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppAlertDialog
import com.example.expensetracker.core.designsystem.components.AppDatePickerDialog
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppIconButton
import com.example.expensetracker.core.designsystem.components.AppScaffold
import com.example.expensetracker.core.designsystem.components.AppSegmentedSelector
import com.example.expensetracker.core.designsystem.components.AppSnackbarHost
import com.example.expensetracker.core.designsystem.components.AppTopAppBar
import com.example.expensetracker.domain.model.TransactionType
import com.example.expensetracker.presentation.components.NumericKeypad
import com.example.expensetracker.presentation.transactions.components.TransactionAmountBannerCard
import com.example.expensetracker.presentation.transactions.components.TransactionFormFields

@Composable
fun AddEditTransactionScreen(
    viewModel: AddEditTransactionViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val snackbarHostState = remember { SnackbarHostState() }
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    var showDiscardDialog by rememberSaveable { mutableStateOf(false) }

    // Standard Android back press interception when unsaved changes exist
    BackHandler(enabled = state.hasUnsavedChanges) {
        showDiscardDialog = true
    }

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            onNavigateBack()
        }
    }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearError()
        }
    }

    val paymentMethods = listOf("CASH", "CARD", "BANK_TRANSFER", "DIGITAL_WALLET")

    AppScaffold(
        topBar = {
            AppTopAppBar(
                title = {
                    AppTypography.TitleLarge(
                        text = stringResource(
                            if (state.id == 0L) R.string.action_add_transaction
                            else R.string.action_edit_transaction
                        )
                    )
                },
                navigationIcon = {
                    AppIconButton(onClick = {
                        if (state.hasUnsavedChanges) {
                            showDiscardDialog = true
                        } else {
                            onNavigateBack()
                        }
                    }) {
                        AppIcon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                actions = {
                    AppIconButton(onClick = { viewModel.saveTransaction() }) {
                        AppIcon(
                            imageVector = Icons.Default.Check,
                            contentDescription = stringResource(R.string.action_save)
                        )
                    }
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
                Column(
                    modifier = Modifier
                        .weight(0.45f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = AppDimensions.paddingSmall),
                    verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
                ) {
                    TransactionAmountBannerCard(
                        amountString = state.amountString,
                        currencyCode = state.selectedAccount?.currency ?: "USD",
                        compact = true
                    )

                    NumericKeypad(
                        onDigitClick = { viewModel.onDigitClick(it) },
                        onBackspaceClick = { viewModel.onBackspaceClick() },
                        onClearClick = { viewModel.onClearClick() },
                        onDecimalClick = { viewModel.onDigitClick(".") }
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(0.55f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = AppDimensions.paddingSmall),
                    verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
                ) {
                    AppSegmentedSelector(
                        options = TransactionType.entries,
                        selectedOption = state.type,
                        onOptionSelected = { viewModel.onTypeChange(it) },
                        labelProvider = {
                            when (it) {
                                TransactionType.TRANSFER -> "Transfer"
                                TransactionType.ADJUSTMENT -> "Adjust"
                                else -> it.displayName
                            }
                        }
                    )

                    TransactionFormFields(
                        state = state,
                        paymentMethods = paymentMethods,
                        dateLabel = stringResource(R.string.date_label),
                        showPaymentMethodHeader = false,
                        onTitleChange = { viewModel.onTitleChange(it) },
                        onAccountSelect = { viewModel.onAccountSelect(it) },
                        onDestAccountSelect = { viewModel.onDestAccountSelect(it) },
                        onCategorySelect = { viewModel.onCategorySelect(it) },
                        onSubcategorySelect = { viewModel.onSubcategorySelect(it) },
                        onOpenDatePicker = { showDatePicker = true },
                        onPaymentMethodSelect = { viewModel.onPaymentMethodSelect(it) },
                        onNoteChange = { viewModel.onNoteChange(it) },
                        onTagsChange = { viewModel.onTagsChange(it) },
                        onSaveClick = { viewModel.saveTransaction() }
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(AppDimensions.paddingDefault),
                verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
            ) {
                AppSegmentedSelector(
                    options = TransactionType.entries,
                    selectedOption = state.type,
                    onOptionSelected = { viewModel.onTypeChange(it) },
                    labelProvider = {
                        when (it) {
                            TransactionType.TRANSFER -> "Transfer"
                            TransactionType.ADJUSTMENT -> "Adjust"
                            else -> it.displayName
                        }
                    }
                )

                TransactionAmountBannerCard(
                    amountString = state.amountString,
                    currencyCode = state.selectedAccount?.currency ?: "USD",
                    compact = false
                )

                NumericKeypad(
                    onDigitClick = { viewModel.onDigitClick(it) },
                    onBackspaceClick = { viewModel.onBackspaceClick() },
                    onClearClick = { viewModel.onClearClick() },
                    onDecimalClick = { viewModel.onDigitClick(".") }
                )

                TransactionFormFields(
                    state = state,
                    paymentMethods = paymentMethods,
                    dateLabel = stringResource(R.string.transaction_date),
                    showPaymentMethodHeader = true,
                    onTitleChange = { viewModel.onTitleChange(it) },
                    onAccountSelect = { viewModel.onAccountSelect(it) },
                    onDestAccountSelect = { viewModel.onDestAccountSelect(it) },
                    onCategorySelect = { viewModel.onCategorySelect(it) },
                    onSubcategorySelect = { viewModel.onSubcategorySelect(it) },
                    onOpenDatePicker = { showDatePicker = true },
                    onPaymentMethodSelect = { viewModel.onPaymentMethodSelect(it) },
                    onNoteChange = { viewModel.onNoteChange(it) },
                    onTagsChange = { viewModel.onTagsChange(it) },
                    onSaveClick = { viewModel.saveTransaction() }
                )
            }
        }
    }

    if (showDatePicker) {
        AppDatePickerDialog(
            initialSelectedDateMillis = state.date,
            onDateSelected = { viewModel.onDateChange(it) },
            onDismiss = { showDatePicker = false }
        )
    }

    if (showDiscardDialog) {
        AppAlertDialog(
            onDismissRequest = { showDiscardDialog = false },
            title = stringResource(R.string.discard_changes_title),
            text = {
                AppTypography.BodyMedium(
                    text = stringResource(R.string.discard_changes_message)
                )
            },
            confirmButtonText = stringResource(R.string.action_discard),
            onConfirm = {
                showDiscardDialog = false
                onNavigateBack()
            },
            dismissButtonText = stringResource(R.string.action_keep_editing),
            onDismiss = { showDiscardDialog = false },
            isDestructive = true
        )
    }
}
