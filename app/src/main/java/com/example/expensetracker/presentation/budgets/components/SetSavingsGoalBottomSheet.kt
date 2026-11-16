package com.example.expensetracker.presentation.budgets.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import com.example.expensetracker.R
import com.example.expensetracker.core.common.toIsoDate
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.utils.clickableDebounced
import com.example.expensetracker.core.designsystem.components.AppBottomSheet
import com.example.expensetracker.core.designsystem.components.AppButton
import com.example.expensetracker.core.designsystem.components.AppCard
import com.example.expensetracker.core.designsystem.components.AppDatePickerDialog
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppOutlinedTextField

@Composable
fun SetSavingsGoalBottomSheet(
    currencyCode: String,
    onSave: (
        title: String,
        targetAmount: String,
        targetDate: Long,
        colorHex: String,
        description: String?,
        initialDeposit: String?
    ) -> Unit,
    onDismiss: () -> Unit
) {
    var goalTitle by rememberSaveable { mutableStateOf("") }
    var goalTargetAmount by rememberSaveable { mutableStateOf("") }
    var goalTargetDate by rememberSaveable { mutableStateOf(System.currentTimeMillis() + 86400000L * 180) }
    var goalSelectedColor by rememberSaveable { mutableStateOf("#4CAF50") }
    var goalDescription by rememberSaveable { mutableStateOf("") }
    var goalInitialDeposit by rememberSaveable { mutableStateOf("") }
    var showGoalDatePicker by rememberSaveable { mutableStateOf(false) }

    val presetColors = listOf(
        Pair("#4CAF50", "Green"),
        Pair("#2196F3", "Blue"),
        Pair("#9C27B0", "Purple"),
        Pair("#FF9800", "Orange"),
        Pair("#E91E63", "Pink"),
        Pair("#00BCD4", "Cyan")
    )

    AppBottomSheet(
        onDismissRequest = onDismiss,
        title = stringResource(R.string.new_savings_goal)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = AppDimensions.paddingLarge),
            verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
        ) {
            AppOutlinedTextField(
                value = goalTitle,
                onValueChange = { goalTitle = it },
                label = { AppTypography.LabelMedium(text = stringResource(R.string.goal_title)) },
                placeholder = { AppTypography.BodyMedium(text = stringResource(R.string.savings_goal_placeholder)) },
                leadingIcon = {
                    AppIcon(
                        imageVector = Icons.Default.Savings,
                        contentDescription = null,
                        tint = Color(0xFFA5B4FC)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            AppOutlinedTextField(
                value = goalTargetAmount,
                onValueChange = { goalTargetAmount = it },
                label = { AppTypography.LabelMedium(text = "${stringResource(R.string.target_amount)} ($currencyCode)") },
                placeholder = { AppTypography.BodyMedium(text = "1000.00") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            AppCard(
                onClick = { showGoalDatePicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppDimensions.paddingDefault, vertical = AppDimensions.paddingMedium),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        AppTypography.LabelSmall(
                            text = stringResource(R.string.target_date),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        AppTypography.BodyMedium(
                            text = goalTargetDate.toIsoDate(),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    AppIcon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMicro)) {
                AppTypography.LabelSmall(
                    text = stringResource(R.string.goal_color),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    presetColors.forEach { (hex, _) ->
                        val color = Color(android.graphics.Color.parseColor(hex))
                        val isSelected = goalSelectedColor == hex
                        Box(
                            modifier = Modifier
                                .size(AppDimensions.components.colorPickerCircle)
                                .clip(CircleShape)
                                .background(color)
                                .clickableDebounced { goalSelectedColor = hex }
                                .then(
                                    if (isSelected) {
                                        Modifier.border(
                                            BorderStroke(AppDimensions.borders.thick, MaterialTheme.colorScheme.onSurface),
                                            CircleShape
                                        )
                                    } else Modifier
                                )
                        )
                    }
                }
            }

            AppOutlinedTextField(
                value = goalInitialDeposit,
                onValueChange = { goalInitialDeposit = it },
                label = { AppTypography.LabelMedium(text = stringResource(R.string.initial_deposit_optional)) },
                placeholder = { AppTypography.BodyMedium(text = "0.00") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            AppOutlinedTextField(
                value = goalDescription,
                onValueChange = { goalDescription = it },
                label = { AppTypography.LabelMedium(text = stringResource(R.string.optional_note)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))

            AppButton(
                onClick = {
                    onSave(
                        goalTitle.ifEmpty { "New Goal" },
                        goalTargetAmount,
                        goalTargetDate,
                        goalSelectedColor,
                        goalDescription.ifBlank { null },
                        goalInitialDeposit.ifBlank { null }
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = goalTargetAmount.isNotBlank()
            ) {
                AppTypography.ButtonText(text = stringResource(R.string.save_goal))
            }
        }
    }

    if (showGoalDatePicker) {
        AppDatePickerDialog(
            initialSelectedDateMillis = goalTargetDate,
            onDateSelected = { pickedDate ->
                pickedDate?.let { goalTargetDate = it }
                showGoalDatePicker = false
            },
            onDismiss = { showGoalDatePicker = false }
        )
    }
}

