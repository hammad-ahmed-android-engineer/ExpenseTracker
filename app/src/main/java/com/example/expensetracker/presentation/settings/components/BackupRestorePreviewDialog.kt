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
import androidx.compose.runtime.saveable.rememberSaveable
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
    var mode by rememberSaveable { mutableStateOf(BackupRestoreMode.MERGE_SKIP_DUPLICATES) }

    AppAlertDialog(
        onDismissRequest = onDismiss,
        title = stringResource(R.string.restore_preview_title),
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)) {
                KeyValueRow(label = stringResource(R.string.exported_label), value = preview.exportedAt.formatDate("MMM dd, yyyy"))
                KeyValueRow(label = stringResource(R.string.transactions_label), value = "${preview.transactionCount}")
                KeyValueRow(label = stringResource(R.string.accounts_label), value = "${preview.accountCount}")
                KeyValueRow(label = stringResource(R.string.categories_label), value = "${preview.categoryCount}")
                KeyValueRow(label = stringResource(R.string.budgets_goals_label), value = "${preview.budgetCount} / ${preview.savingsGoalCount}")
                AppDivider()
                AppTypography.TitleSmall(text = stringResource(R.string.restore_mode), color = MaterialTheme.colorScheme.primary)
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
                            AppTypography.LabelLarge(text = stringResource(R.string.merge_skip_duplicates))
                            AppTypography.Caption(text = stringResource(R.string.merge_skip_duplicates_desc))
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
                            AppTypography.LabelLarge(text = stringResource(R.string.replace_all_data))
                            AppTypography.Caption(text = stringResource(R.string.replace_all_data_desc))
                        }
                    }
                }
            }
        },
        confirmButtonText = stringResource(R.string.restore_backup),
        isDestructive = mode == BackupRestoreMode.REPLACE_ALL,
        onConfirm = { onConfirm(mode) },
        dismissButtonText = stringResource(R.string.action_cancel),
        onDismiss = onDismiss
    )
}

