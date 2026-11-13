package com.example.expensetracker.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppButton
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppSurface

@Composable
fun EmptyStateView(
    title: String,
    description: String,
    icon: ImageVector = Icons.Default.Inbox,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(AppDimensions.paddingExtraLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AppSurface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f),
            border = null,
            modifier = Modifier.size(AppDimensions.components.emptyStateIconContainer)
        ) {
            Box(contentAlignment = Alignment.Center) {
                AppIcon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(AppDimensions.icons.extraLarge)
                )
            }
        }
        Spacer(modifier = Modifier.height(AppDimensions.paddingDefault))
        AppTypography.TitleMedium(
            text = title,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))
        AppTypography.BodyMedium(
            text = description,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        if (actionText != null && onActionClick != null) {
            Spacer(modifier = Modifier.height(AppDimensions.paddingLarge))
            AppButton(onClick = onActionClick) {
                AppTypography.ButtonText(text = actionText)
            }
        }
    }
}
