package com.example.expensetracker.presentation.dashboard.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.expensetracker.core.common.formatCurrency
import com.example.expensetracker.core.common.formatRelativeDate
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppSurface
import com.example.expensetracker.core.designsystem.theme.semanticColors
import com.example.expensetracker.domain.model.Transaction
import com.example.expensetracker.domain.model.TransactionType
import com.example.expensetracker.presentation.components.CategoryIcon

@Composable
fun RecentTransactionItem(
    tx: Transaction,
    currencyCode: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = semanticColors
    val prefix = when (tx.type) {
        TransactionType.INCOME, TransactionType.REFUND -> "+"
        TransactionType.EXPENSE -> "-"
        else -> ""
    }
    val amountColor = when (tx.type) {
        TransactionType.INCOME, TransactionType.REFUND -> colors.income
        TransactionType.EXPENSE -> MaterialTheme.colorScheme.onSurface
        TransactionType.TRANSFER -> colors.transfer
        TransactionType.ADJUSTMENT -> MaterialTheme.colorScheme.primary
    }

    AppSurface(
        onClick = onClick,
        shape = RoundedCornerShape(AppDimensions.cornerLarge),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppDimensions.paddingMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CategoryIcon(iconName = tx.categoryIcon, colorHex = tx.categoryColor)
            Spacer(modifier = Modifier.width(AppDimensions.paddingMedium))
            Column(modifier = Modifier.weight(1f)) {
                AppTypography.TitleSmall(
                    text = tx.title,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )
                AppTypography.Caption(
                    text = "${tx.categoryName.ifEmpty { tx.type.displayName }} • ${tx.date.formatRelativeDate()}",
                    maxLines = 1
                )
            }
            Spacer(modifier = Modifier.width(AppDimensions.paddingSmall))
            AppTypography.TitleSmall(
                text = "$prefix${tx.amount.formatCurrency(currencyCode)}",
                fontWeight = FontWeight.Bold,
                color = amountColor
            )
        }
    }
}

