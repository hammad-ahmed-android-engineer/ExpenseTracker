package com.example.expensetracker.presentation.transactions.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.expensetracker.R
import com.example.expensetracker.core.common.formatCurrency
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppSurface
import com.example.expensetracker.core.designsystem.theme.semanticColors
import com.example.expensetracker.domain.model.Transaction
import com.example.expensetracker.domain.model.TransactionType
import com.example.expensetracker.presentation.components.CategoryIcon

@Composable
fun TransactionListItem(
    tx: Transaction,
    currencyCode: String,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onDuplicate: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }
    val colors = semanticColors

    val formattedAmount = kotlin.math.abs(tx.amount).formatCurrency(currencyCode)
    val displayAmount = when (tx.type) {
        TransactionType.INCOME, TransactionType.REFUND -> "+$formattedAmount"
        TransactionType.EXPENSE -> formattedAmount
        TransactionType.TRANSFER -> "-$formattedAmount"
        TransactionType.ADJUSTMENT -> if (tx.amount >= 0) "+$formattedAmount" else "-$formattedAmount"
    }
    val amountColor = when (tx.type) {
        TransactionType.INCOME, TransactionType.REFUND -> colors.income
        TransactionType.EXPENSE -> MaterialTheme.colorScheme.onSurface
        TransactionType.TRANSFER -> colors.expense
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
                .padding(horizontal = AppDimensions.paddingMedium, vertical = AppDimensions.paddingSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CategoryIcon(iconName = tx.categoryIcon, colorHex = tx.categoryColor)
            Spacer(modifier = Modifier.width(AppDimensions.paddingSmall))

            if (tx.type == TransactionType.TRANSFER) {
                Column(modifier = Modifier.weight(1f)) {
                    AppTypography.TitleSmall(
                        text = formattedAmount,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(AppDimensions.paddingMicro))
                    AppTypography.Caption(
                        text = stringResource(
                            R.string.transfer_format,
                            tx.accountName.ifEmpty { "Savings" },
                            (tx.note ?: "").ifEmpty { "checking" }
                        ),
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.width(AppDimensions.paddingSmall))
                AppTypography.TitleSmall(
                    text = "-$formattedAmount",
                    fontWeight = FontWeight.Bold,
                    color = colors.expense,
                    maxLines = 1
                )
            } else {
                Column(modifier = Modifier.weight(1f)) {
                    AppTypography.TitleSmall(
                        text = tx.title.ifEmpty { tx.categoryName.ifEmpty { stringResource(R.string.default_transaction_title) } },
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                    if (!tx.note.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(AppDimensions.paddingMicro))
                        AppTypography.Caption(
                            text = tx.note,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                    }
                    if (tx.accountName.isNotBlank()) {
                        Spacer(modifier = Modifier.height(AppDimensions.paddingMicro))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(AppDimensions.cornerSmall))
                                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                                .padding(horizontal = AppDimensions.paddingSmall, vertical = AppDimensions.paddingMicro / 2)
                        ) {
                            AppTypography.Caption(
                                text = tx.accountName,
                                maxLines = 1,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.width(AppDimensions.paddingSmall))
                AppTypography.TitleSmall(
                    text = displayAmount,
                    fontWeight = FontWeight.Bold,
                    color = amountColor,
                    maxLines = 1
                )
            }
        }
    }
}

