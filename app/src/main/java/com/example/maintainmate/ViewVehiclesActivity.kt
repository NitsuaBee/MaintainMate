package com.example.maintainmate

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ViewVehiclesActivity : AppCompatActivity() {

    companion object {
        const val TAG = "ViewVehiclesActivity" // Tag for logging
    }

    private lateinit var vehicleAdapter: VehicleAdapter // Declare vehicleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_vehicles)

        Log.d(TAG, "onCreate: View Vehicles Activity started")

        val noVehiclesText = findViewById<TextView>(R.id.noVehiclesText)
        val vehicleListView = findViewById<ListView>(R.id.vehicleListView)
        val backButton = findViewById<Button>(R.id.backButton)

        // Set up back button click listener
        backButton.setOnClickListener {
            Log.d(TAG, "Back button clicked")
            finish() // Close the current activity and return to the previous one
        }

        // Initialize the adapter for the ListView
        vehicleAdapter = VehicleAdapter(this, AddVehicleActivity.vehicleList)
        vehicleListView.adapter = vehicleAdapter

        // Check if the vehicle list is empty
        if (AddVehicleActivity.vehicleList.isNotEmpty()) {
            noVehiclesText.visibility = TextView.GONE
        } else {
            noVehiclesText.visibility = TextView.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: Refreshing vehicle list")

        // Refresh the vehicle list in onResume
        vehicleAdapter.notifyDataSetChanged()

        val noVehiclesText = findViewById<TextView>(R.id.noVehiclesText)

        // Check if the vehicle list is empty after editing
        if (AddVehicleActivity.vehicleList.isNotEmpty()) {
            noVehiclesText.visibility = TextView.GONE
        } else {
            noVehiclesText.visibility = TextView.VISIBLE
        }
    }
}
