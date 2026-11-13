package com.example.expensetracker.core.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.example.expensetracker.domain.model.ColorPalette

@Immutable
data class ThemeColors(
    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,
    val secondary: Color,
    val onSecondary: Color,
    val secondaryContainer: Color,
    val onSecondaryContainer: Color,
    val tertiary: Color,
    val onTertiary: Color,
    val tertiaryContainer: Color,
    val onTertiaryContainer: Color,
    val background: Color,
    val onBackground: Color,
    val surface: Color,
    val onSurface: Color,
    val surfaceVariant: Color,
    val onSurfaceVariant: Color,
    val surfaceContainerLowest: Color,
    val surfaceContainerLow: Color,
    val surfaceContainer: Color,
    val surfaceContainerHigh: Color,
    val surfaceContainerHighest: Color,
    val outline: Color,
    val outlineVariant: Color,
    val error: Color,
    val onError: Color,
    val errorContainer: Color,
    val onErrorContainer: Color,
    val previewSwatch: Color
)

@Immutable
data class SemanticColors(
    val income: Color,
    val onIncome: Color,
    val incomeContainer: Color,
    val expense: Color,
    val onExpense: Color,
    val expenseContainer: Color,
    val warning: Color,
    val onWarning: Color,
    val warningContainer: Color,
    val transfer: Color,
    val onTransfer: Color,
    val transferContainer: Color,
    val savings: Color,
    val savingsContainer: Color,
    val chartPalette: List<Color>
)

val LocalSemanticColors = staticCompositionLocalOf<SemanticColors> {
    error("SemanticColors not provided")
}

val semanticColors: SemanticColors
    @Composable
    @ReadOnlyComposable
    get() = LocalSemanticColors.current

fun lightSemanticColors(): SemanticColors = SemanticColors(
    income = Color(0xFF047857),
    onIncome = Color.White,
    incomeContainer = Color(0xFFD1FAE5),
    expense = Color(0xFFDC2626),
    onExpense = Color.White,
    expenseContainer = Color(0xFFFEE2E2),
    warning = Color(0xFFD97706),
    onWarning = Color.White,
    warningContainer = Color(0xFFFEF3C7),
    transfer = Color(0xFF2563EB),
    onTransfer = Color.White,
    transferContainer = Color(0xFFDBEAFE),
    savings = Color(0xFF0891B2),
    savingsContainer = Color(0xFFCFFAFE),
    chartPalette = listOf(
        Color(0xFF2563EB),
        Color(0xFF059669),
        Color(0xFFD97706),
        Color(0xFFDB2777),
        Color(0xFF7C3AED),
        Color(0xFF0891B2),
        Color(0xFFDC2626),
        Color(0xFF4F46E5)
    )
)

fun darkSemanticColors(): SemanticColors = SemanticColors(
    income = Color(0xFF34D399),
    onIncome = Color(0xFF022C22),
    incomeContainer = Color(0xFF064E3B),
    expense = Color(0xFFF87171),
    onExpense = Color(0xFF450A0A),
    expenseContainer = Color(0xFF7F1D1D),
    warning = Color(0xFFFBBF24),
    onWarning = Color(0xFF451A03),
    warningContainer = Color(0xFF78350F),
    transfer = Color(0xFF60A5FA),
    onTransfer = Color(0xFF172554),
    transferContainer = Color(0xFF1E3A8A),
    savings = Color(0xFF22D3EE),
    savingsContainer = Color(0xFF164E63),
    chartPalette = listOf(
        Color(0xFF60A5FA),
        Color(0xFF34D399),
        Color(0xFFFBBF24),
        Color(0xFFF472B6),
        Color(0xFFA78BFA),
        Color(0xFF22D3EE),
        Color(0xFFF87171),
        Color(0xFF818CF8)
    )
)

