package com.example.maintainmate

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class VehicleAdapter(private val context: Context, private val vehicles: MutableList<Vehicle>) : BaseAdapter() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val userId = auth.currentUser?.uid

    override fun getCount(): Int = vehicles.size

    override fun getItem(position: Int): Any = vehicles[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val vehicle = vehicles[position]
        val rowView = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_vehicle, parent, false)

        // Vehicle Information
        val vehicleInfo = rowView.findViewById<TextView>(R.id.vehicleInfo)
        vehicleInfo.text = "${vehicle.brand} ${vehicle.model} (${vehicle.year}), VIN: ${vehicle.vin ?: "Not Provided"}"

        // Edit Button
        val editButton = rowView.findViewById<Button>(R.id.editButton)
        editButton.setOnClickListener {
            val vehicle = vehicles[position]
            val intent = Intent(context, AddVehicleActivity::class.java)
            intent.putExtra("VEHICLE_DOC_ID", vehicle.id)
            intent.putExtra("BRAND", vehicle.brand)
            intent.putExtra("MODEL", vehicle.model)
            intent.putExtra("YEAR", vehicle.year)
            intent.putExtra("VIN", vehicle.vin)
            context.startActivity(intent)
        }



        // Delete Button
        val deleteButton = rowView.findViewById<Button>(R.id.deleteButton)
        deleteButton.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Delete Vehicle")
                .setMessage("Are you sure you want to delete this vehicle?")
                .setPositiveButton("Yes") { _, _ ->
                    val userId = FirebaseAuth.getInstance().currentUser?.uid

                    if (userId != null) {
                        val vehicleToDelete = vehicles[position]

                        FirebaseFirestore.getInstance()
                            .collection("Users") // Ensure "Users" matches your Firestore collection name
                            .document(userId) // Current user's document
                            .collection("vehicles") // Vehicles subcollection
                            .document(vehicleToDelete.id) // Use the vehicle's unique ID
                            .delete()
                            .addOnSuccessListener {
                                vehicles.removeAt(position) // Remove from local list
                                notifyDataSetChanged() // Update UI
                                Toast.makeText(context, "Vehicle Successfully Deleted", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(context, "Error deleting vehicle: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                    } else {
                        Toast.makeText(context, "User not authenticated", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("No", null)
                .show()

        }


        return rowView
    }
}
