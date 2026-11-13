package com.example.expensetracker.presentation.categories.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import com.example.expensetracker.R
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppAlertDialog
import com.example.expensetracker.core.designsystem.components.AppFilterChip
import com.example.expensetracker.core.designsystem.components.AppOutlinedTextField
import com.example.expensetracker.core.designsystem.components.AppSurface
import com.example.expensetracker.core.utils.toComposeColor
import com.example.expensetracker.domain.model.TransactionType
import com.example.expensetracker.presentation.components.CategoryIcon

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddCategoryDialog(
    type: TransactionType,
    onDismiss: () -> Unit,
    onConfirm: (name: String, icon: String, color: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedIcon by remember { mutableStateOf("Restaurant") }
    var selectedColor by remember { mutableStateOf("#10B981") }

    val icons = listOf(
        "Restaurant", "ShoppingCart", "DirectionsCar", "ShoppingBag",
        "Movie", "LocalHospital", "School", "Home",
        "Flight", "Payments", "Work", "Store"
    )
    val colors = listOf(
        "#10B981", "#2563EB", "#D97706", "#DC2626",
        "#7C3AED", "#DB2777", "#0891B2", "#4F46E5"
    )

    AppAlertDialog(
        onDismissRequest = onDismiss,
        title = stringResource(R.string.new_category_title, type.displayName),
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)) {
                AppOutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { AppTypography.LabelMedium(text = stringResource(R.string.category_name_label)) },
                    modifier = Modifier.fillMaxWidth()
                )

                AppTypography.TitleSmall(text = stringResource(R.string.color_label))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall),
                    verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
                ) {
                    colors.forEach { hex ->
                        val isSelected = selectedColor == hex
                        AppSurface(
                            onClick = { selectedColor = hex },
                            shape = CircleShape,
                            color = hex.toComposeColor(),
                            border = if (isSelected) {
                                BorderStroke(AppDimensions.borders.thick, MaterialTheme.colorScheme.onSurface)
                            } else null,
                            modifier = Modifier.size(AppDimensions.components.colorPickerCircle)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(hex.toComposeColor())
                            )
                        }
                    }
                }

                AppTypography.TitleSmall(text = stringResource(R.string.icon_label))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingMicro),
                    verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMicro)
                ) {
                    icons.forEach { iconName ->
                        AppFilterChip(
                            selected = selectedIcon == iconName,
                            onClick = { selectedIcon = iconName },
                            label = {
                                CategoryIcon(
                                    iconName = iconName,
                                    colorHex = selectedColor,
                                    size = AppDimensions.icons.large,
                                    iconSize = AppDimensions.icons.small
                                )
                            }
                        )
                    }
                }
            }
        },
        confirmButtonText = stringResource(R.string.action_save),
        confirmEnabled = name.trim().isNotEmpty(),
        onConfirm = { onConfirm(name, selectedIcon, selectedColor) },
        dismissButtonText = stringResource(R.string.action_cancel),
        onDismiss = onDismiss
    )
}