private fun buildLightTheme(
    primary: Color,
    primaryContainer: Color,
    onPrimaryContainer: Color,
    secondary: Color,
    secondaryContainer: Color,
    onSecondaryContainer: Color,
    tertiary: Color,
    tertiaryContainer: Color,
    onTertiaryContainer: Color,
    tintBg: Color = Color(0xFFF6F7FB),
    tintSurface: Color = Color(0xFFFFFFFF),
    tintSurfaceLow: Color = Color(0xFFF1F3F9),
    tintSurfaceHigh: Color = Color(0xFFE7EBF3),
    onPrimary: Color = Color.White
): ThemeColors = ThemeColors(
    primary = primary,
    onPrimary = onPrimary,
    primaryContainer = primaryContainer,
    onPrimaryContainer = onPrimaryContainer,
    secondary = secondary,
    onSecondary = Color.White,
    secondaryContainer = secondaryContainer,
    onSecondaryContainer = onSecondaryContainer,
    tertiary = tertiary,
    onTertiary = Color.White,
    tertiaryContainer = tertiaryContainer,
    onTertiaryContainer = onTertiaryContainer,
    background = tintBg,
    onBackground = Color(0xFF0F172A),
    surface = tintSurface,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = tintSurfaceHigh,
    onSurfaceVariant = Color(0xFF475569),
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = tintSurfaceLow,
    surfaceContainer = Color(0xFFECEFF6),
    surfaceContainerHigh = tintSurfaceHigh,
    surfaceContainerHighest = Color(0xFFDFE4EE),
    outline = Color(0xFF94A3B8),
    outlineVariant = Color(0xFFE2E8F0),
    error = Color(0xFFDC2626),
    onError = Color.White,
    errorContainer = Color(0xFFFEE2E2),
    onErrorContainer = Color(0xFF7F1D1D),
    previewSwatch = primary
)

private fun buildDarkTheme(
    primary: Color,
    onPrimary: Color,
    primaryContainer: Color,
    onPrimaryContainer: Color,
    secondary: Color,
    secondaryContainer: Color,
    onSecondaryContainer: Color,
    tertiary: Color,
    tertiaryContainer: Color,
    onTertiaryContainer: Color,
    isAmoled: Boolean
): ThemeColors {
    val bg = if (isAmoled) Color.Black else Color(0xFF0B0F17)
    val surf = if (isAmoled) Color(0xFF080A0F) else Color(0xFF111723)
    val surfLow = if (isAmoled) Color(0xFF0E1117) else Color(0xFF161D2B)
    val surfMed = if (isAmoled) Color(0xFF131720) else Color(0xFF1C2434)
    val surfHigh = if (isAmoled) Color(0xFF1B202C) else Color(0xFF232C3E)
    val surfHighest = if (isAmoled) Color(0xFF242A38) else Color(0xFF2C364B)

    return ThemeColors(
        primary = primary,
        onPrimary = onPrimary,
        primaryContainer = primaryContainer,
        onPrimaryContainer = onPrimaryContainer,
        secondary = secondary,
        onSecondary = Color(0xFF0F172A),
        secondaryContainer = secondaryContainer,
        onSecondaryContainer = onSecondaryContainer,
        tertiary = tertiary,
        onTertiary = Color(0xFF0F172A),
        tertiaryContainer = tertiaryContainer,
        onTertiaryContainer = onTertiaryContainer,
        background = bg,
        onBackground = Color(0xFFF1F5F9),
        surface = surf,
        onSurface = Color(0xFFF1F5F9),
        surfaceVariant = surfHigh,
        onSurfaceVariant = Color(0xFF94A3B8),
        surfaceContainerLowest = bg,
        surfaceContainerLow = surfLow,
        surfaceContainer = surfMed,
        surfaceContainerHigh = surfHigh,
        surfaceContainerHighest = surfHighest,
        outline = Color(0xFF64748B),
        outlineVariant = Color(0xFF263042),
        error = Color(0xFFF87171),
        onError = Color(0xFF450A0A),
        errorContainer = Color(0xFF7F1D1D),
        onErrorContainer = Color(0xFFFEE2E2),
        previewSwatch = primary
    )
}

