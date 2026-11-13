package com.example.expensetracker.core.utils

import androidx.compose.ui.graphics.Color

fun String.toComposeColor(fallback: Color = Color(0xFF10B981)): Color =
    try {
        Color(android.graphics.Color.parseColor(this))
    } catch (_: Exception) {
        fallback
    }

