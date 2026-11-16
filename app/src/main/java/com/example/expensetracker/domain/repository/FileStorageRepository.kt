package com.example.expensetracker.domain.repository

/**
 * Abstraction for reading and writing text content to platform document URIs (e.g. SAF).
 */
interface FileStorageRepository {
    suspend fun writeTextToUri(uriString: String, content: String): Result<Unit>
    suspend fun readTextFromUri(uriString: String): Result<String>
}