fun ColorPalette.getLightColors(): ThemeColors = when (this) {
    ColorPalette.OCEAN_BLUE -> buildLightTheme(
        primary = Color(0xFF155EEF),
        primaryContainer = Color(0xFFDCE7FF),
        onPrimaryContainer = Color(0xFF0A2463),
        secondary = Color(0xFF0E7490),
        secondaryContainer = Color(0xFFCFFAFE),
        onSecondaryContainer = Color(0xFF083344),
        tertiary = Color(0xFF6366F1),
        tertiaryContainer = Color(0xFFE0E7FF),
        onTertiaryContainer = Color(0xFF1E1B4B)
    )
    ColorPalette.EMERALD_GREEN -> buildLightTheme(
        primary = Color(0xFF047857),
        primaryContainer = Color(0xFFD1FAE5),
        onPrimaryContainer = Color(0xFF022C22),
        secondary = Color(0xFF0F766E),
        secondaryContainer = Color(0xFFCCFBF1),
        onSecondaryContainer = Color(0xFF134E4A),
        tertiary = Color(0xFF2563EB),
        tertiaryContainer = Color(0xFFDBEAFE),
        onTertiaryContainer = Color(0xFF1E3A8A),
        tintBg = Color(0xFFF5F9F7),
        tintSurfaceLow = Color(0xFFEDF5F1),
        tintSurfaceHigh = Color(0xFFE2EDE7)
    )
    ColorPalette.PURPLE_VIOLET -> buildLightTheme(
        primary = Color(0xFF6D28D9),
        primaryContainer = Color(0xFFEDE9FE),
        onPrimaryContainer = Color(0xFF2E1065),
        secondary = Color(0xFF9333EA),
        secondaryContainer = Color(0xFFF3E8FF),
        onSecondaryContainer = Color(0xFF3B0764),
        tertiary = Color(0xFFDB2777),
        tertiaryContainer = Color(0xFFFCE7F3),
        onTertiaryContainer = Color(0xFF500724),
        tintBg = Color(0xFFF8F6FC),
        tintSurfaceLow = Color(0xFFF2EEF9),
        tintSurfaceHigh = Color(0xFFE8E2F4)
    )
    ColorPalette.SUNSET_ORANGE -> buildLightTheme(
        primary = Color(0xFFC2410C),
        primaryContainer = Color(0xFFFFEDD5),
        onPrimaryContainer = Color(0xFF431407),
        secondary = Color(0xFFD97706),
        secondaryContainer = Color(0xFFFEF3C7),
        onSecondaryContainer = Color(0xFF451A03),
        tertiary = Color(0xFFE11D48),
        tertiaryContainer = Color(0xFFFFE4E6),
        onTertiaryContainer = Color(0xFF4C0519),
        tintBg = Color(0xFFFCF8F5),
        tintSurfaceLow = Color(0xFFF8F0EA),
        tintSurfaceHigh = Color(0xFFF0E3D8)
    )
    ColorPalette.ROSE_PINK -> buildLightTheme(
        primary = Color(0xFFBE185D),
        primaryContainer = Color(0xFFFCE7F3),
        onPrimaryContainer = Color(0xFF500724),
        secondary = Color(0xFF9D174D),
        secondaryContainer = Color(0xFFFFE4E6),
        onSecondaryContainer = Color(0xFF4C0519),
        tertiary = Color(0xFF7C3AED),
        tertiaryContainer = Color(0xFFEDE9FE),
        onTertiaryContainer = Color(0xFF2E1065),
        tintBg = Color(0xFFFCF6F9),
        tintSurfaceLow = Color(0xFFF7ECF1),
        tintSurfaceHigh = Color(0xFFEFDDE6)
    )
    ColorPalette.MIDNIGHT -> buildLightTheme(
        primary = Color(0xFF1E3A8A),
        primaryContainer = Color(0xFFDBEAFE),
        onPrimaryContainer = Color(0xFF172554),
        secondary = Color(0xFF334155),
        secondaryContainer = Color(0xFFE2E8F0),
        onSecondaryContainer = Color(0xFF0F172A),
        tertiary = Color(0xFF0284C7),
        tertiaryContainer = Color(0xFFE0F2FE),
        onTertiaryContainer = Color(0xFF082F49)
    )
    ColorPalette.TEAL -> buildLightTheme(
        primary = Color(0xFF0F766E),
        primaryContainer = Color(0xFFCCFBF1),
        onPrimaryContainer = Color(0xFF042F2E),
        secondary = Color(0xFF0E7490),
        secondaryContainer = Color(0xFFCFFAFE),
        onSecondaryContainer = Color(0xFF083344),
        tertiary = Color(0xFF059669),
        tertiaryContainer = Color(0xFFD1FAE5),
        onTertiaryContainer = Color(0xFF022C22),
        tintBg = Color(0xFFF4FAFA),
        tintSurfaceLow = Color(0xFFEBF4F4),
        tintSurfaceHigh = Color(0xFFDCEBEB)
    )
    ColorPalette.MONOCHROME -> buildLightTheme(
        primary = Color(0xFF18181B),
        primaryContainer = Color(0xFFE4E4E7),
        onPrimaryContainer = Color(0xFF09090B),
        secondary = Color(0xFF3F3F46),
        secondaryContainer = Color(0xFFF4F4F5),
        onSecondaryContainer = Color(0xFF18181B),
        tertiary = Color(0xFF52525B),
        tertiaryContainer = Color(0xFFE4E4E7),
        onTertiaryContainer = Color(0xFF18181B),
        tintBg = Color(0xFFFAFAFA),
        tintSurfaceLow = Color(0xFFF4F4F5),
        tintSurfaceHigh = Color(0xFFE4E4E7)
    )
    ColorPalette.FOREST_GREEN -> buildLightTheme(
        primary = Color(0xFF166534),
        primaryContainer = Color(0xFFDCFCE7),
        onPrimaryContainer = Color(0xFF052E16),
        secondary = Color(0xFF3F6212),
        secondaryContainer = Color(0xFFECFCCB),
        onSecondaryContainer = Color(0xFF1A2E05),
        tertiary = Color(0xFF0F766E),
        tertiaryContainer = Color(0xFFCCFBF1),
        onTertiaryContainer = Color(0xFF042F2E),
        tintBg = Color(0xFFF5F9F5),
        tintSurfaceLow = Color(0xFFEDF4ED),
        tintSurfaceHigh = Color(0xFFE0ECE0)
    )
    ColorPalette.CRIMSON_RED -> buildLightTheme(
        primary = Color(0xFFB91C1C),
        primaryContainer = Color(0xFFFEE2E2),
        onPrimaryContainer = Color(0xFF450A0A),
        secondary = Color(0xFF9F1239),
        secondaryContainer = Color(0xFFFFE4E6),
        onSecondaryContainer = Color(0xFF4C0519),
        tertiary = Color(0xFFD97706),
        tertiaryContainer = Color(0xFFFEF3C7),
        onTertiaryContainer = Color(0xFF451A03),
        tintBg = Color(0xFFFCF6F6),
        tintSurfaceLow = Color(0xFFF7ECEC),
        tintSurfaceHigh = Color(0xFFEFDFDF)
    )
}

