package com.example.expensetracker.presentation.budgets.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.expensetracker.R
import com.example.expensetracker.core.common.formatCurrency
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppCard
import com.example.expensetracker.core.designsystem.components.AppDropdownMenu
import com.example.expensetracker.core.designsystem.components.AppDropdownMenuItem
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppIconButton
import com.example.expensetracker.domain.model.Budget
import java.util.Locale

import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun BudgetCardItem(
    budget: Budget,
    currencyCode: String,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pct = if (budget.amountLimit > 0) {
        ((budget.spentAmount.toFloat() / budget.amountLimit.toFloat()) * 100f).coerceIn(0f, 100f)
    } else 0f

    val progressColor = when {
        pct >= 90f -> Color(0xFFEF4444) // Coral red
        pct >= 70f -> Color(0xFFFB923C) // Orange
        else -> Color(0xFF10B981) // Emerald
    }

    var showMenu by remember { mutableStateOf(false) }

    AppCard(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = AppDimensions.paddingDefault,
                    top = AppDimensions.paddingDefault,
                    bottom = AppDimensions.paddingDefault,
                    end = AppDimensions.paddingSmall
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = AppDimensions.paddingSmall)
            ) {
                AppTypography.TitleMedium(
                    text = budget.name,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(AppDimensions.paddingMicro))

                AppTypography.BodySmall(
                    text = "${budget.spentAmount.formatCurrency(currencyCode)} / ${
                        budget.amountLimit.formatCurrency(
                            currencyCode
                        )
                    } (${"%.0f".format(Locale.US, pct)}%)",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Right-aligned horizontal progress bar with percentage pill inside
            Box(
                modifier = Modifier
                    .width(AppDimensions.components.emptyStateIconContainer * 2)
                    .height(AppDimensions.paddingLarge)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
            ) {
                val fillFraction = (pct / 100f).coerceIn(0.12f, 1f)
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = fillFraction)
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .background(progressColor),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Box(
                        modifier = Modifier
                            .padding(end = AppDimensions.paddingMicro)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.25f))
                            .padding(horizontal = AppDimensions.paddingMicro, vertical = AppDimensions.borders.thin),
                        contentAlignment = Alignment.Center
                    ) {
                        AppTypography.Caption(
                            text = "${"%.0f".format(Locale.US, pct)}%",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Options menu button for budget management (e.g. Delete)
            Box {
                AppIconButton(onClick = { showMenu = true }) {
                    AppIcon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = stringResource(R.string.budget_options),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                AppDropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    AppDropdownMenuItem(
                        text = { AppTypography.LabelMedium(text = stringResource(R.string.delete_budget)) },
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
}

