package com.example.expensetracker.presentation.budgets.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppButton
import com.example.expensetracker.core.designsystem.components.AppIcon

@Composable
fun BudgetSectionHeader(
    title: String,
    actionLabel: String,
    onActionClick: () -> Unit,
    isLargeTitle: Boolean = false,
    buttonContainerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    buttonContentColor: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isLargeTitle) {
            AppTypography.TitleLarge(
                text = title,
                fontWeight = FontWeight.Bold
            )
        } else {
            AppTypography.TitleMedium(
                text = title,
                fontWeight = FontWeight.Bold
            )
        }
        AppButton(
            onClick = onActionClick,
            shape = RoundedCornerShape(AppDimensions.cornerLarge),
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonContainerColor,
                contentColor = buttonContentColor
            )
        ) {
            AppIcon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(AppDimensions.iconSmall),
                tint = buttonContentColor
            )
            Spacer(modifier = Modifier.width(AppDimensions.paddingMicro))
            AppTypography.LabelMedium(
                text = actionLabel,
                fontWeight = FontWeight.Bold,
                color = buttonContentColor
            )
        }
    }
}

