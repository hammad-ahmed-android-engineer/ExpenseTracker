package com.example.expensetracker.presentation.transactions.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.expensetracker.R
import com.example.expensetracker.core.common.formatCurrency
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppDropdownMenu
import com.example.expensetracker.core.designsystem.components.AppDropdownMenuItem
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppIconButton
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
                    text = "${tx.categoryName.ifEmpty { tx.type.displayName }} • ${tx.accountName}",
                    maxLines = 1
                )
            }
            AppTypography.TitleSmall(
                text = "$prefix${tx.amount.formatCurrency(currencyCode)}",
                fontWeight = FontWeight.Bold,
                color = amountColor
            )
            Box {
                AppIconButton(onClick = { showMenu = true }) {
                    AppIcon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Options"
                    )
                }
                AppDropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    AppDropdownMenuItem(
                        text = { AppTypography.LabelMedium(text = stringResource(R.string.action_edit)) },
                        onClick = {
                            showMenu = false
                            onEdit()
                        },
                        leadingIcon = { AppIcon(imageVector = Icons.Default.Edit, contentDescription = null) }
                    )
                    AppDropdownMenuItem(
                        text = { AppTypography.LabelMedium(text = stringResource(R.string.action_duplicate)) },
                        onClick = {
                            showMenu = false
                            onDuplicate()
                        },
                        leadingIcon = { AppIcon(imageVector = Icons.Default.ContentCopy, contentDescription = null) }
                    )
                    AppDropdownMenuItem(
                        text = {
                            AppTypography.LabelMedium(
                                text = stringResource(R.string.action_delete),
                                color = MaterialTheme.colorScheme.error
                            )
                        },
                        onClick = {
                            showMenu = false
                            onDelete()
                        },
                        leadingIcon = {
                            AppIcon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    )
                }
            }
        }
    }
}

