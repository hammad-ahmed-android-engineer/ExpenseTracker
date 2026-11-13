package com.example.expensetracker.core.designsystem.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.expensetracker.core.designsystem.rememberDebouncedClick

@Composable
fun AppRadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val debouncedClick = onClick?.let { rememberDebouncedClick(onClick = it) }
    RadioButton(
        selected = selected,
        onClick = debouncedClick,
        modifier = modifier,
        enabled = enabled,
        colors = RadioButtonDefaults.colors(
            selectedColor = MaterialTheme.colorScheme.primary,
            unselectedColor = MaterialTheme.colorScheme.outline
        )
    )
}
