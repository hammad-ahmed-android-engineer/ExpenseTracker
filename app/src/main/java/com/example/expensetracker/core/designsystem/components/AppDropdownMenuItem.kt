package com.example.expensetracker.core.designsystem.components

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.expensetracker.core.utils.rememberDebouncedClick

@Composable
fun AppDropdownMenuItem(
    text: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true
) {
    val debouncedClick = rememberDebouncedClick(onClick = onClick)
    DropdownMenuItem(
        text = text,
        onClick = debouncedClick,
        modifier = modifier,
        leadingIcon = leadingIcon,
        enabled = enabled
    )
}

