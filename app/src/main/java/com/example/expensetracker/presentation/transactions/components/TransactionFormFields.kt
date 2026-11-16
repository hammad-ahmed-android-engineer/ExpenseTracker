package com.example.expensetracker.presentation.transactions.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.expensetracker.R
import com.example.expensetracker.core.common.formatDate
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppButton
import com.example.expensetracker.core.designsystem.components.AppFilterChip
import com.example.expensetracker.core.designsystem.components.AppOutlinedButton
import com.example.expensetracker.core.designsystem.components.AppOutlinedTextField
import com.example.expensetracker.domain.model.Account
import com.example.expensetracker.domain.model.Category
import com.example.expensetracker.domain.model.Subcategory
import com.example.expensetracker.domain.model.TransactionType
import com.example.expensetracker.presentation.transactions.AddEditTransactionUiState

@Composable
fun TransactionFormFields(
    state: AddEditTransactionUiState,
    paymentMethods: List<String>,
    dateLabel: String,
    showPaymentMethodHeader: Boolean,
    onTitleChange: (String) -> Unit,
    onAccountSelect: (Account) -> Unit,
    onDestAccountSelect: (Account) -> Unit,
    onCategorySelect: (Category) -> Unit,
    onSubcategorySelect: (Subcategory?) -> Unit,
    onOpenDatePicker: () -> Unit,
    onPaymentMethodSelect: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    onTagsChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
    ) {
        AppOutlinedTextField(
            value = state.title,
            onValueChange = onTitleChange,
            label = { AppTypography.LabelMedium(text = stringResource(R.string.title_optional)) },
            modifier = Modifier.fillMaxWidth()
        )

        AppTypography.TitleSmall(text = stringResource(R.string.select_account))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)) {
            items(state.accounts, key = { it.id }) { acc ->
                AppFilterChip(
                    selected = state.selectedAccount?.id == acc.id,
                    onClick = { onAccountSelect(acc) },
                    label = { AppTypography.LabelMedium(text = acc.name) }
                )
            }
        }

        if (state.type == TransactionType.TRANSFER) {
            AppTypography.TitleSmall(text = stringResource(R.string.select_destination_account))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)) {
                items(state.accounts.filter { it.id != state.selectedAccount?.id }, key = { it.id }) { acc ->
                    AppFilterChip(
                        selected = state.selectedDestAccount?.id == acc.id,
                        onClick = { onDestAccountSelect(acc) },
                        label = { AppTypography.LabelMedium(text = acc.name) }
                    )
                }
            }
        } else {
            AppTypography.TitleSmall(text = stringResource(R.string.select_category))
            val catType = if (state.type == TransactionType.INCOME || state.type == TransactionType.REFUND) {
                TransactionType.INCOME
            } else TransactionType.EXPENSE
            val filteredCats = state.categories.filter { it.type == catType }
            LazyRow(horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)) {
                items(filteredCats, key = { it.id }) { cat ->
                    CategoryChipItem(
                        category = cat,
                        isSelected = state.selectedCategory?.id == cat.id,
                        onClick = { onCategorySelect(cat) }
                    )
                }
            }
            val subs = state.selectedCategory?.subcategories.orEmpty()
            if (subs.isNotEmpty()) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)) {
                    items(subs, key = { it.id }) { sub ->
                        AppFilterChip(
                            selected = state.selectedSubcategory?.id == sub.id,
                            onClick = {
                                onSubcategorySelect(
                                    if (state.selectedSubcategory?.id == sub.id) null else sub
                                )
                            },
                            label = { AppTypography.LabelSmall(text = sub.name) }
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppTypography.TitleSmall(text = dateLabel)
            AppOutlinedButton(onClick = onOpenDatePicker) {
                AppTypography.ButtonText(text = state.date.formatDate("MMM dd, yyyy"))
            }
        }

        if (showPaymentMethodHeader) {
            AppTypography.TitleSmall(text = stringResource(R.string.payment_method))
        }
        LazyRow(horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)) {
            items(paymentMethods) { method ->
                AppFilterChip(
                    selected = state.paymentMethod == method,
                    onClick = { onPaymentMethodSelect(method) },
                    label = { AppTypography.LabelSmall(text = method.replace("_", " ")) }
                )
            }
        }

        AppOutlinedTextField(
            value = state.note,
            onValueChange = onNoteChange,
            label = { AppTypography.LabelMedium(text = stringResource(R.string.notes_description)) },
            modifier = Modifier.fillMaxWidth()
        )

        AppOutlinedTextField(
            value = state.tagsText,
            onValueChange = onTagsChange,
            label = { AppTypography.LabelMedium(text = stringResource(R.string.tags_comma_separated)) },
            modifier = Modifier.fillMaxWidth()
        )

        AppButton(
            onClick = onSaveClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            AppTypography.ButtonText(
                text = stringResource(
                    if (state.id == 0L) R.string.save_transaction
                    else R.string.update_transaction
                )
            )
        }
    }
}

