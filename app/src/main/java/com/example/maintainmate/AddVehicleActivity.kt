package com.example.maintainmate

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddVehicleActivity : AppCompatActivity() {

    companion object {
        // Store vehicles in an in-memory list
        val `vehicleList` = mutableListOf<`Vehicle`>()
        const val TAG = "AddVehicleActivity" // Tag for logging
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vehicle)

        // Log when the activity is created
        Log.d(TAG, "Activity created")

        // Get references to the input fields and button
        val editTextVehicleBrand = findViewById<EditText>(R.id.editTextVehicleBrand)
        val editTextVehicleModel = findViewById<EditText>(R.id.editTextVehicleModel)
        val editTextVehicleYear = findViewById<EditText>(R.id.editTextVehicleYear)
        val editTextVin = findViewById<EditText>(R.id.editTextVin)
        val buttonAddVehicle = findViewById<Button>(R.id.buttonAddVehicle)

        // Set click listener for the Add Vehicle button
        buttonAddVehicle.setOnClickListener {
            val vehicleBrand = editTextVehicleBrand.text.toString()
            val vehicleModel = editTextVehicleModel.text.toString()
            val vehicleYear = editTextVehicleYear.text.toString()
            val vin = editTextVin.text.toString()  // Optional field

            // Log button click event and vehicle details
            Log.d(TAG, "Add Vehicle button clicked")
            Log.d(TAG, "Vehicle details - Brand: $vehicleBrand, Model: $vehicleModel, Year: $vehicleYear, VIN: $vin")

            if (vehicleBrand.isNotEmpty() && vehicleModel.isNotEmpty() && vehicleYear.isNotEmpty()) {
                // Add vehicle to the list
                val `vehicle` = `Vehicle`(vehicleBrand, vehicleModel, vehicleYear, if (vin.isNotEmpty()) vin else null)
                `vehicleList`.add(`vehicle`)

                Log.d(TAG, "Vehicle added to the list: $`vehicle`")

                Toast.makeText(
                    this,
                    "Vehicle Added: $vehicleBrand $vehicleModel ($vehicleYear), VIN: ${vin.ifEmpty { "Not Provided" }}",
                    Toast.LENGTH_LONG
                ).show()

                // Clear the input fields after adding
                editTextVehicleBrand.text.clear()
                editTextVehicleModel.text.clear()
                editTextVehicleYear.text.clear()
                editTextVin.text.clear()
            } else {
                // Show error message if required fields are empty
                Log.d(TAG, "Fields are missing - Brand: $vehicleBrand, Model: $vehicleModel, Year: $vehicleYear")
                Toast.makeText(this, "Please fill out all required fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
