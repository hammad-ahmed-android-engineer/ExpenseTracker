package com.example.expensetracker.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.utils.toComposeColor

@Composable
fun CategoryIcon(
    iconName: String,
    colorHex: String,
    modifier: Modifier = Modifier,
    size: Dp = AppDimensions.components.avatar,
    iconSize: Dp = AppDimensions.icons.medium
) {
    val accentColor = colorHex.toComposeColor()

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(AppDimensions.cornerMedium))
            .background(accentColor.copy(alpha = 0.14f))
    ) {
        AppIcon(
            imageVector = iconName.toCategoryIconVector(),
            contentDescription = iconName,
            tint = accentColor,
            modifier = Modifier.size(iconSize)
        )
    }
}
