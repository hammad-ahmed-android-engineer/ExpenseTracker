package com.example.expensetracker.presentation.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.expensetracker.R
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppBottomSheet
import com.example.expensetracker.core.designsystem.components.AppDivider
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppIconButton
import com.example.expensetracker.core.designsystem.components.AppSurface
import com.example.expensetracker.core.designsystem.components.AppSwitch
import com.example.expensetracker.domain.model.DashboardWidgetType
import com.example.expensetracker.domain.model.UserPreferences

@Composable
fun DashboardCustomizeBottomSheet(
    preferences: UserPreferences,
    onToggleWidget: (DashboardWidgetType) -> Unit,
    onMoveWidget: (DashboardWidgetType, Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    val ordered = preferences.orderedWidgets()
    val hiddenKeys = preferences.hiddenDashboardWidgets
        .split(",")
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .toSet()

    AppBottomSheet(
        onDismissRequest = onDismiss,
        title = stringResource(R.string.customize_dashboard)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)) {
            AppTypography.LabelLarge(
                text = stringResource(R.string.visible_widgets_order),
                color = MaterialTheme.colorScheme.primary
            )
            AppDivider()

            ordered.forEachIndexed { index, widget ->
                val isVisible = widget.key !in hiddenKeys
                AppSurface(
                    onClick = { onToggleWidget(widget) },
                    shape = RoundedCornerShape(AppDimensions.cornerMedium),
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = AppDimensions.paddingMedium,
                                vertical = AppDimensions.paddingSmall
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppTypography.BodyMedium(
                            text = widget.displayName,
                            modifier = Modifier.weight(1f)
                        )
                        AppIconButton(
                            onClick = { onMoveWidget(widget, true) },
                            enabled = index > 0
                        ) {
                            AppIcon(
                                imageVector = Icons.Default.ArrowUpward,
                                contentDescription = "Move Up"
                            )
                        }
                        AppIconButton(
                            onClick = { onMoveWidget(widget, false) },
                            enabled = index < ordered.lastIndex
                        ) {
                            AppIcon(
                                imageVector = Icons.Default.ArrowDownward,
                                contentDescription = "Move Down"
                            )
                        }
                        AppSwitch(
                            checked = isVisible,
                            onCheckedChange = { onToggleWidget(widget) }
                        )
                    }
                }
            }
        }
    }
}

