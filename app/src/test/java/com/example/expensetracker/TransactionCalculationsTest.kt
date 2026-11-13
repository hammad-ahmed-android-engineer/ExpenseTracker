package com.example.expensetracker

import com.example.expensetracker.core.common.formatCurrencyWithSymbol
import com.example.expensetracker.core.common.toDecimalString
import com.example.expensetracker.core.common.toMinorUnits
import org.junit.Assert.assertEquals
import org.junit.Test

class TransactionCalculationsTest {

    @Test
    fun testMinorUnitsConversion() {
        val minor = "12.34".toMinorUnits()
        assertEquals(1234L, minor)

        val decimal = 1234L.toDecimalString()
        assertEquals("12.34", decimal)
    }

    @Test
    fun testZeroAndInvalidAmountConversions() {
        val minorZero = "0".toMinorUnits()
        assertEquals(0L, minorZero)

        val minorEmpty = "".toMinorUnits()
        assertEquals(0L, minorEmpty)
    }

    @Test
    fun testCurrencyFormatting() {
        val formattedUsd = 1234L.formatCurrencyWithSymbol("USD")
        assertEquals("$12.34", formattedUsd)

        val formattedEur = 5000L.formatCurrencyWithSymbol("EUR")
        assertEquals("€50.00", formattedEur)
    }
}
