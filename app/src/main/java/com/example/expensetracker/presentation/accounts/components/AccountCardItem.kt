package com.example.expensetracker.presentation.accounts.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.expensetracker.core.common.formatCurrency
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppCard
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.utils.toComposeColor
import com.example.expensetracker.domain.model.Account
import com.example.expensetracker.presentation.components.CategoryIcon

@Composable
fun AccountCardItem(
    acc: Account,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val fallbackBg = when {
        acc.name.contains("Checking", ignoreCase = true) -> Color(0xFF3B4371)
        acc.name.contains("Cash", ignoreCase = true) -> Color(0xFF1E5E4E)
        acc.name.contains("Savings", ignoreCase = true) -> Color(0xFF1D6A5D)
        acc.name.contains("Credit", ignoreCase = true) -> Color(0xFF9E432A)
        else -> acc.colorHex.toComposeColor()
    }

    AppCard(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppDimensions.cornerLarge),
        colors = CardDefaults.cardColors(containerColor = fallbackBg),
        border = null
    ) {
        Column(
            modifier = Modifier.padding(AppDimensions.paddingDefault),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CategoryIcon(
                    iconName = acc.iconName,
                    colorHex = "#FFFFFF",
                    modifier = Modifier.size(AppDimensions.icons.large)
                )
                AppIcon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.75f),
                    modifier = Modifier.size(AppDimensions.icons.medium)
                )
            }

            Spacer(modifier = Modifier.height(AppDimensions.paddingMedium))

            AppTypography.LabelSmall(
                text = "${acc.name}:",
                color = Color.White.copy(alpha = 0.85f),
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(AppDimensions.paddingMicro))

            AppTypography.TitleLarge(
                text = acc.currentBalance.formatCurrency(acc.currency),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    }
}
