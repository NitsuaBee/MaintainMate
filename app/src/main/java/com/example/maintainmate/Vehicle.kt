package com.example.maintainmate

data class Vehicle(
    val id: String,
    val brand: String,
    val model: String,
    val year: String,
    val vin: String? = null,
    val mileage: Int = 0 // New mileage field
) {
    // Method to convert Vehicle instance to a map
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "brand" to brand,
            "model" to model,
            "year" to year,
            "vin" to vin,
            "mileage" to mileage
        )
    }
}
