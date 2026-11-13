package com.example.expensetracker.core.designsystem.components

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.expensetracker.core.designsystem.AppDimensions

@Composable
fun AppDivider(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
) {
    HorizontalDivider(
        modifier = modifier,
        thickness = AppDimensions.borders.thin,
        color = color
    )
}

