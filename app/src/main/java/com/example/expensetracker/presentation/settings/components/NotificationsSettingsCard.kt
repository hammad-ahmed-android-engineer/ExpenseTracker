package com.example.expensetracker.presentation.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppCard
import com.example.expensetracker.core.designsystem.components.AppDivider
import com.example.expensetracker.core.designsystem.components.AppSlider
import com.example.expensetracker.core.designsystem.components.AppSwitch
import com.example.expensetracker.domain.model.UserPreferences

@Composable
fun NotificationsSettingsCard(
    preferences: UserPreferences,
    onBudgetAlertChange: (Boolean) -> Unit,
    onRecurringReminderChange: (Boolean) -> Unit,
    onBudgetThresholdChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    AppCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(AppDimensions.paddingDefault),
            verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    AppTypography.TitleSmall(text = "Budget Limit Alerts")
                    AppTypography.Caption(text = "Notify when spending hits warning threshold")
                }
                AppSwitch(
                    checked = preferences.isBudgetAlertEnabled,
                    onCheckedChange = onBudgetAlertChange
                )
            }

            if (preferences.isBudgetAlertEnabled) {
                Column {
                    AppTypography.LabelMedium(
                        text = "Warning Threshold: ${preferences.budgetWarningThreshold}%",
                        color = MaterialTheme.colorScheme.primary
                    )
                    AppSlider(
                        value = preferences.budgetWarningThreshold.toFloat(),
                        onValueChange = { onBudgetThresholdChange(it.toInt()) },
                        valueRange = 60f..100f,
                        steps = 3
                    )
                }
            }

            AppDivider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    AppTypography.TitleSmall(text = "Recurring Bill Reminders")
                    AppTypography.Caption(text = "Notify when scheduled rules execute")
                }
                AppSwitch(
                    checked = preferences.isRecurringReminderEnabled,
                    onCheckedChange = onRecurringReminderChange
                )
            }
        }
    }
}

