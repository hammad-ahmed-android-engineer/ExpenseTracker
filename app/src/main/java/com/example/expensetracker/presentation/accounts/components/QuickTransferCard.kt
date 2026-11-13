package com.example.expensetracker.presentation.accounts.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.expensetracker.R
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppCard
import com.example.expensetracker.core.designsystem.components.AppDropdownMenu
import com.example.expensetracker.core.designsystem.components.AppDropdownMenuItem
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppIconButton
import com.example.expensetracker.core.designsystem.components.AppSurface
import com.example.expensetracker.domain.model.Account

@Composable
fun QuickTransferCard(
    accounts: List<Account>,
    onOpenFullTransfer: (fromId: Long, toId: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var sourceAccount by remember(accounts) {
        mutableStateOf(accounts.firstOrNull { it.name.contains("Cash", ignoreCase = true) } ?: accounts.firstOrNull())
    }
    var destAccount by remember(accounts) {
        mutableStateOf(accounts.firstOrNull { it.name.contains("Bank", ignoreCase = true) || it.name.contains("Checking", ignoreCase = true) } ?: accounts.getOrNull(1))
    }

    var showSourceMenu by remember { mutableStateOf(false) }
    var showDestMenu by remember { mutableStateOf(false) }

    AppCard(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(AppDimensions.paddingDefault)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppTypography.TitleMedium(
                    text = stringResource(R.string.quick_transfer),
                    fontWeight = FontWeight.SemiBold
                )
                AppIconButton(onClick = {
                    val sId = sourceAccount?.id ?: 0L
                    val dId = destAccount?.id ?: 0L
                    onOpenFullTransfer(sId, dId)
                }) {
                    AppIcon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Transfer info",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(AppDimensions.icons.small)
                    )
                }
            }

            Spacer(modifier = Modifier.height(AppDimensions.paddingMicro))
            AppTypography.Caption(
                text = stringResource(R.string.atomic_log_select_fields),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(AppDimensions.paddingMedium))

            // Selectors Row: Source [Cash ▾]  to  Destination [Bank ▾]
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Source Field
                Column(modifier = Modifier.weight(1f)) {
                    AppTypography.LabelSmall(
                        text = stringResource(R.string.source_label),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(AppDimensions.paddingMicro))
                    AppSurface(
                        onClick = { showSourceMenu = true },
                        shape = RoundedCornerShape(AppDimensions.cornerMedium),
                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                        border = BorderStroke(AppDimensions.borders.thin, MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = AppDimensions.paddingMedium, vertical = AppDimensions.paddingSmall),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AppTypography.BodyMedium(
                                text = sourceAccount?.name ?: "Cash",
                                fontWeight = FontWeight.Medium,
                                maxLines = 1
                            )
                            AppIcon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        AppDropdownMenu(
                            expanded = showSourceMenu,
                            onDismissRequest = { showSourceMenu = false }
                        ) {
                            accounts.forEach { acc ->
                                AppDropdownMenuItem(
                                    text = { AppTypography.LabelMedium(text = acc.name) },
                                    onClick = {
                                        sourceAccount = acc
                                        showSourceMenu = false
                                    }
                                )
                            }
                        }
                    }
                }

                AppTypography.LabelSmall(
                    text = stringResource(R.string.to_label),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = AppDimensions.paddingMedium)
                )

                // Destination Field
                Column(modifier = Modifier.weight(1f)) {
                    AppTypography.LabelSmall(
                        text = stringResource(R.string.destination_label),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(AppDimensions.paddingMicro))
                    AppSurface(
                        onClick = { showDestMenu = true },
                        shape = RoundedCornerShape(AppDimensions.cornerMedium),
                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                        border = BorderStroke(AppDimensions.borders.thin, MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = AppDimensions.paddingMedium, vertical = AppDimensions.paddingSmall),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AppTypography.BodyMedium(
                                text = destAccount?.name ?: "Bank",
                                fontWeight = FontWeight.Medium,
                                maxLines = 1
                            )
                            AppIcon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        AppDropdownMenu(
                            expanded = showDestMenu,
                            onDismissRequest = { showDestMenu = false }
                        ) {
                            accounts.forEach { acc ->
                                AppDropdownMenuItem(
                                    text = { AppTypography.LabelMedium(text = acc.name) },
                                    onClick = {
                                        destAccount = acc
                                        showDestMenu = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(AppDimensions.paddingMedium))

            // Bottom note
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingMicro)
            ) {
                AppIcon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = Color(0xFF34D399),
                    modifier = Modifier.size(AppDimensions.icons.small)
                )
                AppTypography.Caption(
                    text = stringResource(R.string.automatic_double_counting_prevention),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

