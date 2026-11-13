package com.example.expensetracker.presentation.savings.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.expensetracker.R
import com.example.expensetracker.core.common.formatCurrency
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppButton
import com.example.expensetracker.core.designsystem.components.AppCard
import com.example.expensetracker.core.designsystem.components.AppDropdownMenu
import com.example.expensetracker.core.designsystem.components.AppDropdownMenuItem
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppIconButton
import com.example.expensetracker.domain.model.SavingsGoal

@Composable
fun SavingsGoalCardItem(
    goal: SavingsGoal,
    currencyCode: String,
    onDepositClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pct = if (goal.targetAmount > 0) {
        (goal.currentAmount.toFloat() / goal.targetAmount.toFloat()).coerceIn(0f, 1f)
    } else 0f

    val ringColor = if (pct >= 0.7f) Color(0xFF2DD4BF) else Color(0xFFF87171)
    var showMenu by remember { mutableStateOf(false) }

    AppCard(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(AppDimensions.paddingDefault)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    AppTypography.TitleMedium(
                        text = goal.title,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(AppDimensions.paddingMicro))
                    AppTypography.BodySmall(
                        text = "(${goal.currentAmount.formatCurrency(currencyCode)} / ${goal.targetAmount.formatCurrency(currencyCode)})",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Circular Progress Ring on right + options menu
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingMicro)
                ) {
                    Box(
                        modifier = Modifier.size(AppDimensions.icons.huge),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(AppDimensions.icons.huge)) {
                            val strokeW = 8f
                            drawCircle(
                                color = Color.White.copy(alpha = 0.12f),
                                style = Stroke(width = strokeW)
                            )
                            drawArc(
                                color = ringColor,
                                startAngle = -90f,
                                sweepAngle = 360f * pct,
                                useCenter = false,
                                style = Stroke(width = strokeW, cap = StrokeCap.Round)
                            )
                        }
                    }

                    Box {
                        AppIconButton(onClick = { showMenu = true }) {
                            AppIcon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Goal Options",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        AppDropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            AppDropdownMenuItem(
                                text = { AppTypography.LabelMedium(text = "Delete Goal") },
                                leadingIcon = {
                                    AppIcon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                },
                                onClick = {
                                    showMenu = false
                                    onDelete()
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(AppDimensions.paddingMedium))

            // Full-width prominent light lavender "Add Funds" button
            AppButton(
                onClick = onDepositClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(AppDimensions.cornerLarge),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFA5B4FC),
                    contentColor = Color(0xFF1E1B4B)
                )
            ) {
                AppTypography.ButtonText(
                    text = stringResource(R.string.add_funds),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E1B4B)
                )
            }
        }
    }
}

