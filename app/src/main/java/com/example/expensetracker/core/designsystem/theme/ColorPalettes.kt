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
    val bg = if (isAmoled) Color.Black else Color(0xFF111319)
    val surf = if (isAmoled) Color(0xFF080A0F) else Color(0xFF161922)
    val surfLow = if (isAmoled) Color(0xFF0E1117) else Color(0xFF1C202F)
    val surfMed = if (isAmoled) Color(0xFF131720) else Color(0xFF22273D)
    val surfHigh = if (isAmoled) Color(0xFF1B202C) else Color(0xFF282F48)
    val surfHighest = if (isAmoled) Color(0xFF242A38) else Color(0xFF323B5A)

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
        outline = Color(0xFF475569),
        outlineVariant = Color(0xFF263042),
        error = Color(0xFFF87171),
        onError = Color(0xFF450A0A),
        errorContainer = Color(0xFF7F1D1D),
        onErrorContainer = Color(0xFFFEE2E2),
        previewSwatch = primary
    )
}

fun ColorPalette.getLightColors(): ThemeColors = when (this) {
    ColorPalette.TERRACOTTA -> buildLightTheme(
        primary = Color(0xFFC2410C),
        primaryContainer = Color(0xFFFFEDD5),
        onPrimaryContainer = Color(0xFF431407),
        secondary = Color(0xFFE06D53),
        secondaryContainer = Color(0xFFFFE4DE),
        onSecondaryContainer = Color(0xFF3E1208),
        tertiary = Color(0xFFD97706),
        tertiaryContainer = Color(0xFFFEF3C7),
        onTertiaryContainer = Color(0xFF451A03)
    )
    ColorPalette.AMBER -> buildLightTheme(
        primary = Color(0xFFD97706),
        primaryContainer = Color(0xFFFEF3C7),
        onPrimaryContainer = Color(0xFF451A03),
        secondary = Color(0xFFE68B41),
        secondaryContainer = Color(0xFFFFE8D6),
        onSecondaryContainer = Color(0xFF482004),
        tertiary = Color(0xFFB45309),
        tertiaryContainer = Color(0xFFFDE68A),
        onTertiaryContainer = Color(0xFF381A02)
    )
    ColorPalette.SAND -> buildLightTheme(
        primary = Color(0xFFB48A14),
        primaryContainer = Color(0xFFFEF9C3),
        onPrimaryContainer = Color(0xFF423203),
        secondary = Color(0xFFE5B54E),
        secondaryContainer = Color(0xFFFFF0D0),
        onSecondaryContainer = Color(0xFF3E2C04),
        tertiary = Color(0xFFCA8A04),
        tertiaryContainer = Color(0xFFFEF08A),
        onTertiaryContainer = Color(0xFF3F2B02)
    )
    ColorPalette.MINT -> buildLightTheme(
        primary = Color(0xFF059669),
        primaryContainer = Color(0xFFD1FAE5),
        onPrimaryContainer = Color(0xFF022C22),
        secondary = Color(0xFF8ED6B1),
        secondaryContainer = Color(0xFFE6F7EF),
        onSecondaryContainer = Color(0xFF0E3825),
        tertiary = Color(0xFF0F766E),
        tertiaryContainer = Color(0xFFCCFBF1),
        onTertiaryContainer = Color(0xFF134E4A)
    )
    ColorPalette.SKY_BLUE -> buildLightTheme(
        primary = Color(0xFF0284C7),
        primaryContainer = Color(0xFFE0F2FE),
        onPrimaryContainer = Color(0xFF082F49),
        secondary = Color(0xFF6EA8D9),
        secondaryContainer = Color(0xFFE2EFF9),
        onSecondaryContainer = Color(0xFF0D2D49),
        tertiary = Color(0xFF2563EB),
        tertiaryContainer = Color(0xFFDBEAFE),
        onTertiaryContainer = Color(0xFF1E3A8A)
    )
    ColorPalette.LAVENDER -> buildLightTheme(
        primary = Color(0xFF6366F1),
        primaryContainer = Color(0xFFE0E7FF),
        onPrimaryContainer = Color(0xFF1E1B4B),
        secondary = Color(0xFFA78BFA),
        secondaryContainer = Color(0xFFEDE9FE),
        onSecondaryContainer = Color(0xFF2E1065),
        tertiary = Color(0xFF8B5CF6),
        tertiaryContainer = Color(0xFFF3E8FF),
        onTertiaryContainer = Color(0xFF3B0764)
    )
    ColorPalette.OLIVE -> buildLightTheme(
        primary = Color(0xFF65A30D),
        primaryContainer = Color(0xFFECFCCB),
        onPrimaryContainer = Color(0xFF1A2E05),
        secondary = Color(0xFFA8B150),
        secondaryContainer = Color(0xFFF3F5DC),
        onSecondaryContainer = Color(0xFF2A2E08),
        tertiary = Color(0xFF4D7C0F),
        tertiaryContainer = Color(0xFFD9F99D),
        onTertiaryContainer = Color(0xFF142404)
    )
    ColorPalette.SAGE -> buildLightTheme(
        primary = Color(0xFF0F766E),
        primaryContainer = Color(0xFFCCFBF1),
        onPrimaryContainer = Color(0xFF042F2E),
        secondary = Color(0xFF74A99C),
        secondaryContainer = Color(0xFFE2F0ED),
        onSecondaryContainer = Color(0xFF133730),
        tertiary = Color(0xFF14B8A6),
        tertiaryContainer = Color(0xFF99F6E4),
        onTertiaryContainer = Color(0xFF042F2E)
    )
    ColorPalette.ROSE -> buildLightTheme(
        primary = Color(0xFFBE185D),
        primaryContainer = Color(0xFFFCE7F3),
        onPrimaryContainer = Color(0xFF500724),
        secondary = Color(0xFFD472A5),
        secondaryContainer = Color(0xFFFAE4EF),
        onSecondaryContainer = Color(0xFF44122C),
        tertiary = Color(0xFFDB2777),
        tertiaryContainer = Color(0xFFFBCFE8),
        onTertiaryContainer = Color(0xFF500724)
    )
    ColorPalette.CRIMSON -> buildLightTheme(
        primary = Color(0xFFB91C1C),
        primaryContainer = Color(0xFFFEE2E2),
        onPrimaryContainer = Color(0xFF450A0A),
        secondary = Color(0xFFDC5775),
        secondaryContainer = Color(0xFFFBE4E9),
        onSecondaryContainer = Color(0xFF470C1B),
        tertiary = Color(0xFFE11D48),
        tertiaryContainer = Color(0xFFFFE4E6),
        onTertiaryContainer = Color(0xFF4C0519)
    )
    ColorPalette.SLATE -> buildLightTheme(
        primary = Color(0xFF475569),
        primaryContainer = Color(0xFFE2E8F0),
        onPrimaryContainer = Color(0xFF0F172A),
        secondary = Color(0xFF5C677D),
        secondaryContainer = Color(0xFFE5E7EB),
        onSecondaryContainer = Color(0xFF1A1F2B),
        tertiary = Color(0xFF334155),
        tertiaryContainer = Color(0xFFCBD5E1),
        onTertiaryContainer = Color(0xFF0F172A)
    )
    ColorPalette.CHARCOAL -> buildLightTheme(
        primary = Color(0xFF1E293B),
        primaryContainer = Color(0xFFE2E8F0),
        onPrimaryContainer = Color(0xFF0F172A),
        secondary = Color(0xFF2B2D42),
        secondaryContainer = Color(0xFFDEDFE6),
        onSecondaryContainer = Color(0xFF10111A),
        tertiary = Color(0xFF334155),
        tertiaryContainer = Color(0xFFCBD5E1),
        onTertiaryContainer = Color(0xFF0F172A)
    )
}

