package com.example.expensetracker.domain.repository

import com.example.expensetracker.domain.model.backup.BackupPreview
import com.example.expensetracker.domain.model.backup.BackupRestoreMode

interface BackupRepository {
    suspend fun createBackupJson(): String
    suspend fun exportTransactionsJson(): String
    suspend fun exportTransactionsCsv(): String
    suspend fun importTransactionsCsv(csv: String): Result<Int>
    suspend fun inspectBackup(json: String): Result<BackupPreview>
    suspend fun restoreBackup(json: String, mode: BackupRestoreMode): Result<BackupPreview>
    suspend fun seedSampleData()
    suspend fun clearAllUserData()
}
