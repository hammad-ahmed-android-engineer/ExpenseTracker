package com.example.expensetracker.presentation.settings.components

import android.os.Build
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.expensetracker.R
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppCard
import com.example.expensetracker.core.designsystem.components.AppDivider
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppSegmentedSelector
import com.example.expensetracker.core.designsystem.components.AppSurface
import com.example.expensetracker.core.designsystem.components.AppSwitch
import com.example.expensetracker.core.designsystem.theme.getDarkColors
import com.example.expensetracker.core.designsystem.theme.getLightColors
import com.example.expensetracker.domain.model.ColorPalette
import com.example.expensetracker.domain.model.ThemeMode
import com.example.expensetracker.domain.model.UserPreferences

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ThemeAppearanceCard(
    preferences: UserPreferences,
    onThemeModeChange: (ThemeMode) -> Unit,
    onColorPaletteChange: (ColorPalette) -> Unit,
    onDynamicColorChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = when (preferences.themeMode) {
        ThemeMode.DARK, ThemeMode.AMOLED -> true
        ThemeMode.LIGHT -> false
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    AppCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(AppDimensions.paddingDefault),
            verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
        ) {
            AppTypography.TitleSmall(text = stringResource(R.string.theme_mode))
            AppSegmentedSelector(
                options = ThemeMode.entries,
                selectedOption = preferences.themeMode,
                onOptionSelected = onThemeModeChange,
                labelProvider = { it.displayName }
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                AppDivider()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        AppTypography.TitleSmall(text = "Material You Dynamic Color")
                        AppTypography.Caption(text = "Adapt palette from wallpaper")
                    }
                    AppSwitch(
                        checked = preferences.isDynamicColorEnabled,
                        onCheckedChange = onDynamicColorChange
                    )
                }
            }

            AppDivider()
            AppTypography.TitleSmall(text = stringResource(R.string.color_scheme_palette))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium),
                verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
            ) {
                ColorPalette.entries.forEach { palette ->
                    val colors = if (isDark) palette.getDarkColors(false) else palette.getLightColors()
                    val isSelected = preferences.colorPalette == palette

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AppSurface(
                            onClick = { onColorPaletteChange(palette) },
                            shape = CircleShape,
                            color = colors.previewSwatch,
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
                                        tint = colors.onPrimary,
                                        modifier = Modifier.size(AppDimensions.icons.small)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(AppDimensions.paddingMicro))
                        AppTypography.Caption(
                            text = palette.displayName,
                            textAlign = TextAlign.Center,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

