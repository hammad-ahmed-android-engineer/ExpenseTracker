package com.example.expensetracker.presentation.reports.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.expensetracker.R
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppFilterChip
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppIconButton
import com.example.expensetracker.presentation.reports.DateFilterPeriod

@Composable
fun ReportPeriodSelector(
    selectedPeriod: DateFilterPeriod,
    dateRangeLabel: String,
    onSelectPeriod: (DateFilterPeriod) -> Unit,
    onPreviousPeriod: () -> Unit,
    onNextPeriod: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(DateFilterPeriod.entries) { period ->
                AppFilterChip(
                    selected = selectedPeriod == period,
                    onClick = { onSelectPeriod(period) },
                    label = { AppTypography.LabelSmall(text = period.displayName) }
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppIconButton(onClick = onPreviousPeriod) {
                AppIcon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = stringResource(R.string.prev_range)
                )
            }
            AppTypography.TitleSmall(
                text = dateRangeLabel,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            AppIconButton(onClick = onNextPeriod) {
                AppIcon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = stringResource(R.string.next_range)
                )
            }
        }
    }
}

