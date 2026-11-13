package com.example.expensetracker.presentation.settings.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Nightlight
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
import com.example.expensetracker.domain.model.ThemeMode

@Composable
fun ThemeModesCard(
    selectedMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier
) {
    AppCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(AppDimensions.paddingDefault),
            verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
        ) {
            AppTypography.TitleMedium(
                text = stringResource(R.string.theme_modes),
                fontWeight = FontWeight.SemiBold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
            ) {
                val modes = listOf(
                    Triple(ThemeMode.LIGHT, stringResource(R.string.theme_light), Icons.Default.LightMode),
                    Triple(ThemeMode.DARK, stringResource(R.string.theme_dark), Icons.Default.DarkMode),
                    Triple(ThemeMode.SYSTEM, stringResource(R.string.theme_system), Icons.Default.BrightnessAuto),
                    Triple(ThemeMode.AMOLED, stringResource(R.string.theme_amoled), Icons.Default.Nightlight)
                )

                modes.forEach { (mode, label, icon) ->
                    val isSelected = selectedMode == mode
                    val containerColor = if (isSelected) {
                        Color.White
                    } else {
                        MaterialTheme.colorScheme.surfaceContainerHigh
                    }
                    val contentColor = if (isSelected) {
                        Color(0xFF0F172A)
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }

                    AppSurface(
                        onClick = { onThemeModeChange(mode) },
                        shape = RoundedCornerShape(AppDimensions.cornerLarge),
                        color = containerColor,
                        border = if (isSelected) null else BorderStroke(AppDimensions.borders.thin, MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = AppDimensions.paddingMedium, horizontal = AppDimensions.paddingMicro),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            AppIcon(
                                imageVector = icon,
                                contentDescription = label,
                                tint = contentColor,
                                modifier = Modifier.size(AppDimensions.icons.medium)
                            )
                            Spacer(modifier = Modifier.height(AppDimensions.paddingMicro))
                            AppTypography.Caption(
                                text = label,
                                color = contentColor,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 2
                            )
                        }
                    }
                }
            }
        }
    }
}

