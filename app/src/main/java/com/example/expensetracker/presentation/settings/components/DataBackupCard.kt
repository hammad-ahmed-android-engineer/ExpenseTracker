package com.example.expensetracker.presentation.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.expensetracker.R
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppButton
import com.example.expensetracker.core.designsystem.components.AppCard
import com.example.expensetracker.core.designsystem.components.AppDivider
import com.example.expensetracker.core.designsystem.components.AppOutlinedButton
import com.example.expensetracker.core.designsystem.components.AppTextButton

@Composable
fun DataBackupCard(
    onExportCsv: () -> Unit,
    onExportJson: () -> Unit,
    onCreateBackup: () -> Unit,
    onRestoreBackup: () -> Unit,
    onSeedSampleData: () -> Unit,
    onClearAllData: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(AppDimensions.paddingDefault),
            verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
        ) {
            AppTypography.TitleSmall(text = "Export Records")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
            ) {
                AppOutlinedButton(onClick = onExportCsv, modifier = Modifier.weight(1f)) {
                    AppTypography.ButtonText(text = "Export CSV")
                }
                AppOutlinedButton(onClick = onExportJson, modifier = Modifier.weight(1f)) {
                    AppTypography.ButtonText(text = "Export JSON")
                }
            }

            AppDivider()

            AppTypography.TitleSmall(text = "Offline Backup & Restore")
            AppTypography.Caption(text = "Save or restore all accounts, categories, transactions, budgets, and goals locally")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
            ) {
                AppButton(onClick = onCreateBackup, modifier = Modifier.weight(1f)) {
                    AppTypography.ButtonText(text = "Create Backup")
                }
                AppOutlinedButton(onClick = onRestoreBackup, modifier = Modifier.weight(1f)) {
                    AppTypography.ButtonText(text = "Restore")
                }
            }

            AppDivider()

            AppTypography.TitleSmall(text = stringResource(R.string.sample_data_generator))
            AppTypography.BodySmall(
                text = stringResource(R.string.sample_data_desc),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            AppButton(
                onClick = onSeedSampleData,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                AppTypography.ButtonText(text = stringResource(R.string.seed_sample_data))
            }

            AppTextButton(
                onClick = onClearAllData,
                modifier = Modifier.fillMaxWidth()
            ) {
                AppTypography.ButtonText(
                    text = "Reset & Clear All Data",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

