package com.example.expensetracker.presentation.savings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Savings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import com.example.expensetracker.core.designsystem.components.AppSegmentedSelector
import com.example.expensetracker.domain.model.SavingsGoal

@Composable
fun GoalFundsDialog(
    goal: SavingsGoal,
    onConfirm: (amount: String, isDeposit: Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    var depositAmount by rememberSaveable { mutableStateOf("") }
    var depositIsDeposit by rememberSaveable { mutableStateOf(true) }

    val depositLabel = stringResource(R.string.deposit)
    val withdrawLabel = stringResource(R.string.withdraw)
    val quickAmounts = listOf("10", "25", "50", "100")

    AppAlertDialog(
        onDismissRequest = onDismiss,
        title = stringResource(R.string.funds_for_goal, goal.title),
        icon = Icons.Default.Savings,
        subtitle = stringResource(R.string.funds_for_goal_desc),
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)) {
                AppSegmentedSelector(
                    options = listOf(true, false),
                    selectedOption = depositIsDeposit,
                    onOptionSelected = { depositIsDeposit = it },
                    labelProvider = { if (it) depositLabel else withdrawLabel }
                )

                AppOutlinedTextField(
                    value = depositAmount,
                    onValueChange = { depositAmount = it },
                    label = { AppTypography.LabelMedium(text = stringResource(R.string.amount)) },
                    placeholder = { AppTypography.BodyMedium(text = stringResource(R.string.amount_placeholder)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
                ) {
                    quickAmounts.forEach { quickVal ->
                        AppFilterChip(
                            selected = depositAmount == quickVal,
                            onClick = { depositAmount = quickVal },
                            label = { AppTypography.LabelSmall(text = "+$$quickVal") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        },
        confirmButtonText = stringResource(R.string.action_submit),
        confirmEnabled = (depositAmount.toDoubleOrNull() ?: 0.0) > 0.0,
        onConfirm = {
            onConfirm(depositAmount, depositIsDeposit)
            onDismiss()
        },
        dismissButtonText = stringResource(R.string.action_cancel),
        onDismiss = onDismiss
    )
}

