package com.example.maintainmate

data class Vehicle(
    val id: String,
    val brand: String,
    val model: String,
    val year: String,
    val vin: String? = null
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "brand" to brand,
            "model" to model,
            "year" to year,
            "vin" to vin
        )
    }
}
