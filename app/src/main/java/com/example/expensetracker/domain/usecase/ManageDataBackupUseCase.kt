package com.example.expensetracker.domain.usecase

import com.example.expensetracker.domain.model.BackupPreview
import com.example.expensetracker.domain.model.BackupRestoreMode
import com.example.expensetracker.domain.repository.BackupRepository
import javax.inject.Inject

class ManageDataBackupUseCase @Inject constructor(
    private val backupRepository: BackupRepository
) {
    suspend fun createBackupJson(): String = backupRepository.createBackupJson()

    suspend fun exportTransactionsCsv(): String = backupRepository.exportTransactionsCsv()

    suspend fun exportTransactionsJson(): String = backupRepository.exportTransactionsJson()

    suspend fun inspectBackup(json: String): Result<BackupPreview> = backupRepository.inspectBackup(json)

    suspend fun restoreBackup(json: String, mode: BackupRestoreMode): Result<BackupPreview> =
        backupRepository.restoreBackup(json, mode)

    suspend fun seedSampleData() = backupRepository.seedSampleData()

    suspend fun clearAllUserData() = backupRepository.clearAllUserData()
}

