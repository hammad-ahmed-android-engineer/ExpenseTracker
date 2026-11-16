package com.example.expensetracker.presentation.transactions.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.expensetracker.R
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppFilterChip
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppIconButton
import com.example.expensetracker.core.designsystem.components.AppOutlinedTextField
import com.example.expensetracker.domain.model.TransactionType

@Composable
fun TransactionSearchAndFiltersBar(
    searchQuery: String,
    selectedType: TransactionType?,
    onSearchQueryChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    onTypeSelect: (TransactionType?) -> Unit,
    searchModifier: Modifier = Modifier,
    chipsContentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {
    val mainTypes = listOf(
        TransactionType.INCOME,
        TransactionType.EXPENSE,
        TransactionType.TRANSFER
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
    ) {
        AppOutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = { AppTypography.BodyMedium(text = stringResource(R.string.search_transactions_hint)) },
            leadingIcon = {
                AppIcon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    AppIconButton(onClick = onClearSearch) {
                        AppIcon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(R.string.action_clear)
                        )
                    }
                }
            },
            modifier = searchModifier.fillMaxWidth(),
            shape = RoundedCornerShape(AppDimensions.cornerPill)
        )

        LazyRow(
            contentPadding = chipsContentPadding,
            horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
        ) {
            item {
                AppFilterChip(
                    selected = selectedType == null,
                    onClick = { onTypeSelect(null) },
                    label = { AppTypography.LabelSmall(text = stringResource(R.string.filter_all)) }
                )
            }
            items(mainTypes) { type ->
                AppFilterChip(
                    selected = selectedType == type,
                    onClick = { onTypeSelect(type) },
                    label = { AppTypography.LabelSmall(text = type.displayName) }
                )
            }
        }
    }
}

