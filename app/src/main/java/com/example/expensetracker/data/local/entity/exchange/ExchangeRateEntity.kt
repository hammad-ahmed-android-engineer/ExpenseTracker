package com.example.expensetracker.data.local.entity.exchange

import androidx.room.Entity

@Entity(
    tableName = "exchange_rates",
    primaryKeys = ["fromCurrency", "toCurrency"]
)
data class ExchangeRateEntity(
    val fromCurrency: String,
    val toCurrency: String,
    val rate: Double,
    val updatedAt: Long = System.currentTimeMillis()
)
