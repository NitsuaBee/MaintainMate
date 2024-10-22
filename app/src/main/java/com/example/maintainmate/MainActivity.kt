package com.example.maintainmate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonOpenAddVehicle = findViewById<Button>(R.id.buttonOpenAddVehicle)
        val buttonViewVehicles = findViewById<Button>(R.id.buttonViewVehicles)

        // Navigate to AddVehicleActivity when the button is clicked
        buttonOpenAddVehicle.setOnClickListener {
            val intent = Intent(this, AddVehicleActivity::class.java)
            startActivity(intent)
        }

        // Navigate to ViewVehiclesActivity when the button is clicked
        buttonViewVehicles.setOnClickListener {
            val intent = Intent(this, ViewVehiclesActivity::class.java)
            startActivity(intent)
        }
    }
}