fun ColorPalette.getDarkColors(isAmoled: Boolean = false): ThemeColors = when (this) {
    ColorPalette.TERRACOTTA -> buildDarkTheme(
        primary = Color(0xFFE06D53),
        onPrimary = Color(0xFF3E1208),
        primaryContainer = Color(0xFF9E3A24),
        onPrimaryContainer = Color(0xFFFFDBCF),
        secondary = Color(0xFFFB923C),
        secondaryContainer = Color(0xFF7C2D12),
        onSecondaryContainer = Color(0xFFFFEDD5),
        tertiary = Color(0xFFFBBF24),
        tertiaryContainer = Color(0xFF78350F),
        onTertiaryContainer = Color(0xFFFEF3C7),
        isAmoled = isAmoled
    )
    ColorPalette.AMBER -> buildDarkTheme(
        primary = Color(0xFFE68B41),
        onPrimary = Color(0xFF3F1D03),
        primaryContainer = Color(0xFF9A4D13),
        onPrimaryContainer = Color(0xFFFFDDC4),
        secondary = Color(0xFFF59E0B),
        secondaryContainer = Color(0xFF78350F),
        onSecondaryContainer = Color(0xFFFEF3C7),
        tertiary = Color(0xFFFBBF24),
        tertiaryContainer = Color(0xFF713F12),
        onTertiaryContainer = Color(0xFFFEF9C3),
        isAmoled = isAmoled
    )
    ColorPalette.SAND -> buildDarkTheme(
        primary = Color(0xFFE5B54E),
        onPrimary = Color(0xFF3D2E04),
        primaryContainer = Color(0xFF8E6B15),
        onPrimaryContainer = Color(0xFFFFF0CA),
        secondary = Color(0xFFFBBF24),
        secondaryContainer = Color(0xFF78350F),
        onSecondaryContainer = Color(0xFFFEF3C7),
        tertiary = Color(0xFFFACC15),
        tertiaryContainer = Color(0xFF713F12),
        onTertiaryContainer = Color(0xFFFEF9C3),
        isAmoled = isAmoled
    )
    ColorPalette.MINT -> buildDarkTheme(
        primary = Color(0xFF8ED6B1),
        onPrimary = Color(0xFF0A3320),
        primaryContainer = Color(0xFF206E49),
        onPrimaryContainer = Color(0xFFD4F3E4),
        secondary = Color(0xFF34D399),
        secondaryContainer = Color(0xFF065F46),
        onSecondaryContainer = Color(0xFFD1FAE5),
        tertiary = Color(0xFF2DD4BF),
        tertiaryContainer = Color(0xFF115E59),
        onTertiaryContainer = Color(0xFFCCFBF1),
        isAmoled = isAmoled
    )
    ColorPalette.SKY_BLUE -> buildDarkTheme(
        primary = Color(0xFF6EA8D9),
        onPrimary = Color(0xFF082642),
        primaryContainer = Color(0xFF1D5A8C),
        onPrimaryContainer = Color(0xFFD4E7F7),
        secondary = Color(0xFF38BDF8),
        secondaryContainer = Color(0xFF0369A1),
        onSecondaryContainer = Color(0xFFE0F2FE),
        tertiary = Color(0xFF60A5FA),
        tertiaryContainer = Color(0xFF1E40AF),
        onTertiaryContainer = Color(0xFFDBEAFE),
        isAmoled = isAmoled
    )
    ColorPalette.LAVENDER -> buildDarkTheme(
        primary = Color(0xFFA78BFA),
        onPrimary = Color(0xFF1E1045),
        primaryContainer = Color(0xFF4C309C),
        onPrimaryContainer = Color(0xFFEDE9FE),
        secondary = Color(0xFF818CF8),
        secondaryContainer = Color(0xFF2E3272),
        onSecondaryContainer = Color(0xFFE0E7FF),
        tertiary = Color(0xFFC084FC),
        tertiaryContainer = Color(0xFF581C87),
        onTertiaryContainer = Color(0xFFF3E8FF),
        isAmoled = isAmoled
    )
    ColorPalette.OLIVE -> buildDarkTheme(
        primary = Color(0xFFA8B150),
        onPrimary = Color(0xFF232805),
        primaryContainer = Color(0xFF5E651D),
        onPrimaryContainer = Color(0xFFECEFC7),
        secondary = Color(0xFFA3E635),
        secondaryContainer = Color(0xFF3F6212),
        onSecondaryContainer = Color(0xFFECFCCB),
        tertiary = Color(0xFF84CC16),
        tertiaryContainer = Color(0xFF365314),
        onTertiaryContainer = Color(0xFFD9F99D),
        isAmoled = isAmoled
    )
    ColorPalette.SAGE -> buildDarkTheme(
        primary = Color(0xFF74A99C),
        onPrimary = Color(0xFF0F2A24),
        primaryContainer = Color(0xFF2D6155),
        onPrimaryContainer = Color(0xFFD1EAE4),
        secondary = Color(0xFF2DD4BF),
        secondaryContainer = Color(0xFF115E59),
        onSecondaryContainer = Color(0xFFCCFBF1),
        tertiary = Color(0xFF5EEAD4),
        tertiaryContainer = Color(0xFF0F766E),
        onTertiaryContainer = Color(0xFFCCFBF1),
        isAmoled = isAmoled
    )
    ColorPalette.ROSE -> buildDarkTheme(
        primary = Color(0xFFD472A5),
        onPrimary = Color(0xFF3B0C25),
        primaryContainer = Color(0xFF8A2E5F),
        onPrimaryContainer = Color(0xFFF7D9EA),
        secondary = Color(0xFFF472B6),
        secondaryContainer = Color(0xFF831843),
        onSecondaryContainer = Color(0xFFFCE7F3),
        tertiary = Color(0xFFFB7185),
        tertiaryContainer = Color(0xFF881337),
        onTertiaryContainer = Color(0xFFFFE4E6),
        isAmoled = isAmoled
    )
    ColorPalette.CRIMSON -> buildDarkTheme(
        primary = Color(0xFFDC5775),
        onPrimary = Color(0xFF3F0B18),
        primaryContainer = Color(0xFF8E223D),
        onPrimaryContainer = Color(0xFFF9D5DE),
        secondary = Color(0xFFF43F5E),
        secondaryContainer = Color(0xFF881337),
        onSecondaryContainer = Color(0xFFFFE4E6),
        tertiary = Color(0xFFFB7185),
        tertiaryContainer = Color(0xFF9F1239),
        onTertiaryContainer = Color(0xFFFFE4E6),
        isAmoled = isAmoled
    )
    ColorPalette.SLATE -> buildDarkTheme(
        primary = Color(0xFF94A3B8),
        onPrimary = Color(0xFF0F172A),
        primaryContainer = Color(0xFF334155),
        onPrimaryContainer = Color(0xFFF1F5F9),
        secondary = Color(0xFFCBD5E1),
        secondaryContainer = Color(0xFF475569),
        onSecondaryContainer = Color(0xFFF8FAFC),
        tertiary = Color(0xFF64748B),
        tertiaryContainer = Color(0xFF1E293B),
        onTertiaryContainer = Color(0xFFE2E8F0),
        isAmoled = isAmoled
    )
    ColorPalette.CHARCOAL -> buildDarkTheme(
        primary = Color(0xFF818CF8),
        onPrimary = Color(0xFF1E1B4B),
        primaryContainer = Color(0xFF2B2D42),
        onPrimaryContainer = Color(0xFFE0E7FF),
        secondary = Color(0xFF94A3B8),
        secondaryContainer = Color(0xFF1E293B),
        onSecondaryContainer = Color(0xFFF1F5F9),
        tertiary = Color(0xFFA5B4FC),
        tertiaryContainer = Color(0xFF312E81),
        onTertiaryContainer = Color(0xFFEDE9FE),
        isAmoled = isAmoled
    )
}
