package com.example.expensetracker.core.designsystem.components

import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.expensetracker.core.designsystem.rememberDebouncedCallback

@Composable
fun AppCheckbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val debouncedChange = onCheckedChange?.let { rememberDebouncedCallback(onAction = it) }
    Checkbox(
        checked = checked,
        onCheckedChange = debouncedChange,
        modifier = modifier,
        enabled = enabled,
        colors = CheckboxDefaults.colors(
            checkedColor = MaterialTheme.colorScheme.primary,
            checkmarkColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}
