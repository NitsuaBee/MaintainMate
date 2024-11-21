package com.example.maintainmate

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.maintainmate.api.ChatRequest
import com.example.maintainmate.api.ChatResponse
import com.example.maintainmate.api.GPTApi
import com.example.maintainmate.api.Message
import com.example.maintainmate.utils.ReminderManager
import com.example.maintainmate.utils.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class MaintenanceDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maintenance_details)

        val vehicleBrand = intent.getStringExtra("VEHICLE_BRAND")
        val vehicleModel = intent.getStringExtra("VEHICLE_MODEL")
        val vehicleYear = intent.getStringExtra("VEHICLE_YEAR")
        val vehicleMileage = intent.getIntExtra("VEHICLE_MILEAGE", 0)

        val maintenanceTitle = findViewById<TextView>(R.id.maintenanceTitle)
        val detailsTextView = findViewById<TextView>(R.id.maintenanceDetails)
        val setReminderButton = findViewById<Button>(R.id.setReminderButton)

        maintenanceTitle.text = "Maintenance Info: $vehicleBrand $vehicleModel ($vehicleYear)"
        detailsTextView.text = "Fetching maintenance information..."

        // Fetch and display AI-informed maintenance info
        fetchMaintenanceInfo(vehicleBrand, vehicleModel, vehicleYear, vehicleMileage, detailsTextView)

        // Set Reminder Button Click Listener
        setReminderButton.setOnClickListener {
            showReminder(vehicleBrand, vehicleModel)
        }
    }

    private fun fetchMaintenanceInfo(
        brand: String?,
        model: String?,
        year: String?,
        mileage: Int,
        detailsTextView: TextView
    ) {
        val api = RetrofitClient.instance.create(GPTApi::class.java)

        val prompt = """
            Provide detailed maintenance information for a $brand $model ($year) with $mileage miles.
        """.trimIndent()

        val request = ChatRequest(
            messages = listOf(Message(role = "user", content = prompt)),
            model = "gpt-4o",
            max_tokens = 300,
            temperature = 0.7
        )

        api.getChatResponse(request).enqueue(object : Callback<ChatResponse> {
            override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
                if (response.isSuccessful) {
                    val maintenanceInfo = response.body()?.choices?.firstOrNull()?.message?.content
                    if (maintenanceInfo != null) {
                        detailsTextView.text = maintenanceInfo
                    } else {
                        detailsTextView.text = "No maintenance information available."
                        Log.e("MaintenanceDetails", "Empty response body.")
                    }
                } else {
                    detailsTextView.text = "Error fetching maintenance information."
                    Log.e("MaintenanceDetails", "API Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                detailsTextView.text = "Failed to fetch maintenance information."
                Log.e("MaintenanceDetails", "API Failure: ${t.message}")
            }
        })
    }

    private fun showReminder(vehicleBrand: String?, vehicleModel: String?) {
        val calendar = Calendar.getInstance()

        // DatePickerDialog to choose the reminder date
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                // TimePickerDialog to choose the reminder time
                TimePickerDialog(
                    this,
                    { _, hourOfDay, minute ->
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        calendar.set(Calendar.MINUTE, minute)

                        // Use ReminderManager to set the reminder
                        val reminderManager = ReminderManager(this)
                        reminderManager.setReminder(calendar, vehicleBrand, vehicleModel)

                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                ).show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

}
