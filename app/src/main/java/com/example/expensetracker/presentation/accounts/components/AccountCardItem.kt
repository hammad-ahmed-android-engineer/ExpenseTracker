package com.example.expensetracker.presentation.accounts.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Unarchive
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.expensetracker.core.common.formatCurrency
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppBadge
import com.example.expensetracker.core.designsystem.components.AppCard
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppIconButton
import com.example.expensetracker.domain.model.Account
import com.example.expensetracker.presentation.components.CategoryIcon

@Composable
fun AccountCardItem(
    acc: Account,
    onToggleArchive: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppCard(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppDimensions.paddingDefault),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CategoryIcon(iconName = acc.iconName, colorHex = acc.colorHex)
            Spacer(modifier = Modifier.width(AppDimensions.paddingMedium))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AppTypography.TitleMedium(
                        text = acc.name,
                        maxLines = 1,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    if (acc.isArchived) {
                        Spacer(modifier = Modifier.width(AppDimensions.paddingSmall))
                        AppBadge(text = "Archived")
                    }
                }
                AppTypography.Caption(text = "${acc.type.displayName} • ${acc.currency}")
            }
            AppTypography.TitleMedium(
                text = acc.currentBalance.formatCurrency(acc.currency),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            AppIconButton(onClick = onToggleArchive) {
                AppIcon(
                    imageVector = if (acc.isArchived) Icons.Default.Unarchive else Icons.Default.Archive,
                    contentDescription = if (acc.isArchived) "Unarchive" else "Archive",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

