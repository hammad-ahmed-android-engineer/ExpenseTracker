package com.example.expensetracker.core.common

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale
import kotlin.math.abs

enum class SupportedCurrency(
    val code: String,
    val symbol: String,
    val currencyName: String,
    val decimalPlaces: Int = 2
) {
    USD("USD", "$", "US Dollar"),
    EUR("EUR", "€", "Euro"),
    GBP("GBP", "£", "British Pound"),
    INR("INR", "₹", "Indian Rupee"),
    PKR("PKR", "Rs", "Pakistani Rupee"),
    SAR("SAR", "SR", "Saudi Riyal"),
    AED("AED", "AED", "UAE Dirham"),
    CAD("CAD", "CA$", "Canadian Dollar"),
    AUD("AUD", "A$", "Australian Dollar"),
    JPY("JPY", "¥", "Japanese Yen", 0);

    companion object {
        fun fromCode(code: String): SupportedCurrency =
            entries.find { it.code.equals(code, ignoreCase = true) } ?: USD
    }
}

fun Long.formatCurrency(
    currencyCode: String = "USD",
    locale: Locale = Locale.getDefault()
): String {
    val currency = SupportedCurrency.fromCode(currencyCode)
    val majorUnits = this.toMajorUnitsBigDecimal(currency.decimalPlaces).toDouble()

    return try {
        val format = NumberFormat.getCurrencyInstance(locale)
        format.currency = Currency.getInstance(currency.code)
        format.maximumFractionDigits = currency.decimalPlaces
        format.minimumFractionDigits = currency.decimalPlaces
        format.format(majorUnits)
    } catch (_: Exception) {
        "${currency.symbol}${this.toDecimalString(currency.decimalPlaces)}"
    }
}

fun Long.formatCurrencyWithSymbol(currencyCode: String = "USD"): String {
    val currency = SupportedCurrency.fromCode(currencyCode)
    return "${currency.symbol}${this.toDecimalString(currency.decimalPlaces)}"
}

fun Long.formatCurrencyCompact(currencyCode: String = "USD"): String {
    val currency = SupportedCurrency.fromCode(currencyCode)
    val major = this.toMajorUnitsBigDecimal(currency.decimalPlaces).toDouble()
    return when {
        abs(major) >= 1_000_000 -> "${currency.symbol}%.1fM".format(Locale.US, major / 1_000_000)
        abs(major) >= 1_000 -> "${currency.symbol}%.1fk".format(Locale.US, major / 1_000)
        else -> this.formatCurrency(currencyCode)
    }
}
