package com.example.expensetracker.presentation.budgets.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.expensetracker.R
import com.example.expensetracker.core.common.toMinorUnits
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppAlertDialog
import com.example.expensetracker.core.designsystem.components.AppOutlinedTextField
import com.example.expensetracker.domain.model.SavingsGoal

@Composable
fun AddGoalFundsDialog(
    goal: SavingsGoal,
    onConfirm: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    var fundsAmount by rememberSaveable { mutableStateOf("") }

    AppAlertDialog(
        onDismissRequest = onDismiss,
        title = stringResource(R.string.add_funds_to_goal, goal.title),
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)) {
                AppTypography.BodyMedium(text = stringResource(R.string.enter_deposit_amount))
                AppOutlinedTextField(
                    value = fundsAmount,
                    onValueChange = { fundsAmount = it },
                    label = { AppTypography.LabelMedium(text = stringResource(R.string.amount)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButtonText = stringResource(R.string.action_deposit),
        onConfirm = {
            val amt = fundsAmount.toMinorUnits()
            if (amt > 0L) {
                onConfirm(amt)
            }
            onDismiss()
        },
        dismissButtonText = stringResource(R.string.action_cancel),
        onDismiss = onDismiss
    )
}

