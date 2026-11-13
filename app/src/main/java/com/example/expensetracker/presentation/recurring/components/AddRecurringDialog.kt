package com.example.expensetracker.presentation.recurring.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.expensetracker.R
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppAlertDialog
import com.example.expensetracker.core.designsystem.components.AppFilterChip
import com.example.expensetracker.core.designsystem.components.AppOutlinedTextField
import com.example.expensetracker.core.designsystem.components.AppSegmentedSelector
import com.example.expensetracker.domain.model.Account
import com.example.expensetracker.domain.model.Category
import com.example.expensetracker.domain.model.RecurringFrequency
import com.example.expensetracker.domain.model.TransactionType

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddRecurringDialog(
    categories: List<Category>,
    accounts: List<Account>,
    onDismiss: () -> Unit,
    onConfirm: (
        title: String,
        amount: String,
        type: TransactionType,
        frequency: RecurringFrequency,
        categoryId: Long,
        accountId: Long
    ) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(TransactionType.EXPENSE) }
    var selectedFreq by remember { mutableStateOf(RecurringFrequency.MONTHLY) }
    val filteredCats = remember(categories, selectedType) { categories.filter { it.type == selectedType } }
    var selectedCategory by remember(filteredCats) { mutableStateOf(filteredCats.firstOrNull()) }
    var selectedAccount by remember(accounts) { mutableStateOf(accounts.firstOrNull()) }

    AppAlertDialog(
        onDismissRequest = onDismiss,
        title = "New Recurring Rule",
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)) {
                AppSegmentedSelector(
                    options = listOf(TransactionType.EXPENSE, TransactionType.INCOME),
                    selectedOption = selectedType,
                    onOptionSelected = { selectedType = it },
                    labelProvider = { it.displayName }
                )
                AppOutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { AppTypography.LabelMedium(text = "Title (e.g. Internet Bill)") },
                    modifier = Modifier.fillMaxWidth()
                )
                AppOutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { AppTypography.LabelMedium(text = "Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                AppTypography.TitleSmall(text = stringResource(R.string.frequency))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingMicro),
                    verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMicro)
                ) {
                    RecurringFrequency.entries.forEach { freq ->
                        AppFilterChip(
                            selected = selectedFreq == freq,
                            onClick = { selectedFreq = freq },
                            label = { AppTypography.LabelSmall(text = freq.displayName) }
                        )
                    }
                }
                if (accounts.isNotEmpty()) {
                    AppTypography.TitleSmall(text = stringResource(R.string.select_account))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)) {
                        items(accounts, key = { it.id }) { acc ->
                            AppFilterChip(
                                selected = selectedAccount?.id == acc.id,
                                onClick = { selectedAccount = acc },
                                label = { AppTypography.LabelSmall(text = acc.name) }
                            )
                        }
                    }
                }
                if (filteredCats.isNotEmpty()) {
                    AppTypography.TitleSmall(text = stringResource(R.string.select_category))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)) {
                        items(filteredCats, key = { it.id }) { cat ->
                            AppFilterChip(
                                selected = selectedCategory?.id == cat.id,
                                onClick = { selectedCategory = cat },
                                label = { AppTypography.LabelSmall(text = cat.name) }
                            )
                        }
                    }
                }
            }
        },
        confirmButtonText = stringResource(R.string.action_save),
        confirmEnabled = (amount.toDoubleOrNull() ?: 0.0) > 0.0,
        onConfirm = {
            onConfirm(
                title,
                amount,
                selectedType,
                selectedFreq,
                selectedCategory?.id ?: 1L,
                selectedAccount?.id ?: 1L
            )
        },
        dismissButtonText = stringResource(R.string.action_cancel),
        onDismiss = onDismiss
    )
}

