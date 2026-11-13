package com.example.expensetracker.core.designsystem.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.expensetracker.R
import com.example.expensetracker.core.designsystem.AppTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDatePickerDialog(
    initialSelectedDateMillis: Long,
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialSelectedDateMillis)
    DatePickerDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        confirmButton = {
            AppTextButton(onClick = {
                datePickerState.selectedDateMillis?.let(onDateSelected)
                onDismiss()
            }) {
                AppTypography.ButtonText(text = stringResource(R.string.action_save))
            }
        },
        dismissButton = {
            AppTextButton(onClick = onDismiss) {
                AppTypography.ButtonText(
                    text = stringResource(R.string.action_cancel),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

