package com.example.expensetracker.presentation.transactions.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.expensetracker.R
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppBottomSheet
import com.example.expensetracker.core.designsystem.components.AppButton
import com.example.expensetracker.core.designsystem.components.AppFilterChip
import com.example.expensetracker.core.designsystem.components.AppOutlinedButton
import com.example.expensetracker.core.designsystem.components.AppRadioButton
import com.example.expensetracker.core.designsystem.components.AppSurface
import com.example.expensetracker.domain.model.Account
import com.example.expensetracker.domain.model.Category
import com.example.expensetracker.domain.repository.TransactionFilter
import com.example.expensetracker.domain.repository.TransactionSortOrder

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TransactionFilterBottomSheet(
    filter: TransactionFilter,
    accounts: List<Account>,
    categories: List<Category>,
    onAccountSelect: (Long?) -> Unit,
    onCategorySelect: (Long?) -> Unit,
    onPaymentMethodSelect: (String?) -> Unit,
    onSortOrderSelect: (TransactionSortOrder) -> Unit,
    onClearFilters: () -> Unit,
    onDismiss: () -> Unit
) {
    val paymentMethods = listOf("CASH", "CARD", "BANK_TRANSFER", "DIGITAL_WALLET")

    AppBottomSheet(
        onDismissRequest = onDismiss,
        title = "Filter & Sort"
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)) {
            AppTypography.TitleSmall(text = "Sort By", color = MaterialTheme.colorScheme.primary)
            Column(verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMicro)) {
                TransactionSortOrder.entries.forEach { order ->
                    AppSurface(
                        onClick = { onSortOrderSelect(order) },
                        shape = RoundedCornerShape(AppDimensions.cornerMedium),
                        color = MaterialTheme.colorScheme.surfaceContainer,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = AppDimensions.paddingSmall),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AppRadioButton(
                                selected = filter.sortOrder == order,
                                onClick = { onSortOrderSelect(order) }
                            )
                            AppTypography.BodyMedium(text = order.displayName)
                        }
                    }
                }
            }

            AppTypography.TitleSmall(text = "Account", color = MaterialTheme.colorScheme.primary)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall),
                verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMicro)
            ) {
                AppFilterChip(
                    selected = filter.accountId == null,
                    onClick = { onAccountSelect(null) },
                    label = { AppTypography.LabelSmall(text = "All Accounts") }
                )
                accounts.forEach { acc ->
                    AppFilterChip(
                        selected = filter.accountId == acc.id,
                        onClick = { onAccountSelect(acc.id) },
                        label = { AppTypography.LabelSmall(text = acc.name) }
                    )
                }
            }

            AppTypography.TitleSmall(text = "Payment Method", color = MaterialTheme.colorScheme.primary)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall),
                verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMicro)
            ) {
                AppFilterChip(
                    selected = filter.paymentMethod == null,
                    onClick = { onPaymentMethodSelect(null) },
                    label = { AppTypography.LabelSmall(text = "Any") }
                )
                paymentMethods.forEach { method ->
                    AppFilterChip(
                        selected = filter.paymentMethod == method,
                        onClick = { onPaymentMethodSelect(method) },
                        label = { AppTypography.LabelSmall(text = method.replace("_", " ")) }
                    )
                }
            }

            AppTypography.TitleSmall(text = "Category", color = MaterialTheme.colorScheme.primary)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall),
                verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMicro)
            ) {
                AppFilterChip(
                    selected = filter.categoryId == null,
                    onClick = { onCategorySelect(null) },
                    label = { AppTypography.LabelSmall(text = "All Categories") }
                )
                categories.forEach { cat ->
                    AppFilterChip(
                        selected = filter.categoryId == cat.id,
                        onClick = { onCategorySelect(cat.id) },
                        label = { AppTypography.LabelSmall(text = cat.name) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
            ) {
                AppOutlinedButton(
                    onClick = onClearFilters,
                    modifier = Modifier.weight(1f)
                ) {
                    AppTypography.ButtonText(text = stringResource(R.string.action_clear))
                }
                AppButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f)
                ) {
                    AppTypography.ButtonText(text = "Apply")
                }
            }
        }
    }
}

