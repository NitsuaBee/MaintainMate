package com.example.maintainmate

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ViewVehiclesActivity : AppCompatActivity() {

    companion object {
        const val TAG = "ViewVehiclesActivity"
    }

    private lateinit var vehicleAdapter: VehicleAdapter
    private val vehicleList = mutableListOf<Vehicle>()
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_vehicles)

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val noVehiclesText = findViewById<TextView>(R.id.noVehiclesText)
        val vehicleListView = findViewById<ListView>(R.id.vehicleListView)
        val backButton = findViewById<Button>(R.id.backButton)

        backButton.setOnClickListener {
            finish()
        }

        // Initialize adapter and set it to the ListView
        vehicleAdapter = VehicleAdapter(this, vehicleList)
        vehicleListView.adapter = vehicleAdapter

        // Listen for changes in the Firestore collection
        listenForVehicleChanges(noVehiclesText)
    }

    private fun listenForVehicleChanges(noVehiclesText: TextView) {
        val userId = auth.currentUser?.uid

        if (userId != null) {
            firestore.collection("Users").document(userId)
                .collection("vehicles")
                .addSnapshotListener { snapshots, e ->
                    if (e != null) {
                        Log.e(TAG, "Listen failed: ${e.message}", e)
                        Toast.makeText(this, "Error listening for changes: ${e.message}", Toast.LENGTH_LONG).show()
                        return@addSnapshotListener
                    }

                    if (snapshots != null && !snapshots.isEmpty) {
                        vehicleList.clear() // Clear the list to avoid duplication
                        for (document in snapshots) {
                            val brand = document.getString("brand") ?: "Unknown Brand"
                            val model = document.getString("model") ?: "Unknown Model"
                            val year = document.getString("year") ?: "Unknown Year"
                            val vin = document.getString("vin") ?: "Not Provided"

                            Log.d(TAG, "Fetched vehicle: $brand $model ($year), VIN: $vin")

                            // Add vehicle to the list
                            vehicleList.add(
                                Vehicle(
                                    id = document.id, // Include the Firestore document ID
                                    brand = brand,
                                    model = model,
                                    year = year,
                                    vin = vin
                                )
                            )
                        }

                        vehicleAdapter.notifyDataSetChanged()

                        // Hide the "No Vehicles" text if there are vehicles
                        noVehiclesText.visibility = if (vehicleList.isEmpty()) TextView.VISIBLE else TextView.GONE
                    } else {
                        Log.d(TAG, "No vehicles found in Firestore")
                        vehicleList.clear()
                        vehicleAdapter.notifyDataSetChanged()
                        noVehiclesText.visibility = TextView.VISIBLE
                    }
                }
        } else {
            Log.e(TAG, "User not logged in")
            Toast.makeText(this, "User not logged in", Toast.LENGTH_LONG).show()
        }
    }
}
