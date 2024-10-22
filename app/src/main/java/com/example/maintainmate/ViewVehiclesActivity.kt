package com.example.maintainmate

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ViewVehiclesActivity : AppCompatActivity() {

    companion object {
        const val TAG = "ViewVehiclesActivity" // Tag for loggingi the vehicle
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_vehicles)

        Log.d(TAG, "onCreate: View Vehicles Activity started")

        val noVehiclesText = findViewById<TextView>(R.id.noVehiclesText)
        val vehicleListView = findViewById<ListView>(R.id.vehicleListView)

        //Log vehicle list status
        Log.d(TAG, "onCreate: Checking if vehicle list is empty")

        if (AddVehicleActivity.vehicleList.isNotEmpty()) {
            noVehiclesText.visibility = TextView.GONE
            Log.d(TAG, "onCreate: Vehicle list contains ${AddVehicleActivity.vehicleList.size} items")

            // Set up the adapter with vehicle list
            val vehicleAdapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                AddVehicleActivity.vehicleList.map { vehicle ->
                    "${vehicle.brand} ${vehicle.model} (${vehicle.year}), VIN: ${vehicle.vin ?: "Not Provided"}"
                }
            )

            vehicleListView.adapter = vehicleAdapter
            Log.d(TAG, "onCreate: Adapter set with vehicle list")

        } else {
            // Log if vehicle list is empty
            Log.d(TAG, "onCreate: No vehicles available")
            noVehiclesText.visibility = TextView.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()

        Log.d(TAG, "onResume: Checking vehicle list in onResume")

        val noVehiclesText = findViewById<TextView>(R.id.noVehiclesText)
        val vehicleListView = findViewById<ListView>(R.id.vehicleListView)

        if (AddVehicleActivity.vehicleList.isNotEmpty()) {
            noVehiclesText.visibility = TextView.GONE

            val vehicleAdapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                AddVehicleActivity.vehicleList.map { vehicle ->
                    "${vehicle.brand} ${vehicle.model} (${vehicle.year}), VIN: ${vehicle.vin ?: "Not Provided"}"
                }
            )

            vehicleListView.adapter = vehicleAdapter
            Log.d(TAG, "onResume: Adapter refreshed with updated vehicle list")
        } else {
            noVehiclesText.visibility = TextView.VISIBLE
            Log.d(TAG, "onResume: No vehicles available to display")
        }
    }
}
