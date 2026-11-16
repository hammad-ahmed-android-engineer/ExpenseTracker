package com.example.expensetracker.presentation.accounts.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.expensetracker.domain.model.Account

@Composable
fun TransferMoneyDialog(
    accounts: List<Account>,
    onDismiss: () -> Unit,
    onConfirm: (sourceId: Long, destId: Long, amount: String, note: String?) -> Unit
) {
    val activeAccounts = remember(accounts) { accounts.filterNot { it.isArchived } }
    var sourceAccId by rememberSaveable { mutableStateOf(activeAccounts.firstOrNull()?.id) }
    var destAccId by rememberSaveable { mutableStateOf(activeAccounts.getOrNull(1)?.id) }
    val sourceAcc = activeAccounts.find { it.id == sourceAccId } ?: activeAccounts.firstOrNull()
    val destAcc = activeAccounts.find { it.id == destAccId } ?: activeAccounts.firstOrNull { it.id != sourceAcc?.id }
    var amount by rememberSaveable { mutableStateOf("") }
    var note by rememberSaveable { mutableStateOf("") }

    AppAlertDialog(
        onDismissRequest = onDismiss,
        title = stringResource(R.string.transfer_money_title),
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)) {
                AppTypography.TitleSmall(text = stringResource(R.string.from_account))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)) {
                    items(activeAccounts, key = { it.id }) { acc ->
                        AppFilterChip(
                            selected = sourceAcc?.id == acc.id,
                            onClick = {
                                sourceAccId = acc.id
                                if (destAcc?.id == acc.id) {
                                    destAccId = activeAccounts.firstOrNull { it.id != acc.id }?.id
                                }
                            },
                            label = { AppTypography.LabelSmall(text = acc.name) }
                        )
                    }
                }

                AppTypography.TitleSmall(text = stringResource(R.string.to_account))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)) {
                    items(activeAccounts.filter { it.id != sourceAcc?.id }, key = { it.id }) { acc ->
                        AppFilterChip(
                            selected = destAcc?.id == acc.id,
                            onClick = { destAccId = acc.id },
                            label = { AppTypography.LabelSmall(text = acc.name) }
                        )
                    }
                }

                AppOutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { AppTypography.LabelMedium(text = stringResource(R.string.transfer_amount)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )

                AppOutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { AppTypography.LabelMedium(text = stringResource(R.string.notes_description)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButtonText = stringResource(R.string.action_transfer),
        confirmEnabled = sourceAcc != null && destAcc != null &&
            sourceAcc?.id != destAcc?.id &&
            (amount.toDoubleOrNull() ?: 0.0) > 0.0,
        onConfirm = {
            if (sourceAcc != null && destAcc != null) {
                onConfirm(sourceAcc!!.id, destAcc!!.id, amount, note.ifBlank { null })
            }
        },
        dismissButtonText = stringResource(R.string.action_cancel),
        onDismiss = onDismiss
    )
}

