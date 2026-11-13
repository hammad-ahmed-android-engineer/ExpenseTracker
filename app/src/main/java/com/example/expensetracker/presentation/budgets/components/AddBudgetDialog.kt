package com.example.expensetracker.presentation.budgets.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.expensetracker.R
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppAlertDialog
import com.example.expensetracker.core.designsystem.components.AppFilterChip
import com.example.expensetracker.core.designsystem.components.AppOutlinedTextField
import com.example.expensetracker.core.designsystem.components.AppSegmentedSelector
import com.example.expensetracker.core.designsystem.components.AppSlider
import com.example.expensetracker.core.designsystem.components.AppSwitch
import com.example.expensetracker.domain.model.BudgetPeriodType
import com.example.expensetracker.domain.model.Category

@Composable
fun AddBudgetDialog(
    categories: List<Category>,
    onDismiss: () -> Unit,
    onConfirm: (
        name: String,
        limit: String,
        periodType: BudgetPeriodType,
        threshold: Int,
        rollover: Boolean,
        category: Category?
    ) -> Unit
) {
    var name by remember { mutableStateOf("Monthly Budget") }
    var limit by remember { mutableStateOf("") }
    var periodType by remember { mutableStateOf(BudgetPeriodType.MONTHLY) }
    var threshold by remember { mutableFloatStateOf(80f) }
    var rollover by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }

    val periods = listOf(BudgetPeriodType.WEEKLY, BudgetPeriodType.MONTHLY, BudgetPeriodType.YEARLY)

    AppAlertDialog(
        onDismissRequest = onDismiss,
        title = stringResource(R.string.create_budget),
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)) {
                AppOutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { AppTypography.LabelMedium(text = "Budget Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                AppOutlinedTextField(
                    value = limit,
                    onValueChange = { limit = it },
                    label = { AppTypography.LabelMedium(text = "Spending Limit") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                AppTypography.LabelLarge(text = "Period")
                AppSegmentedSelector(
                    options = periods,
                    selectedOption = periodType,
                    onOptionSelected = { periodType = it },
                    labelProvider = { it.displayName }
                )

                if (categories.isNotEmpty()) {
                    AppTypography.LabelLarge(text = "Scope (Optional Category)")
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)) {
                        item {
                            AppFilterChip(
                                selected = selectedCategory == null,
                                onClick = { selectedCategory = null },
                                label = { AppTypography.LabelSmall(text = "All Spending") }
                            )
                        }
                        items(categories, key = { it.id }) { cat ->
                            AppFilterChip(
                                selected = selectedCategory?.id == cat.id,
                                onClick = { selectedCategory = cat },
                                label = { AppTypography.LabelSmall(text = cat.name) }
                            )
                        }
                    }
                }

                Column {
                    AppTypography.LabelLarge(
                        text = "Alert Threshold: ${threshold.toInt()}%",
                        color = MaterialTheme.colorScheme.primary
                    )
                    AppSlider(
                        value = threshold,
                        onValueChange = { threshold = it },
                        valueRange = 50f..100f,
                        steps = 4
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppTypography.BodyMedium(text = "Enable Budget Rollover")
                    AppSwitch(checked = rollover, onCheckedChange = { rollover = it })
                }
            }
        },
        confirmButtonText = stringResource(R.string.action_create),
        confirmEnabled = limit.toDoubleOrNull()?.let { it > 0 } == true,
        onConfirm = {
            onConfirm(name, limit, periodType, threshold.toInt(), rollover, selectedCategory)
        },
        dismissButtonText = stringResource(R.string.action_cancel),
        onDismiss = onDismiss
    )
}

