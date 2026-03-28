package com.buildndeploy.herflow.data.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootRescheduleReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Keep defaults deterministic; can be replaced by user preferences.
            ReminderScheduler.scheduleDailyReminder(context, ReminderType.PILL, hourOfDay = 8, minute = 0)
            ReminderScheduler.scheduleDailyReminder(context, ReminderType.OVULATION, hourOfDay = 9, minute = 0)
            ReminderScheduler.scheduleDailyReminder(context, ReminderType.PMS, hourOfDay = 10, minute = 0)
        }
    }
}
