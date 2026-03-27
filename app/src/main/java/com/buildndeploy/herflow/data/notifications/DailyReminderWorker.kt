package com.buildndeploy.herflow.data.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters

class DailyReminderWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val reminderType = inputData.getString(KEY_REMINDER_TYPE) ?: ReminderType.PILL.name
        // TODO: hook up NotificationManager + channels for production notifications.
        return if (reminderType.isNotBlank()) Result.success() else Result.retry()
    }

    companion object {
        const val KEY_REMINDER_TYPE = "key_reminder_type"

        fun inputData(type: ReminderType): Data =
            Data.Builder()
                .putString(KEY_REMINDER_TYPE, type.name)
                .build()
    }
}
