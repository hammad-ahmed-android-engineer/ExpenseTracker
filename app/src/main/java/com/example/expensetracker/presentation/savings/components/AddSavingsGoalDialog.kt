package com.example.expensetracker.presentation.savings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.example.expensetracker.core.designsystem.components.AppOutlinedTextField

@Composable
fun AddSavingsGoalDialog(
    onConfirm: (title: String, targetAmount: String, description: String?) -> Unit,
    onDismiss: () -> Unit
) {
    var addGoalTitle by rememberSaveable { mutableStateOf("") }
    var addGoalTarget by rememberSaveable { mutableStateOf("") }
    var addGoalDesc by rememberSaveable { mutableStateOf("") }

    AppAlertDialog(
        onDismissRequest = onDismiss,
        title = stringResource(R.string.new_savings_goal),
        icon = Icons.Default.Savings,
        subtitle = stringResource(R.string.new_savings_goal_subtitle),
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)) {
                AppOutlinedTextField(
                    value = addGoalTitle,
                    onValueChange = { addGoalTitle = it },
                    label = { AppTypography.LabelMedium(text = stringResource(R.string.goal_title)) },
                    placeholder = { AppTypography.BodyMedium(text = stringResource(R.string.goal_title_placeholder)) },
                    modifier = Modifier.fillMaxWidth()
                )
                AppOutlinedTextField(
                    value = addGoalTarget,
                    onValueChange = { addGoalTarget = it },
                    label = { AppTypography.LabelMedium(text = stringResource(R.string.target_amount)) },
                    placeholder = { AppTypography.BodyMedium(text = stringResource(R.string.target_amount_placeholder)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                AppOutlinedTextField(
                    value = addGoalDesc,
                    onValueChange = { addGoalDesc = it },
                    label = { AppTypography.LabelMedium(text = stringResource(R.string.goal_desc_label)) },
                    placeholder = { AppTypography.BodyMedium(text = stringResource(R.string.goal_desc_placeholder)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButtonText = stringResource(R.string.action_save),
        confirmEnabled = addGoalTitle.trim().isNotEmpty() && (addGoalTarget.toDoubleOrNull() ?: 0.0) > 0.0,
        onConfirm = {
            onConfirm(addGoalTitle, addGoalTarget, addGoalDesc.ifBlank { null })
            onDismiss()
        },
        dismissButtonText = stringResource(R.string.action_cancel),
        onDismiss = onDismiss
    )
}

