package com.example.expensetracker.core.common

import java.math.BigDecimal
import java.math.RoundingMode

fun String.toMinorUnits(decimalPlaces: Int = 2): Long {
    val cleanInput = this.trim().replace(",", ".")
    if (cleanInput.isEmpty()) return 0L
    return try {
        val bd = BigDecimal(cleanInput)
        val scaled = bd.setScale(decimalPlaces, RoundingMode.HALF_UP)
        scaled.movePointRight(decimalPlaces).toLong()
    } catch (_: Exception) {
        0L
    }
}

fun Long.toMajorUnitsBigDecimal(decimalPlaces: Int = 2): BigDecimal =
    BigDecimal(this).movePointLeft(decimalPlaces).setScale(decimalPlaces, RoundingMode.HALF_UP)

fun Long.toDecimalString(decimalPlaces: Int = 2): String =
    this.toMajorUnitsBigDecimal(decimalPlaces).toPlainString()
