package com.example.expensetracker.presentation.settings.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.expensetracker.R
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppCard
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppSurface
import com.example.expensetracker.domain.model.ColorPalette

@Composable
fun PalettePickerCard(
    selectedPalette: ColorPalette,
    onColorPaletteChange: (ColorPalette) -> Unit,
    modifier: Modifier = Modifier
) {
    AppCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(AppDimensions.paddingDefault),
            verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
        ) {
            AppTypography.TitleMedium(
                text = stringResource(R.string.material_you_palette),
                fontWeight = FontWeight.SemiBold
            )

            // 12 circular color swatches displayed in 2 rows of 6
            val palettes = ColorPalette.entries.take(12)
            val chunked = palettes.chunked(6)

            chunked.forEach { rowPalettes ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    rowPalettes.forEach { palette ->
                        val isSelected = selectedPalette == palette
                        val swatchColor = Color(palette.hexColor)

                        AppSurface(
                            onClick = { onColorPaletteChange(palette) },
                            shape = CircleShape,
                            color = swatchColor,
                            border = if (isSelected) {
                                BorderStroke(AppDimensions.borders.thick, MaterialTheme.colorScheme.onSurface)
                            } else null,
                            modifier = Modifier.size(AppDimensions.components.colorPickerCircle)
                        ) {
                            if (isSelected) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    AppIcon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = palette.displayName,
                                        tint = Color.White,
                                        modifier = Modifier.size(AppDimensions.icons.small)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

