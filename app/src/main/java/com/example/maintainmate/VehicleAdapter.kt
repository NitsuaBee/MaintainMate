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

class VehicleAdapter(private val context: Context, private val vehicles: MutableList<Vehicle>) : BaseAdapter() {

    override fun getCount(): Int {
        return vehicles.size
    }

    override fun getItem(position: Int): Any {
        return vehicles[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val vehicle = vehicles[position]

        val rowView = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_vehicle, parent, false)

        // Vehicle Information
        val vehicleInfo = rowView.findViewById<TextView>(R.id.vehicleInfo)
        vehicleInfo.text = "${vehicle.brand} ${vehicle.model} (${vehicle.year}), VIN: ${vehicle.vin ?: "Not Provided"}"

        // Edit Button
        val editButton = rowView.findViewById<Button>(R.id.editButton)
        editButton.setOnClickListener {
            val intent = Intent(context, AddVehicleActivity::class.java)
            intent.putExtra("EDIT_VEHICLE", position) // Pass position for editing
            context.startActivity(intent)
        }

        // Delete Button
        val deleteButton = rowView.findViewById<Button>(R.id.deleteButton)
        deleteButton.setOnClickListener {
            // Show confirmation dialog
            AlertDialog.Builder(context)
                .setTitle("Delete Vehicle")
                .setMessage("Are you sure you want to delete this vehicle?")
                .setPositiveButton("Yes") { _, _ ->
                    vehicles.removeAt(position)
                    notifyDataSetChanged() // Refresh the list
                }
                .setNegativeButton("No", null)
                .show()
        }

        return rowView
    }
}
