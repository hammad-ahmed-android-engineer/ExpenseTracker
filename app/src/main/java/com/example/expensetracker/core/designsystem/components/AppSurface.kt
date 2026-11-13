package com.example.expensetracker.core.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.rememberDebouncedClick

@Composable
fun AppSurface(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(AppDimensions.cornerLarge),
    color: Color = MaterialTheme.colorScheme.surfaceContainerLow,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    tonalElevation: Dp = AppDimensions.elevations.none,
    border: BorderStroke? = BorderStroke(
        width = AppDimensions.borders.thin,
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.55f)
    ),
    content: @Composable () -> Unit
) {
    if (onClick != null) {
        val debouncedClick = rememberDebouncedClick(onClick = onClick)
        Surface(
            onClick = debouncedClick,
            modifier = modifier,
            shape = shape,
            color = color,
            contentColor = contentColor,
            tonalElevation = tonalElevation,
            border = border,
            content = content
        )
    } else {
        Surface(
            modifier = modifier,
            shape = shape,
            color = color,
            contentColor = contentColor,
            tonalElevation = tonalElevation,
            border = border,
            content = content
        )
    }
}
