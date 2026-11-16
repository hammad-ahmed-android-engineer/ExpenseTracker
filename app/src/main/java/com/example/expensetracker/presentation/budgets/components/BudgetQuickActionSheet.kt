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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalanceWallet
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
import com.example.expensetracker.core.designsystem.components.AppBottomSheet
import com.example.expensetracker.core.designsystem.components.AppCard
import com.example.expensetracker.core.designsystem.components.AppIcon

@Composable
fun BudgetQuickActionSheet(
    onSelectBudget: () -> Unit,
    onSelectSavingsGoal: () -> Unit,
    onDismiss: () -> Unit
) {
    AppBottomSheet(
        onDismissRequest = onDismiss,
        title = stringResource(R.string.budgets_savings_title)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = AppDimensions.paddingLarge),
            verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingDefault)
        ) {
            AppCard(
                onClick = onSelectBudget,
                modifier = Modifier.fillMaxWidth()
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
                                .size(AppDimensions.components.buttonHeight)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            AppIcon(
                                imageVector = Icons.Default.AccountBalanceWallet,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        Column {
                            AppTypography.TitleMedium(
                                text = stringResource(R.string.set_budget),
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(AppDimensions.paddingMicro))
                            AppTypography.BodySmall(
                                text = stringResource(R.string.set_budget_desc),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    AppIcon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            AppCard(
                onClick = onSelectSavingsGoal,
                modifier = Modifier.fillMaxWidth()
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
                                .size(AppDimensions.components.buttonHeight)
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
                            AppTypography.TitleMedium(
                                text = stringResource(R.string.set_savings_goal),
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(AppDimensions.paddingMicro))
                            AppTypography.BodySmall(
                                text = stringResource(R.string.set_goal_desc),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    AppIcon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

