package com.example.expensetracker.presentation.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.expensetracker.R
import com.example.expensetracker.core.common.formatDate
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppAlertDialog
import com.example.expensetracker.core.designsystem.components.AppDivider
import com.example.expensetracker.core.designsystem.components.AppRadioButton
import com.example.expensetracker.core.designsystem.components.AppSurface
import com.example.expensetracker.domain.model.BackupPreview
import com.example.expensetracker.domain.model.BackupRestoreMode
import com.example.expensetracker.presentation.components.KeyValueRow

@Composable
fun BackupRestorePreviewDialog(
    preview: BackupPreview,
    onConfirm: (BackupRestoreMode) -> Unit,
    onDismiss: () -> Unit
) {
    var mode by remember { mutableStateOf(BackupRestoreMode.MERGE_SKIP_DUPLICATES) }

    AppAlertDialog(
        onDismissRequest = onDismiss,
        title = "Backup Preview",
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)) {
                KeyValueRow(label = "Exported", value = preview.exportedAt.formatDate("MMM dd, yyyy"))
                KeyValueRow(label = "Transactions", value = "${preview.transactionCount}")
                KeyValueRow(label = "Accounts", value = "${preview.accountCount}")
                KeyValueRow(label = "Categories", value = "${preview.categoryCount}")
                KeyValueRow(label = "Budgets / Goals", value = "${preview.budgetCount} / ${preview.savingsGoalCount}")
                AppDivider()
                AppTypography.TitleSmall(text = "Restore Mode", color = MaterialTheme.colorScheme.primary)
                AppSurface(
                    onClick = { mode = BackupRestoreMode.MERGE_SKIP_DUPLICATES },
                    shape = RoundedCornerShape(AppDimensions.cornerMedium),
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(AppDimensions.paddingSmall),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppRadioButton(
                            selected = mode == BackupRestoreMode.MERGE_SKIP_DUPLICATES,
                            onClick = { mode = BackupRestoreMode.MERGE_SKIP_DUPLICATES }
                        )
                        Column {
                            AppTypography.LabelLarge(text = "Merge (Skip Duplicates)")
                            AppTypography.Caption(text = "Keep existing records and import new ones")
                        }
                    }
                }
                AppSurface(
                    onClick = { mode = BackupRestoreMode.REPLACE_ALL },
                    shape = RoundedCornerShape(AppDimensions.cornerMedium),
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(AppDimensions.paddingSmall),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppRadioButton(
                            selected = mode == BackupRestoreMode.REPLACE_ALL,
                            onClick = { mode = BackupRestoreMode.REPLACE_ALL }
                        )
                        Column {
                            AppTypography.LabelLarge(text = "Replace All Data")
                            AppTypography.Caption(text = "Overwrite current database with backup")
                        }
                    }
                }
            }
        },
        confirmButtonText = "Restore Backup",
        isDestructive = mode == BackupRestoreMode.REPLACE_ALL,
        onConfirm = { onConfirm(mode) },
        dismissButtonText = stringResource(R.string.action_cancel),
        onDismiss = onDismiss
    )
}

