package com.example.expensetracker.presentation.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.expensetracker.R
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppBadge
import com.example.expensetracker.core.designsystem.components.AppCard
import com.example.expensetracker.core.utils.toComposeColor
import com.example.expensetracker.domain.model.CategorySpendingSummary
import com.example.expensetracker.presentation.components.charts.ChartSlice
import com.example.expensetracker.presentation.components.charts.DonutChart

@Composable
fun SpendingOverviewCard(
    categories: List<CategorySpendingSummary>,
    totalExpense: Long,
    highestCategoryName: String?,
    currencyCode: String,
    onViewReports: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppCard(
        onClick = onViewReports,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(AppDimensions.paddingDefault),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppTypography.TitleMedium(text = stringResource(R.string.monthly_spending_category))
                if (highestCategoryName != null) {
                    AppBadge(
                        text = highestCategoryName,
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
            Spacer(modifier = Modifier.height(AppDimensions.paddingDefault))

            val slices = categories.map { cs ->
                ChartSlice(
                    label = cs.categoryName,
                    value = cs.totalAmount,
                    color = cs.categoryColor.toComposeColor(),
                    percentage = cs.percentage
                )
            }

            DonutChart(
                slices = slices,
                centerText = stringResource(R.string.total_expenses),
                centerValue = totalExpense,
                currencyCode = currencyCode
            )
        }
    }
}

