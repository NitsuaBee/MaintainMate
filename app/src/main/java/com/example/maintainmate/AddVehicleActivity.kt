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
        val vehicleList = mutableListOf<Vehicle>()
        const val TAG = "AddVehicleActivity" // Tag for logging
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vehicle)

        // Check if we're editing an existing vehicle
        val editIndex = intent.getIntExtra("EDIT_VEHICLE", -1)
        var editingVehicle: Vehicle? = null

        // Get references to the input fields and button
        val editTextVehicleBrand = findViewById<EditText>(R.id.editTextVehicleBrand)
        val editTextVehicleModel = findViewById<EditText>(R.id.editTextVehicleModel)
        val editTextVehicleYear = findViewById<EditText>(R.id.editTextVehicleYear)
        val editTextVin = findViewById<EditText>(R.id.editTextVin)
        val buttonAddVehicle = findViewById<Button>(R.id.buttonAddVehicle)

        // Check if this is an edit action
        if (editIndex != -1) {
            // Pre-fill the fields with the selected vehicle's data
            editingVehicle = vehicleList[editIndex]
            editTextVehicleBrand.setText(editingVehicle.brand)
            editTextVehicleModel.setText(editingVehicle.model)
            editTextVehicleYear.setText(editingVehicle.year)
            editTextVin.setText(editingVehicle.vin ?: "")
            buttonAddVehicle.text = "Update Vehicle" // Change button text for editing
        }

        // Log when the activity is created
        Log.d(TAG, "Activity created")

        val buttonBack = findViewById<Button>(R.id.buttonBack)
        buttonBack.setOnClickListener {
            finish() // This will close the current activity and go back to the previous one
        }

        // Set click listener for the Add Vehicle / Update Vehicle button
        buttonAddVehicle.setOnClickListener {
            val vehicleBrand = editTextVehicleBrand.text.toString()
            val vehicleModel = editTextVehicleModel.text.toString()
            val vehicleYear = editTextVehicleYear.text.toString()
            val vin = editTextVin.text.toString()  // Optional field

            // Log button click event and vehicle details
            Log.d(TAG, "Add/Update Vehicle button clicked")
            Log.d(TAG, "Vehicle details - Brand: $vehicleBrand, Model: $vehicleModel, Year: $vehicleYear, VIN: $vin")

            if (vehicleBrand.isNotEmpty() && vehicleModel.isNotEmpty() && vehicleYear.isNotEmpty()) {
                if (editIndex == -1) {
                    // Add a new vehicle to the list
                    val vehicle = Vehicle(vehicleBrand, vehicleModel, vehicleYear, if (vin.isNotEmpty()) vin else null)
                    vehicleList.add(vehicle)
                    Log.d(TAG, "Vehicle added to the list: $vehicle")
                } else {
                    // Update the existing vehicle
                    editingVehicle = editingVehicle?.copy(
                        brand = vehicleBrand,
                        model = vehicleModel,
                        year = vehicleYear,
                        vin = if (vin.isNotEmpty()) vin else null
                    )
                    vehicleList[editIndex] = editingVehicle!!
                    Log.d(TAG, "Vehicle updated at index $editIndex: $editingVehicle")
                }

                Toast.makeText(
                    this,
                    "Vehicle Added/Updated: $vehicleBrand $vehicleModel ($vehicleYear), VIN: ${vin.ifEmpty { "Not Provided" }}",
                    Toast.LENGTH_LONG
                ).show()

                // Clear the input fields after adding
                editTextVehicleBrand.text.clear()
                editTextVehicleModel.text.clear()
                editTextVehicleYear.text.clear()
                editTextVin.text.clear()

                finish() // Go back after adding/updating the vehicle
            } else {
                // Show error message if required fields are empty
                Log.d(TAG, "Fields are missing - Brand: $vehicleBrand, Model: $vehicleModel, Year: $vehicleYear")
                Toast.makeText(this, "Please fill out all required fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
