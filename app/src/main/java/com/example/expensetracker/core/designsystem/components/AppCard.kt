package com.example.expensetracker.core.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.rememberDebouncedClick

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(AppDimensions.cornerLarge),
    colors: CardColors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
    ),
    border: BorderStroke? = BorderStroke(
        width = AppDimensions.borders.thin,
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.65f)
    ),
    content: @Composable () -> Unit
) {
    if (onClick != null) {
        val debouncedClick = rememberDebouncedClick(onClick = onClick)
        Card(
            onClick = debouncedClick,
            modifier = modifier,
            shape = shape,
            colors = colors,
            border = border,
            elevation = CardDefaults.cardElevation(defaultElevation = AppDimensions.elevations.none)
        ) {
            content()
        }
    } else {
        Card(
            modifier = modifier,
            shape = shape,
            colors = colors,
            border = border,
            elevation = CardDefaults.cardElevation(defaultElevation = AppDimensions.elevations.none)
        ) {
            content()
        }
    }
}
