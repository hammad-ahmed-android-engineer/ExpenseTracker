package com.example.expensetracker.core.designsystem.components

import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.expensetracker.core.utils.rememberDebouncedClick

@Composable
fun AppIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    content: @Composable () -> Unit
) {
    val debouncedClick = rememberDebouncedClick(onClick = onClick)
    IconButton(
        onClick = debouncedClick,
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        content = content
    )
}
