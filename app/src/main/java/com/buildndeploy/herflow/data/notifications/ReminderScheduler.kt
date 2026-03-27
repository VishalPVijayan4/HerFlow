package com.buildndeploy.herflow.data.notifications

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

object ReminderScheduler {

    fun scheduleDailyReminder(
        context: Context,
        reminderType: ReminderType,
        hourOfDay: Int,
        minute: Int
    ) {
        val initialDelay = computeInitialDelay(hourOfDay, minute)
        val request = PeriodicWorkRequestBuilder<DailyReminderWorker>(1, TimeUnit.DAYS)
            .setInputData(DailyReminderWorker.inputData(reminderType))
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(false)
                    .build()
            )
            .setInitialDelay(initialDelay)
            .addTag(reminderType.name)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            uniqueName(reminderType),
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    fun cancelReminder(context: Context, reminderType: ReminderType) {
        WorkManager.getInstance(context).cancelUniqueWork(uniqueName(reminderType))
    }

    private fun uniqueName(type: ReminderType): String = "daily_reminder_${type.name.lowercase()}"

    private fun computeInitialDelay(hourOfDay: Int, minute: Int): Duration {
        val now = LocalDateTime.now()
        var next = now.withHour(hourOfDay).withMinute(minute).withSecond(0).withNano(0)
        if (!next.isAfter(now)) {
            next = next.plusDays(1)
        }
        return Duration.between(now, next)
    }
}
