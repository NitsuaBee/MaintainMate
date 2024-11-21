package com.example.maintainmate.utils

import android.content.Context
import android.content.SharedPreferences
import java.util.Calendar

class ReminderManager(private val context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("ReminderPrefs", Context.MODE_PRIVATE)

    fun setReminder(calendar: Calendar, vehicleBrand: String?, vehicleModel: String?) {
        // Store reminder in SharedPreferences
        val editor = sharedPreferences.edit()
        val reminderKey = "reminder_${System.currentTimeMillis()}"
        val reminderValue = "$vehicleBrand $vehicleModel at ${calendar.time}"
        editor.putString(reminderKey, reminderValue)
        editor.apply()

        // Set the alarm (as previously implemented)
        // ...
    }

    fun getAllReminders(): List<String> {
        val reminders = mutableListOf<String>()
        sharedPreferences.all.forEach { (_, value) ->
            if (value is String) {
                reminders.add(value)
            }
        }
        return reminders
    }
}
