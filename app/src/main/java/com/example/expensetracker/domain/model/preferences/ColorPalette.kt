package com.example.expensetracker.domain.model.preferences

enum class ColorPalette(val displayName: String, val hexColor: Long) {
    TERRACOTTA("Terracotta", 0xFFE06D53),
    AMBER("Amber", 0xFFE68B41),
    SAND("Sand", 0xFFE5B54E),
    MINT("Mint", 0xFF8ED6B1),
    SKY_BLUE("Sky", 0xFF6EA8D9),
    LAVENDER("Lavender", 0xFFA78BFA),
    OLIVE("Olive", 0xFFA8B150),
    SAGE("Sage", 0xFF74A99C),
    ROSE("Rose", 0xFFD472A5),
    CRIMSON("Crimson", 0xFFDC5775),
    SLATE("Slate", 0xFF5C677D),
    CHARCOAL("Charcoal", 0xFF2B2D42);

    companion object {
        fun fromString(name: String): ColorPalette = runCatching {
            when (name) {
                "OCEAN_BLUE" -> SKY_BLUE
                "EMERALD_GREEN" -> MINT
                "PURPLE_VIOLET" -> LAVENDER
                "SUNSET_ORANGE" -> AMBER
                "ROSE_PINK" -> ROSE
                "MIDNIGHT" -> CHARCOAL
                "TEAL" -> SAGE
                "MONOCHROME" -> SLATE
                "FOREST_GREEN" -> OLIVE
                "CRIMSON_RED" -> CRIMSON
                else -> valueOf(name)
            }
        }.getOrDefault(LAVENDER)
    }
}
