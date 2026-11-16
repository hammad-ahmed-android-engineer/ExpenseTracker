package com.example.expensetracker.data.repository

import android.content.Context
import android.net.Uri
import com.example.expensetracker.domain.repository.FileStorageRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileStorageRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : FileStorageRepository {

    override suspend fun writeTextToUri(uriString: String, content: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching {
                val uri = Uri.parse(uriString)
                val outputStream = context.contentResolver.openOutputStream(uri)
                    ?: error("Unable to open output stream for selected file")
                outputStream.use { out ->
                    out.write(content.toByteArray())
                }
            }
        }

    override suspend fun readTextFromUri(uriString: String): Result<String> =
        withContext(Dispatchers.IO) {
            runCatching {
                val uri = Uri.parse(uriString)
                val inputStream = context.contentResolver.openInputStream(uri)
                    ?: error("Could not read selected file")
                inputStream.bufferedReader().use { it.readText() }
            }
        }
}

