package com.example.expensetracker.presentation.calendar.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.expensetracker.core.common.formatCurrency
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppCard
import com.example.expensetracker.core.designsystem.theme.semanticColors
import com.example.expensetracker.domain.model.Transaction
import com.example.expensetracker.domain.model.TransactionType
import com.example.expensetracker.presentation.components.CategoryIcon

@Composable
fun CalendarTransactionRow(
    tx: Transaction,
    currencyCode: String,
    modifier: Modifier = Modifier
) {
    val colors = semanticColors
    val amountColor = when (tx.type) {
        TransactionType.INCOME, TransactionType.REFUND -> colors.income
        TransactionType.EXPENSE -> MaterialTheme.colorScheme.onSurface
        else -> colors.transfer
    }

    AppCard(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppDimensions.paddingMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CategoryIcon(iconName = tx.categoryIcon, colorHex = tx.categoryColor)
            Spacer(modifier = Modifier.width(AppDimensions.paddingMedium))
            Column(modifier = Modifier.weight(1f)) {
                AppTypography.TitleSmall(text = tx.title, fontWeight = FontWeight.SemiBold)
                AppTypography.Caption(text = tx.categoryName.ifEmpty { tx.type.displayName })
            }
            AppTypography.TitleSmall(
                text = tx.amount.formatCurrency(currencyCode),
                fontWeight = FontWeight.Bold,
                color = amountColor
            )
        }
    }
}

