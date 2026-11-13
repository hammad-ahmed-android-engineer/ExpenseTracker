package com.example.expensetracker.presentation.settings.components

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
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileOpen
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.expensetracker.R
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppCard
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppSurface

@Composable
fun DataExportSafCard(
    onExportJson: () -> Unit,
    onExportCsv: () -> Unit,
    onImportJson: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(AppDimensions.paddingDefault),
            verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
        ) {
            AppTypography.TitleMedium(
                text = stringResource(R.string.data_export_saf_backup),
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(AppDimensions.paddingMicro))

            // 1. Export JSON Backup
            AppSurface(
                onClick = onExportJson,
                shape = RoundedCornerShape(AppDimensions.cornerMedium),
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(AppDimensions.paddingMedium),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
                ) {
                    AppIcon(
                        imageVector = Icons.Default.FileDownload,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(AppDimensions.icons.large)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        AppTypography.BodyMedium(
                            text = "Export Backup (JSON)",
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        AppTypography.Caption(
                            text = "Save full offline database backup via Android SAF",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // 2. Export CSV Spreadsheet
            AppSurface(
                onClick = onExportCsv,
                shape = RoundedCornerShape(AppDimensions.cornerMedium),
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(AppDimensions.paddingMedium),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
                ) {
                    AppIcon(
                        imageVector = Icons.Default.TableChart,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(AppDimensions.icons.large)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        AppTypography.BodyMedium(
                            text = "Export Transactions (CSV)",
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        AppTypography.Caption(
                            text = "Download transaction history for Excel or Sheets",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // 3. Restore / Import JSON Backup
            AppSurface(
                onClick = onImportJson,
                shape = RoundedCornerShape(AppDimensions.cornerMedium),
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(AppDimensions.paddingMedium),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
                ) {
                    AppIcon(
                        imageVector = Icons.Default.FileOpen,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(AppDimensions.icons.large)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        AppTypography.BodyMedium(
                            text = "Restore Backup (JSON)",
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        AppTypography.Caption(
                            text = "Select and import a saved SpendWise backup file",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}


