package com.example.expensetracker.presentation.categories.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppCard
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppIconButton
import com.example.expensetracker.domain.model.Category
import com.example.expensetracker.presentation.components.CategoryIcon

@Composable
fun CategoryGridCard(
    category: Category,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppCard(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppDimensions.paddingMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CategoryIcon(iconName = category.iconName, colorHex = category.colorHex)
            Spacer(modifier = Modifier.width(AppDimensions.paddingSmall))
            Column(modifier = Modifier.weight(1f)) {
                AppTypography.TitleSmall(text = category.name, maxLines = 1)
                val subText = when {
                    category.subcategories.isNotEmpty() -> "${category.subcategories.size} sub"
                    category.isDefault -> "Default"
                    else -> "Custom"
                }
                AppTypography.Caption(
                    text = subText,
                    color = if (category.isDefault) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (!category.isDefault) {
                AppIconButton(onClick = onDelete) {
                    AppIcon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(AppDimensions.icons.medium)
                    )
                }
            }
        }
    }
}

