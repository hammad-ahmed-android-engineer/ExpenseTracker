package com.example.expensetracker.data.local.dao

import androidx.room.*
import com.example.expensetracker.data.local.entity.AccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: AccountEntity): Long

    @Update
    suspend fun updateAccount(account: AccountEntity): Int

    @Delete
    suspend fun deleteAccount(account: AccountEntity): Int

    @Query("SELECT * FROM accounts WHERE id = :id")
    suspend fun getAccountById(id: Long): AccountEntity?

    @Query("SELECT * FROM accounts WHERE isArchived = 0 ORDER BY name ASC")
    fun getAllActiveAccountsFlow(): Flow<List<AccountEntity>>

    @Query("SELECT * FROM accounts ORDER BY isArchived ASC, name ASC")
    fun getAllAccountsFlow(): Flow<List<AccountEntity>>

    @Query("SELECT COALESCE(SUM(currentBalance), 0) FROM accounts WHERE isArchived = 0")
    fun getTotalBalanceFlow(): Flow<Long>

    @Query("UPDATE accounts SET currentBalance = currentBalance + :delta WHERE id = :accountId")
    suspend fun updateAccountBalance(accountId: Long, delta: Long): Int
}