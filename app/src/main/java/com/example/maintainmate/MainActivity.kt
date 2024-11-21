package com.example.maintainmate

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase services
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Check if the user is logged in
        val currentUser = auth.currentUser
        if (currentUser == null) {
            redirectToLogin()
            return
        }

        val userId = currentUser.uid
        logUserData(userId, "app_launched")

        // Initialize buttons for navigation
        val buttonOpenAddVehicle = findViewById<Button>(R.id.buttonOpenAddVehicle)
        val buttonViewVehicles = findViewById<Button>(R.id.buttonViewVehicles)

        buttonOpenAddVehicle.setOnClickListener {
            logUserData(userId, "navigate_add_vehicle")
            navigateToAddVehicle()
        }

        buttonViewVehicles.setOnClickListener {
            logUserData(userId, "navigate_view_vehicles")
            navigateToViewVehicles()
        }

        val buttonMaintenance = findViewById<Button>(R.id.buttonMaintenance)
        buttonMaintenance.setOnClickListener {
            val intent = Intent(this, MaintenanceActivity::class.java)
            startActivity(intent)
        }

        val viewRemindersButton = findViewById<Button>(R.id.viewRemindersButton)
        viewRemindersButton.setOnClickListener {
            val intent = Intent(this, RemindersActivity::class.java)
            startActivity(intent)
        }


    }

    private fun logUserData(userId: String, eventType: String) {
        val eventData = hashMapOf(
            "eventType" to eventType,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("Users").document(userId).collection("events")
            .add(eventData)
            .addOnSuccessListener {
                Log.d("Firestore", "Event logged successfully: $eventType")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error logging event: $eventType", e)
            }
    }

    private fun navigateToAddVehicle() {
        val intent = Intent(this, AddVehicleActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToViewVehicles() {
        val intent = Intent(this, ViewVehiclesActivity::class.java)
        startActivity(intent)
    }

    private fun redirectToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
