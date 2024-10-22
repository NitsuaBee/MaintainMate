package com.example.maintainmate

data class `Vehicle`(
    val brand: String,
    val model: String,
    val year: String,
    val vin: String? = null // Optional VIN field
)
