package com.example.expensetracker.presentation.transactions

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import com.example.expensetracker.R
import com.example.expensetracker.core.common.formatDate
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppButton
import com.example.expensetracker.core.designsystem.components.AppCard
import com.example.expensetracker.core.designsystem.components.AppDatePickerDialog
import com.example.expensetracker.core.designsystem.components.AppFilterChip
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppIconButton
import com.example.expensetracker.core.designsystem.components.AppOutlinedButton
import com.example.expensetracker.core.designsystem.components.AppOutlinedTextField
import com.example.expensetracker.core.designsystem.components.AppScaffold
import com.example.expensetracker.core.designsystem.components.AppSegmentedSelector
import com.example.expensetracker.core.designsystem.components.AppSnackbarHost
import com.example.expensetracker.core.designsystem.components.AppTopAppBar
import com.example.expensetracker.domain.model.TransactionType
import com.example.expensetracker.presentation.components.NumericKeypad
import com.example.expensetracker.presentation.transactions.components.CategoryChipItem

@Composable
fun AddEditTransactionScreen(
    viewModel: AddEditTransactionViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val snackbarHostState = remember { SnackbarHostState() }
    var showDatePicker by remember { mutableStateOf(false) }

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
                    AppIconButton(onClick = onNavigateBack) {
                        AppIcon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
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
                    AppCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(AppDimensions.cornerExtraLarge),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        border = null
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(AppDimensions.paddingDefault),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AppTypography.LabelSmall(
                                text = stringResource(R.string.amount),
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                            )
                            Spacer(modifier = Modifier.height(AppDimensions.paddingMicro))
                            AppTypography.DisplayMedium(
                                text = "${state.amountString} ${state.selectedAccount?.currency ?: "USD"}",
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

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

                    AppOutlinedTextField(
                        value = state.title,
                        onValueChange = { viewModel.onTitleChange(it) },
                        label = { AppTypography.LabelMedium(text = stringResource(R.string.title_optional)) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    AppTypography.TitleSmall(text = stringResource(R.string.select_account))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)) {
                        items(state.accounts, key = { it.id }) { acc ->
                            AppFilterChip(
                                selected = state.selectedAccount?.id == acc.id,
                                onClick = { viewModel.onAccountSelect(acc) },
                                label = { AppTypography.LabelMedium(text = acc.name) }
                            )
                        }
                    }

                    if (state.type == TransactionType.TRANSFER) {
                        AppTypography.TitleSmall(text = stringResource(R.string.select_destination_account))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)) {
                            items(state.accounts.filter { it.id != state.selectedAccount?.id }, key = { it.id }) { acc ->
                                AppFilterChip(
                                    selected = state.selectedDestAccount?.id == acc.id,
                                    onClick = { viewModel.onDestAccountSelect(acc) },
                                    label = { AppTypography.LabelMedium(text = acc.name) }
                                )
                            }
                        }
                    } else {
                        AppTypography.TitleSmall(text = stringResource(R.string.select_category))
                        val catType = if (state.type == TransactionType.INCOME || state.type == TransactionType.REFUND) {
                            TransactionType.INCOME
                        } else TransactionType.EXPENSE
                        val filteredCats = state.categories.filter { it.type == catType }
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)) {
                            items(filteredCats, key = { it.id }) { cat ->
                                CategoryChipItem(
                                    category = cat,
                                    isSelected = state.selectedCategory?.id == cat.id,
                                    onClick = { viewModel.onCategorySelect(cat) }
                                )
                            }
                        }
                        val subs = state.selectedCategory?.subcategories.orEmpty()
                        if (subs.isNotEmpty()) {
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)) {
                                items(subs, key = { it.id }) { sub ->
                                    AppFilterChip(
                                        selected = state.selectedSubcategory?.id == sub.id,
                                        onClick = {
                                            viewModel.onSubcategorySelect(
                                                if (state.selectedSubcategory?.id == sub.id) null else sub
                                            )
                                        },
                                        label = { AppTypography.LabelSmall(text = sub.name) }
                                    )
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppTypography.TitleSmall(text = "Date")
                        AppOutlinedButton(onClick = { showDatePicker = true }) {
                            AppTypography.ButtonText(text = state.date.formatDate("MMM dd, yyyy"))
                        }
                    }

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)) {
                        items(paymentMethods) { method ->
                            AppFilterChip(
                                selected = state.paymentMethod == method,
                                onClick = { viewModel.onPaymentMethodSelect(method) },
                                label = { AppTypography.LabelSmall(text = method.replace("_", " ")) }
                            )
                        }
                    }

                    AppOutlinedTextField(
                        value = state.note,
                        onValueChange = { viewModel.onNoteChange(it) },
                        label = { AppTypography.LabelMedium(text = stringResource(R.string.notes_description)) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    AppOutlinedTextField(
                        value = state.tagsText,
                        onValueChange = { viewModel.onTagsChange(it) },
                        label = { AppTypography.LabelMedium(text = "Tags (comma separated)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    AppButton(
                        onClick = { viewModel.saveTransaction() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AppTypography.ButtonText(
                            text = stringResource(
                                if (state.id == 0L) R.string.save_transaction
                                else R.string.update_transaction
                            )
                        )
                    }
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

                AppCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(AppDimensions.cornerExtraLarge),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    border = null
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(AppDimensions.paddingLarge),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AppTypography.LabelSmall(
                            text = stringResource(R.string.amount),
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(AppDimensions.paddingMicro))
                        AppTypography.DisplayLarge(
                            text = "${state.amountString} ${state.selectedAccount?.currency ?: "USD"}",
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                NumericKeypad(
                    onDigitClick = { viewModel.onDigitClick(it) },
                    onBackspaceClick = { viewModel.onBackspaceClick() },
                    onClearClick = { viewModel.onClearClick() },
                    onDecimalClick = { viewModel.onDigitClick(".") }
                )

                AppOutlinedTextField(
                    value = state.title,
                    onValueChange = { viewModel.onTitleChange(it) },
                    label = { AppTypography.LabelMedium(text = stringResource(R.string.title_optional)) },
                    modifier = Modifier.fillMaxWidth()
                )

                AppTypography.TitleSmall(text = stringResource(R.string.select_account))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)) {
                    items(state.accounts, key = { it.id }) { acc ->
                        AppFilterChip(
                            selected = state.selectedAccount?.id == acc.id,
                            onClick = { viewModel.onAccountSelect(acc) },
                            label = { AppTypography.LabelMedium(text = acc.name) }
                        )
                    }
                }

                if (state.type == TransactionType.TRANSFER) {
                    AppTypography.TitleSmall(text = stringResource(R.string.select_destination_account))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)) {
                        items(state.accounts.filter { it.id != state.selectedAccount?.id }, key = { it.id }) { acc ->
                            AppFilterChip(
                                selected = state.selectedDestAccount?.id == acc.id,
                                onClick = { viewModel.onDestAccountSelect(acc) },
                                label = { AppTypography.LabelMedium(text = acc.name) }
                            )
                        }
                    }
                } else {
                    AppTypography.TitleSmall(text = stringResource(R.string.select_category))
                    val catType = if (state.type == TransactionType.INCOME || state.type == TransactionType.REFUND) {
                        TransactionType.INCOME
                    } else TransactionType.EXPENSE
                    val filteredCats = state.categories.filter { it.type == catType }
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)) {
                        items(filteredCats, key = { it.id }) { cat ->
                            CategoryChipItem(
                                category = cat,
                                isSelected = state.selectedCategory?.id == cat.id,
                                onClick = { viewModel.onCategorySelect(cat) }
                            )
                        }
                    }
                    val subs = state.selectedCategory?.subcategories.orEmpty()
                    if (subs.isNotEmpty()) {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)) {
                            items(subs, key = { it.id }) { sub ->
                                AppFilterChip(
                                    selected = state.selectedSubcategory?.id == sub.id,
                                    onClick = {
                                        viewModel.onSubcategorySelect(
                                            if (state.selectedSubcategory?.id == sub.id) null else sub
                                        )
                                    },
                                    label = { AppTypography.LabelSmall(text = sub.name) }
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppTypography.TitleSmall(text = "Transaction Date")
                    AppOutlinedButton(onClick = { showDatePicker = true }) {
                        AppTypography.ButtonText(text = state.date.formatDate("MMM dd, yyyy"))
                    }
                }

                AppTypography.TitleSmall(text = "Payment Method")
                LazyRow(horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)) {
                    items(paymentMethods) { method ->
                        AppFilterChip(
                            selected = state.paymentMethod == method,
                            onClick = { viewModel.onPaymentMethodSelect(method) },
                            label = { AppTypography.LabelSmall(text = method.replace("_", " ")) }
                        )
                    }
                }

                AppOutlinedTextField(
                    value = state.note,
                    onValueChange = { viewModel.onNoteChange(it) },
                    label = { AppTypography.LabelMedium(text = stringResource(R.string.notes_description)) },
                    modifier = Modifier.fillMaxWidth()
                )

                AppOutlinedTextField(
                    value = state.tagsText,
                    onValueChange = { viewModel.onTagsChange(it) },
                    label = { AppTypography.LabelMedium(text = "Tags (comma separated)") },
                    modifier = Modifier.fillMaxWidth()
                )

                AppButton(
                    onClick = { viewModel.saveTransaction() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AppTypography.ButtonText(
                        text = stringResource(
                            if (state.id == 0L) R.string.save_transaction
                            else R.string.update_transaction
                        )
                    )
                }
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
}
