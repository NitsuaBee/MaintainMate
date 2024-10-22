package com.example.maintainmate

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotificationWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        // Trigger the notification here when the scheduled time arrives
        showNotification(applicationContext, "Maintenance Reminder", "Your car maintenance is due.")
        return Result.success()
    }
}
