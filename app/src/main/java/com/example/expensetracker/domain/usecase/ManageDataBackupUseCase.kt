package com.example.expensetracker.domain.usecase

import com.example.expensetracker.domain.model.backup.BackupPreview
import com.example.expensetracker.domain.model.backup.BackupRestoreMode
import com.example.expensetracker.domain.repository.BackupRepository
import com.example.expensetracker.domain.repository.FileStorageRepository
import javax.inject.Inject

class ManageDataBackupUseCase @Inject constructor(
    private val backupRepository: BackupRepository,
    private val fileStorageRepository: FileStorageRepository
) {
    suspend fun createBackupJson(): String = backupRepository.createBackupJson()

    suspend fun exportTransactionsCsv(): String = backupRepository.exportTransactionsCsv()

    suspend fun exportTransactionsJson(): String = backupRepository.exportTransactionsJson()

    suspend fun importCsvFromUri(uriString: String): Result<Int> = runCatching {
        val csv = fileStorageRepository.readTextFromUri(uriString).getOrThrow()
        backupRepository.importTransactionsCsv(csv).getOrThrow()
    }

    suspend fun importTransactionsCsv(csv: String): Result<Int> =
        backupRepository.importTransactionsCsv(csv)

    suspend fun exportCsvToUri(uriString: String): Result<Unit> = runCatching {
        val csv = backupRepository.exportTransactionsCsv()
        fileStorageRepository.writeTextToUri(uriString, csv).getOrThrow()
    }

    suspend fun exportJsonToUri(uriString: String): Result<Unit> = runCatching {
        val json = backupRepository.exportTransactionsJson()
        fileStorageRepository.writeTextToUri(uriString, json).getOrThrow()
    }

    suspend fun createBackupToUri(uriString: String): Result<Unit> = runCatching {
        val backup = backupRepository.createBackupJson()
        fileStorageRepository.writeTextToUri(uriString, backup).getOrThrow()
    }

    suspend fun inspectBackupFromUri(uriString: String): Result<Pair<String, BackupPreview>> = runCatching {
        val json = fileStorageRepository.readTextFromUri(uriString).getOrThrow()
        val preview = backupRepository.inspectBackup(json).getOrThrow()
        json to preview
    }

    suspend fun inspectBackup(json: String): Result<BackupPreview> = backupRepository.inspectBackup(json)

    suspend fun restoreBackup(json: String, mode: BackupRestoreMode): Result<BackupPreview> =
        backupRepository.restoreBackup(json, mode)

    suspend fun seedSampleData() = backupRepository.seedSampleData()

    suspend fun clearAllUserData() = backupRepository.clearAllUserData()
}
