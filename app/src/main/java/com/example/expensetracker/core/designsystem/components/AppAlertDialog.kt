package com.example.expensetracker.core.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppAlertDialog(
    onDismissRequest: () -> Unit,
    title: String,
    text: @Composable (() -> Unit)? = null,
    confirmButtonText: String,
    onConfirm: () -> Unit,
    dismissButtonText: String? = null,
    onDismiss: (() -> Unit)? = null,
    isDestructive: Boolean = false,
    confirmEnabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        properties = DialogProperties(usePlatformDefaultWidth = true)
    ) {
        AppSurface(
            shape = RoundedCornerShape(AppDimensions.cornerExtraLarge),
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            tonalElevation = AppDimensions.elevations.high
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppDimensions.components.dialogPadding)
            ) {
                AppTypography.TitleLarge(
                    text = title,
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (text != null) {
                    Spacer(modifier = Modifier.height(AppDimensions.paddingMedium))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = false)
                            .verticalScroll(rememberScrollState())
                    ) {
                        text()
                    }
                }

                Spacer(modifier = Modifier.height(AppDimensions.paddingLarge))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        AppDimensions.paddingSmall,
                        Alignment.End
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (dismissButtonText != null && onDismiss != null) {
                        AppTextButton(onClick = onDismiss) {
                            AppTypography.ButtonText(
                                text = dismissButtonText,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    AppButton(
                        onClick = onConfirm,
                        enabled = confirmEnabled,
                        modifier = Modifier,
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = if (isDestructive) {
                                MaterialTheme.colorScheme.error
                            } else {
                                MaterialTheme.colorScheme.primary
                            },
                            contentColor = if (isDestructive) {
                                MaterialTheme.colorScheme.onError
                            } else {
                                MaterialTheme.colorScheme.onPrimary
                            }
                        )
                    ) {
                        AppTypography.ButtonText(text = confirmButtonText)
                    }
                }
            }
        }
    }
}
