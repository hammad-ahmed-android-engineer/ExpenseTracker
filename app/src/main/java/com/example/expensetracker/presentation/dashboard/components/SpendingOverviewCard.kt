package com.example.expensetracker.presentation.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.expensetracker.R
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
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
    val displaySlices = if (categories.isEmpty()) {
        listOf(
            ChartSlice(label = "Food", value = 4550L, color = Color(0xFF6366F1), percentage = 35f),
            ChartSlice(label = "Rent", value = 120000L, color = Color(0xFF10B981), percentage = 45f),
            ChartSlice(label = "Bills", value = 4250L, color = Color(0xFFF97316), percentage = 20f)
        )
    } else {
        categories.map { cs ->
            ChartSlice(
                label = cs.categoryName,
                value = cs.totalAmount,
                color = cs.categoryColor.toComposeColor(),
                percentage = cs.percentage
            )
        }
    }

    AppCard(
        onClick = onViewReports,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(AppDimensions.paddingDefault)
        ) {
            AppTypography.TitleMedium(
                text = stringResource(R.string.donut_pie_chart),
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(AppDimensions.paddingDefault))

            DonutChart(
                slices = displaySlices,
                centerText = stringResource(R.string.oct_spending),
                centerValue = 0L,
                currencyCode = currencyCode,
                legendOnRight = true
            )
        }
    }
}

