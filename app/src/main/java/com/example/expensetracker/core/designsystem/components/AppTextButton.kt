package com.example.expensetracker.core.designsystem.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.rememberDebouncedClick

@Composable
fun AppTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.textButtonColors(
        contentColor = MaterialTheme.colorScheme.primary
    ),
    content: @Composable RowScope.() -> Unit
) {
    val debouncedClick = rememberDebouncedClick(onClick = onClick)
    TextButton(
        onClick = debouncedClick,
        modifier = modifier,
        enabled = enabled,
        shape = RoundedCornerShape(AppDimensions.cornerMedium),
        colors = colors,
        content = content
    )
}
