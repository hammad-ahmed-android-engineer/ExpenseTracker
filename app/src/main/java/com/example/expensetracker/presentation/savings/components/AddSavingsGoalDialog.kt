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

@Composable
fun AddSavingsGoalDialog(
    onDismiss: () -> Unit,
    onConfirm: (title: String, target: String, desc: String?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var target by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }

    AppAlertDialog(
        onDismissRequest = onDismiss,
        title = stringResource(R.string.new_savings_goal),
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)) {
                AppOutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { AppTypography.LabelMedium(text = stringResource(R.string.goal_title)) },
                    modifier = Modifier.fillMaxWidth()
                )
                AppOutlinedTextField(
                    value = target,
                    onValueChange = { target = it },
                    label = { AppTypography.LabelMedium(text = stringResource(R.string.target_amount)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                AppOutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { AppTypography.LabelMedium(text = "Description (Optional)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButtonText = stringResource(R.string.action_save),
        confirmEnabled = title.trim().isNotEmpty() && (target.toDoubleOrNull() ?: 0.0) > 0.0,
        onConfirm = { onConfirm(title, target, desc.ifBlank { null }) },
        dismissButtonText = stringResource(R.string.action_cancel),
        onDismiss = onDismiss
    )
}

