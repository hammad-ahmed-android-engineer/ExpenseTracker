package com.example.expensetracker.core.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.expensetracker.domain.repository.RecurringTransactionRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

class RecurringTransactionWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface WorkerEntryPoint {
        fun recurringRepository(): RecurringTransactionRepository
        fun notificationHelper(): NotificationHelper
    }

    override suspend fun doWork(): Result {
        return try {
            val entryPoint = EntryPointAccessors.fromApplication(
                applicationContext,
                WorkerEntryPoint::class.java
            )
            val count = entryPoint.recurringRepository().processDueRecurringTransactions()
            if (count > 0) {
                entryPoint.notificationHelper().showNotification(
                    title = "Recurring Transactions Processed",
                    message = "$count scheduled recurring transactions were generated automatically."
                )
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}