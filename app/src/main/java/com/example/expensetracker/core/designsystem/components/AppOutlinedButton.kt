package com.example.expensetracker.core.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.utils.rememberDebouncedClick

@Composable
fun AppOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(AppDimensions.cornerLarge),
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(
        contentColor = MaterialTheme.colorScheme.primary
    ),
    border: BorderStroke = BorderStroke(
        width = AppDimensions.borders.thin,
        color = MaterialTheme.colorScheme.outlineVariant
    ),
    contentPadding: PaddingValues = PaddingValues(
        horizontal = AppDimensions.paddingDefault,
        vertical = AppDimensions.paddingSmall
    ),
    content: @Composable RowScope.() -> Unit
) {
    val debouncedClick = rememberDebouncedClick(onClick = onClick)
    OutlinedButton(
        onClick = debouncedClick,
        modifier = modifier.heightIn(min = AppDimensions.buttonHeight),
        enabled = enabled,
        shape = shape,
        colors = colors,
        border = border,
        contentPadding = contentPadding,
        content = content
    )
}
