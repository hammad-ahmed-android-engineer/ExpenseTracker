package com.example.expensetracker.core.designsystem.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.LocalDimensions
import com.example.expensetracker.core.designsystem.resolveDimensions
import com.example.expensetracker.domain.model.ColorPalette
import com.example.expensetracker.domain.model.ThemeMode

@Composable
fun ExpenseTrackerTheme(
    themeMode: ThemeMode = ThemeMode.DARK,
    colorPalette: ColorPalette = ColorPalette.LAVENDER,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK, ThemeMode.AMOLED -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    val isAmoled = themeMode == ThemeMode.AMOLED
    val context = LocalContext.current
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !darkTheme
            insetsController.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    val colorScheme: ColorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val base = if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            if (isAmoled) base.copy(background = androidx.compose.ui.graphics.Color.Black, surface = androidx.compose.ui.graphics.Color.Black) else base
        }
        darkTheme -> {
            val t = colorPalette.getDarkColors(isAmoled)
            darkColorScheme(
                primary = t.primary,
                onPrimary = t.onPrimary,
                primaryContainer = t.primaryContainer,
                onPrimaryContainer = t.onPrimaryContainer,
                secondary = t.secondary,
                onSecondary = t.onSecondary,
                secondaryContainer = t.secondaryContainer,
                onSecondaryContainer = t.onSecondaryContainer,
                tertiary = t.tertiary,
                onTertiary = t.onTertiary,
                tertiaryContainer = t.tertiaryContainer,
                onTertiaryContainer = t.onTertiaryContainer,
                background = t.background,
                onBackground = t.onBackground,
                surface = t.surface,
                onSurface = t.onSurface,
                surfaceVariant = t.surfaceVariant,
                onSurfaceVariant = t.onSurfaceVariant,
                surfaceContainerLowest = t.surfaceContainerLowest,
                surfaceContainerLow = t.surfaceContainerLow,
                surfaceContainer = t.surfaceContainer,
                surfaceContainerHigh = t.surfaceContainerHigh,
                surfaceContainerHighest = t.surfaceContainerHighest,
                outline = t.outline,
                outlineVariant = t.outlineVariant,
                error = t.error,
                onError = t.onError,
                errorContainer = t.errorContainer,
                onErrorContainer = t.onErrorContainer
            )
        }
        else -> {
            val t = colorPalette.getLightColors()
            lightColorScheme(
                primary = t.primary,
                onPrimary = t.onPrimary,
                primaryContainer = t.primaryContainer,
                onPrimaryContainer = t.onPrimaryContainer,
                secondary = t.secondary,
                onSecondary = t.onSecondary,
                secondaryContainer = t.secondaryContainer,
                onSecondaryContainer = t.onSecondaryContainer,
                tertiary = t.tertiary,
                onTertiary = t.onTertiary,
                tertiaryContainer = t.tertiaryContainer,
                onTertiaryContainer = t.onTertiaryContainer,
                background = t.background,
                onBackground = t.onBackground,
                surface = t.surface,
                onSurface = t.onSurface,
                surfaceVariant = t.surfaceVariant,
                onSurfaceVariant = t.onSurfaceVariant,
                surfaceContainerLowest = t.surfaceContainerLowest,
                surfaceContainerLow = t.surfaceContainerLow,
                surfaceContainer = t.surfaceContainer,
                surfaceContainerHigh = t.surfaceContainerHigh,
                surfaceContainerHighest = t.surfaceContainerHighest,
                outline = t.outline,
                outlineVariant = t.outlineVariant,
                error = t.error,
                onError = t.onError,
                errorContainer = t.errorContainer,
                onErrorContainer = t.onErrorContainer
            )
        }
    }

    val dimensions = resolveDimensions()
    val typography = remember(dimensions) {
        AppTypography.material3Typography(dimensions.typeScale)
    }
    val semantics = remember(darkTheme) {
        if (darkTheme) darkSemanticColors() else lightSemanticColors()
    }

    CompositionLocalProvider(
        LocalDimensions provides dimensions,
        LocalSemanticColors provides semantics
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            content = content
        )
    }
}
