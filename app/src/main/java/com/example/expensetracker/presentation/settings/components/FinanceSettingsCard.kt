package com.example.expensetracker.presentation.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.expensetracker.R
import com.example.expensetracker.core.common.SupportedCurrency
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppCard
import com.example.expensetracker.core.designsystem.components.AppFilterChip

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FinanceSettingsCard(
    defaultCurrency: String,
    onCurrencyChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    AppCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(AppDimensions.paddingDefault),
            verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
        ) {
            AppTypography.TitleSmall(text = stringResource(R.string.default_currency))
            AppTypography.Caption(text = stringResource(R.string.default_currency_desc))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall),
                verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMicro)
            ) {
                SupportedCurrency.entries.forEach { currency ->
                    AppFilterChip(
                        selected = defaultCurrency == currency.code,
                        onClick = { onCurrencyChange(currency.code) },
                        label = { AppTypography.LabelSmall(text = "${currency.code} ${currency.symbol}") }
                    )
                }
            }
        }
    }
}

