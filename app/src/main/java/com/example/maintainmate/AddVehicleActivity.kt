package com.example.maintainmate

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddVehicleActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private var vehicleDocumentId: String? = null // Track Firestore document ID for editing

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vehicle)

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val editTextVehicleBrand = findViewById<EditText>(R.id.editTextVehicleBrand)
        val editTextVehicleModel = findViewById<EditText>(R.id.editTextVehicleModel)
        val editTextVehicleYear = findViewById<EditText>(R.id.editTextVehicleYear)
        val editTextVin = findViewById<EditText>(R.id.editTextVin)
        val editTextMileage = findViewById<EditText>(R.id.editTextMileage) // New Mileage Field
        val buttonSaveVehicle = findViewById<Button>(R.id.buttonAddVehicle)
        val buttonBack = findViewById<Button>(R.id.buttonBack)

        // Check if we're editing an existing vehicle
        vehicleDocumentId = intent.getStringExtra("VEHICLE_DOC_ID")
        val isEditing = vehicleDocumentId != null

        if (isEditing) {
            // Pre-fill fields with vehicle data for editing
            editTextVehicleBrand.setText(intent.getStringExtra("BRAND"))
            editTextVehicleModel.setText(intent.getStringExtra("MODEL"))
            editTextVehicleYear.setText(intent.getStringExtra("YEAR"))
            editTextVin.setText(intent.getStringExtra("VIN"))
            editTextMileage.setText(intent.getIntExtra("MILEAGE", 0).toString())

            buttonSaveVehicle.text = "Update Vehicle"
        }

        // Save or update vehicle
        buttonSaveVehicle.setOnClickListener {
            val vehicleBrand = editTextVehicleBrand.text.toString().trim()
            val vehicleModel = editTextVehicleModel.text.toString().trim()
            val vehicleYear = editTextVehicleYear.text.toString().trim()
            val vin = editTextVin.text.toString().trim()
            val mileageText = editTextMileage.text.toString().trim()

            // Validate input
            if (vehicleBrand.isEmpty() || vehicleModel.isEmpty() || vehicleYear.isEmpty() || mileageText.isEmpty()) {
                Toast.makeText(this, "All fields, including mileage, are required.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val mileage = mileageText.toIntOrNull()
            if (mileage == null || mileage <= 0) {
                Toast.makeText(this, "Please enter a valid mileage.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userId = auth.currentUser?.uid
            if (userId != null) {
                val vehicle = mapOf(
                    "brand" to vehicleBrand,
                    "model" to vehicleModel,
                    "year" to vehicleYear,
                    "vin" to if (vin.isNotEmpty()) vin else null,
                    "mileage" to mileage
                )

                if (isEditing) {
                    // Update an existing vehicle
                    vehicleDocumentId?.let { docId ->
                        firestore.collection("Users").document(userId)
                            .collection("vehicles").document(docId)
                            .update(vehicle)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Vehicle updated!", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Failed to update vehicle: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    // Add a new vehicle
                    firestore.collection("Users").document(userId)
                        .collection("vehicles")
                        .add(vehicle)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Vehicle added!", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Failed to add vehicle: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            } else {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            }
        }

        // Back button to return to the previous activity
        buttonBack.setOnClickListener {
            finish()
        }
    }
}
