package com.example.maintainmate

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ViewVehiclesActivity : AppCompatActivity() {

    companion object {
        const val TAG = "ViewVehiclesActivity"
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var vehicleAdapter: VehicleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_vehicles)

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recyclerViewVehicles)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize VehicleAdapter for View Vehicles (not maintenance mode)
        vehicleAdapter = VehicleAdapter(
            this,
            mutableListOf(),
            isMaintenanceMode = false,
            onMaintenanceClicked = { vehicle ->
                // Handle maintenance click action here
                Log.d(TAG, "Maintenance clicked for: ${vehicle.brand} ${vehicle.model}")
            }
        )
        recyclerView.adapter = vehicleAdapter

        // Fetch vehicles from Firestore
        fetchVehicles()

        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }
    }

    private fun fetchVehicles() {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("Users").document(userId).collection("vehicles")
            .get()
            .addOnSuccessListener { result ->
                val vehicles = result.map { document ->
                    Vehicle(
                        id = document.id,
                        brand = document.getString("brand") ?: "Unknown Brand",
                        model = document.getString("model") ?: "Unknown Model",
                        year = document.getString("year") ?: "Unknown Year",
                        vin = document.getString("vin"),
                        mileage = document.getLong("mileage")?.toInt() ?: 0
                    )
                }
                vehicleAdapter.updateData(vehicles)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error fetching vehicles", e)
                Toast.makeText(this, "Error fetching vehicles", Toast.LENGTH_SHORT).show()
            }
    }
}
