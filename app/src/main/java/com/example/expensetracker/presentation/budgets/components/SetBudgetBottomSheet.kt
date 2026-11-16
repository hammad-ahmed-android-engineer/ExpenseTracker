package com.example.expensetracker.presentation.budgets.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.expensetracker.R
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppBottomSheet
import com.example.expensetracker.core.designsystem.components.AppButton
import com.example.expensetracker.core.designsystem.components.AppFilterChip
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppOutlinedTextField
import com.example.expensetracker.core.designsystem.components.AppSlider
import com.example.expensetracker.core.designsystem.components.AppSwitch
import com.example.expensetracker.domain.model.BudgetPeriodType
import com.example.expensetracker.domain.model.Category

@Composable
fun SetBudgetBottomSheet(
    currencyCode: String,
    expenseCategories: List<Category>,
    onSave: (
        name: String,
        limit: String,
        periodType: BudgetPeriodType,
        notifyThreshold: Int,
        isRolloverEnabled: Boolean,
        category: Category?
    ) -> Unit,
    onDismiss: () -> Unit
) {
    var budgetName by rememberSaveable { mutableStateOf("") }
    var budgetLimit by rememberSaveable { mutableStateOf("") }
    var budgetPeriodType by rememberSaveable { mutableStateOf(BudgetPeriodType.MONTHLY) }
    var budgetNotifyThreshold by rememberSaveable { mutableStateOf(80) }
    var budgetRolloverEnabled by rememberSaveable { mutableStateOf(false) }
    var budgetSelectedCategoryId by rememberSaveable { mutableStateOf<Long?>(null) }

    val quickPresets = listOf(
        Pair("Groceries", "350.00"),
        Pair("Dining Out", "200.00"),
        Pair("Entertainment", "100.00"),
        Pair("Shopping", "150.00"),
        Pair("Utilities", "120.00"),
        Pair("Transport", "80.00")
    )
    val periods = listOf(BudgetPeriodType.WEEKLY, BudgetPeriodType.MONTHLY, BudgetPeriodType.YEARLY)

    AppBottomSheet(
        onDismissRequest = onDismiss,
        title = stringResource(R.string.create_budget)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = AppDimensions.paddingLarge),
            verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMicro)) {
                AppTypography.LabelSmall(
                    text = stringResource(R.string.quick_presets),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)) {
                    items(quickPresets) { (presetName, presetLimit) ->
                        AppFilterChip(
                            selected = budgetName.equals(presetName, ignoreCase = true),
                            onClick = {
                                budgetName = presetName
                                if (budgetLimit.isEmpty()) budgetLimit = presetLimit
                                val cat = expenseCategories.find { it.name.contains(presetName, ignoreCase = true) }
                                budgetSelectedCategoryId = cat?.id
                            },
                            label = { AppTypography.LabelSmall(text = presetName) }
                        )
                    }
                }
            }

            AppOutlinedTextField(
                value = budgetName,
                onValueChange = { budgetName = it },
                label = { AppTypography.LabelMedium(text = stringResource(R.string.budget_name)) },
                placeholder = { AppTypography.BodyMedium(text = stringResource(R.string.budget_placeholder)) },
                leadingIcon = {
                    AppIcon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            AppOutlinedTextField(
                value = budgetLimit,
                onValueChange = { budgetLimit = it },
                label = { AppTypography.LabelMedium(text = "${stringResource(R.string.spending_limit)} ($currencyCode)") },
                placeholder = { AppTypography.BodyMedium(text = "350.00") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            Column(verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMicro)) {
                AppTypography.LabelSmall(
                    text = stringResource(R.string.budget_period),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
                ) {
                    periods.forEach { p ->
                        AppFilterChip(
                            selected = budgetPeriodType == p,
                            onClick = { budgetPeriodType = p },
                            label = { AppTypography.LabelSmall(text = p.displayName) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            if (expenseCategories.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMicro)) {
                    AppTypography.LabelSmall(
                        text = stringResource(R.string.category_scope_optional),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)) {
                        item {
                            AppFilterChip(
                                selected = budgetSelectedCategoryId == null,
                                onClick = { budgetSelectedCategoryId = null },
                                label = { AppTypography.LabelSmall(text = stringResource(R.string.all_spending)) }
                            )
                        }
                        items(expenseCategories, key = { it.id }) { cat ->
                            AppFilterChip(
                                selected = budgetSelectedCategoryId == cat.id,
                                onClick = {
                                    budgetSelectedCategoryId = cat.id
                                    if (budgetName.isEmpty()) budgetName = cat.name
                                },
                                label = { AppTypography.LabelSmall(text = cat.name) }
                            )
                        }
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMicro)) {
                AppTypography.LabelSmall(
                    text = stringResource(R.string.notify_threshold_label, budgetNotifyThreshold),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                AppSlider(
                    value = budgetNotifyThreshold.toFloat(),
                    onValueChange = { budgetNotifyThreshold = it.toInt() },
                    valueRange = 50f..100f,
                    steps = 9
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    AppTypography.TitleSmall(text = stringResource(R.string.enable_rollover))
                    AppTypography.BodySmall(
                        text = stringResource(R.string.rollover_desc),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                AppSwitch(
                    checked = budgetRolloverEnabled,
                    onCheckedChange = { budgetRolloverEnabled = it }
                )
            }

            Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))

            AppButton(
                onClick = {
                    val selectedCat = expenseCategories.find { it.id == budgetSelectedCategoryId }
                    onSave(
                        budgetName.ifEmpty { "General Budget" },
                        budgetLimit,
                        budgetPeriodType,
                        budgetNotifyThreshold,
                        budgetRolloverEnabled,
                        selectedCat
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = budgetLimit.isNotBlank()
            ) {
                AppTypography.ButtonText(text = stringResource(R.string.save_budget))
            }
        }
    }
}

