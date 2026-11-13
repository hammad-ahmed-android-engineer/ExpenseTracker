package com.example.expensetracker.presentation.savings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.expensetracker.core.designsystem.components.AppOutlinedTextField
import com.example.expensetracker.core.designsystem.components.AppSegmentedSelector

@Composable
fun AddContributionDialog(
    goalTitle: String,
    onDismiss: () -> Unit,
    onConfirm: (amount: String, isDeposit: Boolean) -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var isDeposit by remember { mutableStateOf(true) }
    val depositLabel = stringResource(R.string.deposit)
    val withdrawLabel = stringResource(R.string.withdraw)

    AppAlertDialog(
        onDismissRequest = onDismiss,
        title = "Savings for $goalTitle",
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)) {
                AppSegmentedSelector(
                    options = listOf(true, false),
                    selectedOption = isDeposit,
                    onOptionSelected = { isDeposit = it },
                    labelProvider = { if (it) depositLabel else withdrawLabel }
                )
                AppOutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { AppTypography.LabelMedium(text = stringResource(R.string.amount)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButtonText = stringResource(R.string.action_submit),
        confirmEnabled = (amount.toDoubleOrNull() ?: 0.0) > 0.0,
        onConfirm = { onConfirm(amount, isDeposit) },
        dismissButtonText = stringResource(R.string.action_cancel),
        onDismiss = onDismiss
    )
}

