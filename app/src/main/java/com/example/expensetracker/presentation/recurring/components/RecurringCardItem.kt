package com.example.expensetracker.presentation.recurring.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.expensetracker.core.common.formatCurrency
import com.example.expensetracker.core.common.formatDate
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppCard
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppIconButton
import com.example.expensetracker.core.designsystem.components.AppSwitch
import com.example.expensetracker.core.designsystem.theme.semanticColors
import com.example.expensetracker.domain.model.RecurringTransaction
import com.example.expensetracker.domain.model.TransactionType
import com.example.expensetracker.presentation.components.CategoryIcon

@Composable
fun RecurringCardItem(
    item: RecurringTransaction,
    currencyCode: String,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = semanticColors
    val amountColor = if (item.type == TransactionType.INCOME) colors.income else MaterialTheme.colorScheme.onSurface

    AppCard(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppDimensions.paddingMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CategoryIcon(iconName = item.categoryIcon, colorHex = item.categoryColor)
            Spacer(modifier = Modifier.width(AppDimensions.paddingMedium))
            Column(modifier = Modifier.weight(1f)) {
                AppTypography.TitleSmall(
                    text = item.title,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )
                AppTypography.Caption(
                    text = "${item.frequency.displayName} • Next: ${item.nextDueDate.formatDate()}"
                )
            }
            AppTypography.TitleSmall(
                text = item.amount.formatCurrency(currencyCode),
                fontWeight = FontWeight.Bold,
                color = amountColor
            )
            Spacer(modifier = Modifier.width(AppDimensions.paddingSmall))
            AppSwitch(
                checked = item.isEnabled,
                onCheckedChange = { onToggle() }
            )
            AppIconButton(onClick = onDelete) {
                AppIcon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