fun ColorPalette.getDarkColors(isAmoled: Boolean = false): ThemeColors = when (this) {
    ColorPalette.OCEAN_BLUE -> buildDarkTheme(
        primary = Color(0xFF7CA7FF),
        onPrimary = Color(0xFF061B4B),
        primaryContainer = Color(0xFF133A8E),
        onPrimaryContainer = Color(0xFFDCE7FF),
        secondary = Color(0xFF67E8F9),
        secondaryContainer = Color(0xFF155E75),
        onSecondaryContainer = Color(0xFFCFFAFE),
        tertiary = Color(0xFFA5B4FC),
        tertiaryContainer = Color(0xFF3730A3),
        onTertiaryContainer = Color(0xFFE0E7FF),
        isAmoled = isAmoled
    )
    ColorPalette.EMERALD_GREEN -> buildDarkTheme(
        primary = Color(0xFF34D399),
        onPrimary = Color(0xFF022C22),
        primaryContainer = Color(0xFF065F46),
        onPrimaryContainer = Color(0xFFD1FAE5),
        secondary = Color(0xFF5EEAD4),
        secondaryContainer = Color(0xFF115E59),
        onSecondaryContainer = Color(0xFFCCFBF1),
        tertiary = Color(0xFF60A5FA),
        tertiaryContainer = Color(0xFF1E40AF),
        onTertiaryContainer = Color(0xFFDBEAFE),
        isAmoled = isAmoled
    )
    ColorPalette.PURPLE_VIOLET -> buildDarkTheme(
        primary = Color(0xFFC4B5FD),
        onPrimary = Color(0xFF2E1065),
        primaryContainer = Color(0xFF4C1D95),
        onPrimaryContainer = Color(0xFFEDE9FE),
        secondary = Color(0xFFD8B4FE),
        secondaryContainer = Color(0xFF6B21A8),
        onSecondaryContainer = Color(0xFFF3E8FF),
        tertiary = Color(0xFFF472B6),
        tertiaryContainer = Color(0xFF9D174D),
        onTertiaryContainer = Color(0xFFFCE7F3),
        isAmoled = isAmoled
    )
    ColorPalette.SUNSET_ORANGE -> buildDarkTheme(
        primary = Color(0xFFFB923C),
        onPrimary = Color(0xFF431407),
        primaryContainer = Color(0xFF9A3412),
        onPrimaryContainer = Color(0xFFFFEDD5),
        secondary = Color(0xFFFBBF24),
        secondaryContainer = Color(0xFF92400E),
        onSecondaryContainer = Color(0xFFFEF3C7),
        tertiary = Color(0xFFFB7185),
        tertiaryContainer = Color(0xFF9F1239),
        onTertiaryContainer = Color(0xFFFFE4E6),
        isAmoled = isAmoled
    )
    ColorPalette.ROSE_PINK -> buildDarkTheme(
        primary = Color(0xFFF472B6),
        onPrimary = Color(0xFF500724),
        primaryContainer = Color(0xFF9D174D),
        onPrimaryContainer = Color(0xFFFCE7F3),
        secondary = Color(0xFFFB7185),
        secondaryContainer = Color(0xFF881337),
        onSecondaryContainer = Color(0xFFFFE4E6),
        tertiary = Color(0xFFC4B5FD),
        tertiaryContainer = Color(0xFF5B21B6),
        onTertiaryContainer = Color(0xFFEDE9FE),
        isAmoled = isAmoled
    )
    ColorPalette.MIDNIGHT -> buildDarkTheme(
        primary = Color(0xFF93C5FD),
        onPrimary = Color(0xFF172554),
        primaryContainer = Color(0xFF1E3A8A),
        onPrimaryContainer = Color(0xFFDBEAFE),
        secondary = Color(0xFFCBD5E1),
        secondaryContainer = Color(0xFF334155),
        onSecondaryContainer = Color(0xFFF1F5F9),
        tertiary = Color(0xFF38BDF8),
        tertiaryContainer = Color(0xFF075985),
        onTertiaryContainer = Color(0xFFE0F2FE),
        isAmoled = isAmoled
    )
    ColorPalette.TEAL -> buildDarkTheme(
        primary = Color(0xFF5EEAD4),
        onPrimary = Color(0xFF042F2E),
        primaryContainer = Color(0xFF115E59),
        onPrimaryContainer = Color(0xFFCCFBF1),
        secondary = Color(0xFF67E8F9),
        secondaryContainer = Color(0xFF155E75),
        onSecondaryContainer = Color(0xFFCFFAFE),
        tertiary = Color(0xFF6EE7B7),
        tertiaryContainer = Color(0xFF065F46),
        onTertiaryContainer = Color(0xFFD1FAE5),
        isAmoled = isAmoled
    )
    ColorPalette.MONOCHROME -> buildDarkTheme(
        primary = Color(0xFFF4F4F5),
        onPrimary = Color(0xFF09090B),
        primaryContainer = Color(0xFF27272A),
        onPrimaryContainer = Color(0xFFF4F4F5),
        secondary = Color(0xFFD4D4D8),
        secondaryContainer = Color(0xFF3F3F46),
        onSecondaryContainer = Color(0xFFFAFAFA),
        tertiary = Color(0xFFA1A1AA),
        tertiaryContainer = Color(0xFF3F3F46),
        onTertiaryContainer = Color(0xFFF4F4F5),
        isAmoled = isAmoled
    )
    ColorPalette.FOREST_GREEN -> buildDarkTheme(
        primary = Color(0xFF86EFAC),
        onPrimary = Color(0xFF052E16),
        primaryContainer = Color(0xFF166534),
        onPrimaryContainer = Color(0xFFDCFCE7),
        secondary = Color(0xFFBEF264),
        secondaryContainer = Color(0xFF3F6212),
        onSecondaryContainer = Color(0xFFECFCCB),
        tertiary = Color(0xFF5EEAD4),
        tertiaryContainer = Color(0xFF115E59),
        onTertiaryContainer = Color(0xFFCCFBF1),
        isAmoled = isAmoled
    )
    ColorPalette.CRIMSON_RED -> buildDarkTheme(
        primary = Color(0xFFFCA5A5),
        onPrimary = Color(0xFF450A0A),
        primaryContainer = Color(0xFF991B1B),
        onPrimaryContainer = Color(0xFFFEE2E2),
        secondary = Color(0xFFFDA4AF),
        secondaryContainer = Color(0xFF9F1239),
        onSecondaryContainer = Color(0xFFFFE4E6),
        tertiary = Color(0xFFFCD34D),
        tertiaryContainer = Color(0xFF92400E),
        onTertiaryContainer = Color(0xFFFEF3C7),
        isAmoled = isAmoled
    )
}
