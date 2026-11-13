package com.example.expensetracker.presentation.accounts.components

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
import com.example.expensetracker.core.common.SupportedCurrency
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppAlertDialog
import com.example.expensetracker.core.designsystem.components.AppFilterChip
import com.example.expensetracker.core.designsystem.components.AppOutlinedTextField
import com.example.expensetracker.domain.model.AccountType

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddAccountDialog(
    defaultCurrency: String,
    onDismiss: () -> Unit,
    onConfirm: (name: String, type: AccountType, balance: String, currency: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(AccountType.CASH) }
    var balance by remember { mutableStateOf("0") }
    var selectedCurrency by remember { mutableStateOf(defaultCurrency) }

    AppAlertDialog(
        onDismissRequest = onDismiss,
        title = stringResource(R.string.add_new_account),
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)) {
                AppOutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { AppTypography.LabelMedium(text = stringResource(R.string.account_name)) },
                    modifier = Modifier.fillMaxWidth()
                )
                AppOutlinedTextField(
                    value = balance,
                    onValueChange = { balance = it },
                    label = { AppTypography.LabelMedium(text = stringResource(R.string.initial_balance)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                AppTypography.TitleSmall(text = stringResource(R.string.account_type))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingMicro),
                    verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMicro)
                ) {
                    AccountType.entries.forEach { type ->
                        AppFilterChip(
                            selected = selectedType == type,
                            onClick = { selectedType = type },
                            label = { AppTypography.LabelSmall(text = type.displayName) }
                        )
                    }
                }
                AppTypography.TitleSmall(text = stringResource(R.string.currency_label))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingMicro)) {
                    items(SupportedCurrency.entries) { currency ->
                        AppFilterChip(
                            selected = selectedCurrency == currency.code,
                            onClick = { selectedCurrency = currency.code },
                            label = { AppTypography.LabelSmall(text = "${currency.code} (${currency.symbol})") }
                        )
                    }
                }
            }
        },
        confirmButtonText = stringResource(R.string.action_add),
        confirmEnabled = name.trim().isNotEmpty(),
        onConfirm = { onConfirm(name, selectedType, balance, selectedCurrency) },
        dismissButtonText = stringResource(R.string.action_cancel),
        onDismiss = onDismiss
    )
}

