package com.example.maintainmate

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MaintenanceActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var vehicleAdapter: VehicleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maintenance)

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Set up RecyclerView
        recyclerView = findViewById(R.id.maintenanceRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize adapter
        vehicleAdapter = VehicleAdapter(this, mutableListOf(), isMaintenanceMode = true) { vehicle ->
            navigateToMaintenanceDetails(vehicle)
        }
        recyclerView.adapter = vehicleAdapter

        // Fetch vehicles
        fetchVehicles()
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
    }

    private fun navigateToMaintenanceDetails(vehicle: Vehicle) {
        val intent = Intent(this, MaintenanceDetailsActivity::class.java).apply {
            putExtra("VEHICLE_BRAND", vehicle.brand)
            putExtra("VEHICLE_MODEL", vehicle.model)
            putExtra("VEHICLE_YEAR", vehicle.year)
            putExtra("VEHICLE_MILEAGE", vehicle.mileage)
        }
        startActivity(intent)
    }
}
