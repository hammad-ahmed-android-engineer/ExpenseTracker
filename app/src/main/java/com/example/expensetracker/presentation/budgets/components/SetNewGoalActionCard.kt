package com.example.expensetracker.presentation.budgets.components

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.expensetracker.R
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppCard
import com.example.expensetracker.core.designsystem.components.AppIcon

@Composable
fun SetNewGoalActionCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppCard(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppDimensions.paddingDefault),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
            ) {
                Box(
                    modifier = Modifier
                        .size(AppDimensions.components.avatar)
                        .clip(CircleShape)
                        .background(Color(0xFFA5B4FC).copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    AppIcon(
                        imageVector = Icons.Default.Savings,
                        contentDescription = null,
                        tint = Color(0xFFA5B4FC)
                    )
                }
                Column {
                    AppTypography.TitleSmall(
                        text = stringResource(R.string.set_new_savings_goal),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(AppDimensions.paddingMicro))
                    AppTypography.BodySmall(
                        text = stringResource(R.string.set_new_savings_goal_desc),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            AppIcon(
                imageVector = Icons.Default.Add,
                contentDescription = "Set Goal",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

