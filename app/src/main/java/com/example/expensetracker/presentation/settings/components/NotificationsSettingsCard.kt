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
import androidx.compose.ui.res.stringResource
import com.example.expensetracker.R
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
                    AppTypography.TitleSmall(text = stringResource(R.string.budget_limit_alerts))
                    AppTypography.Caption(text = stringResource(R.string.budget_limit_alerts_desc))
                }
                AppSwitch(
                    checked = preferences.isBudgetAlertEnabled,
                    onCheckedChange = onBudgetAlertChange
                )
            }

            if (preferences.isBudgetAlertEnabled) {
                Column {
                    AppTypography.LabelMedium(
                        text = stringResource(R.string.warning_threshold_format, preferences.budgetWarningThreshold),
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
                    AppTypography.TitleSmall(text = stringResource(R.string.recurring_bill_reminders))
                    AppTypography.Caption(text = stringResource(R.string.recurring_bill_reminders_desc))
                }
                AppSwitch(
                    checked = preferences.isRecurringReminderEnabled,
                    onCheckedChange = onRecurringReminderChange
                )
            }
        }
    }
}

