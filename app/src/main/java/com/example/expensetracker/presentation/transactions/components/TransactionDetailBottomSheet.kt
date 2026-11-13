package com.example.expensetracker.presentation.transactions.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.expensetracker.R
import com.example.expensetracker.core.common.formatCurrency
import com.example.expensetracker.core.common.formatDate
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppBadge
import com.example.expensetracker.core.designsystem.components.AppBottomSheet
import com.example.expensetracker.core.designsystem.components.AppButton
import com.example.expensetracker.core.designsystem.components.AppCard
import com.example.expensetracker.core.designsystem.components.AppDivider
import com.example.expensetracker.core.designsystem.components.AppOutlinedButton
import com.example.expensetracker.core.designsystem.components.AppTextButton
import com.example.expensetracker.core.designsystem.theme.semanticColors
import com.example.expensetracker.domain.model.Transaction
import com.example.expensetracker.domain.model.TransactionType
import com.example.expensetracker.presentation.components.CategoryIcon
import com.example.expensetracker.presentation.components.KeyValueRow

@Composable
fun TransactionDetailBottomSheet(
    transaction: Transaction,
    currencyCode: String,
    onEdit: () -> Unit,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    val colors = semanticColors
    val amountColor = when (transaction.type) {
        TransactionType.INCOME, TransactionType.REFUND -> colors.income
        TransactionType.EXPENSE -> colors.expense
        TransactionType.TRANSFER -> colors.transfer
        TransactionType.ADJUSTMENT -> MaterialTheme.colorScheme.primary
    }

    AppBottomSheet(
        onDismissRequest = onDismiss,
        title = stringResource(R.string.transaction_details)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CategoryIcon(
                    iconName = transaction.categoryIcon,
                    colorHex = transaction.categoryColor,
                    size = AppDimensions.components.fab,
                    iconSize = AppDimensions.icons.large
                )
                Spacer(modifier = Modifier.width(AppDimensions.paddingMedium))
                Column(modifier = Modifier.weight(1f)) {
                    AppTypography.TitleMedium(text = transaction.title)
                    Spacer(modifier = Modifier.height(AppDimensions.paddingMicro))
                    AppBadge(text = transaction.type.displayName)
                }
                AppTypography.HeadlineSmall(
                    text = transaction.amount.formatCurrency(currencyCode),
                    fontWeight = FontWeight.Bold,
                    color = amountColor
                )
            }

            AppCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(AppDimensions.paddingDefault),
                    verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
                ) {
                    KeyValueRow(label = stringResource(R.string.label_category), value = transaction.categoryName.ifEmpty { transaction.type.displayName })
                    AppDivider()
                    KeyValueRow(label = stringResource(R.string.label_account), value = transaction.accountName.ifEmpty { stringResource(R.string.label_primary) })
                    if (transaction.destinationAccountName != null) {
                        AppDivider()
                        KeyValueRow(label = stringResource(R.string.label_destination), value = transaction.destinationAccountName)
                    }
                    AppDivider()
                    KeyValueRow(label = stringResource(R.string.label_date), value = transaction.date.formatDate("EEE, MMM dd, yyyy • hh:mm a"))
                    AppDivider()
                    KeyValueRow(label = stringResource(R.string.label_payment), value = transaction.paymentMethod.replace("_", " "))
                    if (!transaction.note.isNullOrBlank()) {
                        AppDivider()
                        KeyValueRow(label = stringResource(R.string.label_notes), value = transaction.note)
                    }
                    if (transaction.tags.isNotEmpty()) {
                        AppDivider()
                        KeyValueRow(label = stringResource(R.string.label_tags), value = transaction.tags.joinToString(", "))
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
            ) {
                AppOutlinedButton(
                    onClick = onDuplicate,
                    modifier = Modifier.weight(1f)
                ) {
                    AppTypography.ButtonText(text = stringResource(R.string.action_duplicate))
                }
                AppButton(
                    onClick = onEdit,
                    modifier = Modifier.weight(1f)
                ) {
                    AppTypography.ButtonText(text = stringResource(R.string.action_edit))
                }
            }

            AppTextButton(
                onClick = onDelete,
                modifier = Modifier.fillMaxWidth()
            ) {
                AppTypography.ButtonText(
                    text = stringResource(R.string.action_delete),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
