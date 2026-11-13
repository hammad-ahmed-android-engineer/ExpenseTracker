package com.example.expensetracker.presentation.components

import androidx.compose.runtime.Composable
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppAlertDialog

@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    confirmText: String = "Delete",
    dismissText: String = "Cancel",
    isDestructive: Boolean = true,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AppAlertDialog(
        onDismissRequest = onDismiss,
        title = title,
        text = { AppTypography.BodyMedium(text = message) },
        confirmButtonText = confirmText,
        onConfirm = onConfirm,
        dismissButtonText = dismissText,
        onDismiss = onDismiss,
        isDestructive = isDestructive
    )
}
