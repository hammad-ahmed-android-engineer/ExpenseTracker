package com.example.expensetracker.data.local.dao

import androidx.room.*
import com.example.expensetracker.data.local.entity.ExchangeRateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExchangeRateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExchangeRate(rate: ExchangeRateEntity): Long

    @Query("SELECT * FROM exchange_rates WHERE fromCurrency = :from AND toCurrency = :to")
    suspend fun getExchangeRate(from: String, to: String): ExchangeRateEntity?

    @Query("SELECT * FROM exchange_rates")
    fun getAllExchangeRatesFlow(): Flow<List<ExchangeRateEntity>>
}