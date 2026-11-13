package com.example.expensetracker.presentation.categories.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
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
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppBottomSheet
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppIconButton
import com.example.expensetracker.core.designsystem.components.AppOutlinedTextField
import com.example.expensetracker.core.designsystem.components.AppSurface
import com.example.expensetracker.domain.model.Category
import com.example.expensetracker.domain.model.Subcategory
import com.example.expensetracker.presentation.components.CategoryIcon

@Composable
fun CategoryDetailBottomSheet(
    category: Category,
    onAddSubcategory: (String) -> Unit,
    onDeleteSubcategory: (Subcategory) -> Unit,
    onDismiss: () -> Unit
) {
    var newSubName by remember { mutableStateOf("") }

    AppBottomSheet(
        onDismissRequest = onDismiss,
        title = category.name
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CategoryIcon(iconName = category.iconName, colorHex = category.colorHex)
                Spacer(modifier = Modifier.width(AppDimensions.paddingMedium))
                Column {
                    AppTypography.TitleMedium(text = "${category.type.displayName} Category")
                    AppTypography.Caption(text = stringResource(R.string.manage_subcategories))
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
            ) {
                AppOutlinedTextField(
                    value = newSubName,
                    onValueChange = { newSubName = it },
                    label = { AppTypography.LabelMedium(text = stringResource(R.string.new_subcategory_label)) },
                    modifier = Modifier.weight(1f)
                )
                AppIconButton(
                    onClick = {
                        if (newSubName.trim().isNotEmpty()) {
                            onAddSubcategory(newSubName)
                            newSubName = ""
                        }
                    },
                    enabled = newSubName.trim().isNotEmpty()
                ) {
                    AppIcon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Subcategory",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            if (category.subcategories.isEmpty()) {
                AppTypography.BodySmall(
                    text = stringResource(R.string.no_subcategories_yet),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                category.subcategories.forEach { sub ->
                    AppSurface(
                        shape = RoundedCornerShape(AppDimensions.cornerMedium),
                        color = MaterialTheme.colorScheme.surfaceContainer,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = AppDimensions.paddingMedium, vertical = AppDimensions.paddingSmall),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AppTypography.BodyMedium(text = sub.name)
                            AppIconButton(onClick = { onDeleteSubcategory(sub) }) {
                                AppIcon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Subcategory",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))
        }
    }
}

