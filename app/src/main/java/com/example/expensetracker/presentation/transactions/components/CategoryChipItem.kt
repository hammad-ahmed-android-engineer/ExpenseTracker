package com.example.expensetracker.presentation.transactions.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppSurface
import com.example.expensetracker.domain.model.Category
import com.example.expensetracker.presentation.components.CategoryIcon

@Composable
fun CategoryChipItem(
    category: Category,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppSurface(
        onClick = onClick,
        shape = RoundedCornerShape(AppDimensions.cornerPill),
        color = if (isSelected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceContainerLow
        },
        border = BorderStroke(
            width = AppDimensions.borders.thin,
            color = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outlineVariant
            }
        ),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = AppDimensions.paddingMedium,
                vertical = AppDimensions.paddingSmall
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CategoryIcon(
                iconName = category.iconName,
                colorHex = category.colorHex,
                size = AppDimensions.icons.large,
                iconSize = AppDimensions.icons.small
            )
            Spacer(modifier = Modifier.width(AppDimensions.paddingSmall))
            AppTypography.LabelMedium(
                text = category.name,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
    }
}

