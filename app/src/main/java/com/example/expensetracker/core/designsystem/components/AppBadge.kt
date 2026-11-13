package com.example.expensetracker.core.designsystem.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography

@Composable
fun AppBadge(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(AppDimensions.cornerPill),
        color = containerColor
    ) {
        AppTypography.BadgeText(
            text = text,
            color = contentColor,
            modifier = Modifier.padding(
                horizontal = AppDimensions.paddingSmall,
                vertical = AppDimensions.paddingMicro
            )
        )
    }
}

