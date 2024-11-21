package com.example.maintainmate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.maintainmate.utils.ReminderManager

class RemindersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminders)

        val recyclerView = findViewById<RecyclerView>(R.id.remindersRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Assuming ReminderManager has a method to get all reminders.
        val remindersList = ReminderManager(this).getAllReminders()

        // Set up RecyclerView with ReminderAdapter (you need to create ReminderAdapter)
        val adapter = ReminderAdapter(remindersList)
        recyclerView.adapter = adapter
    }
}
