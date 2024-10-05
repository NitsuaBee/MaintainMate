package com.example.maintainmate2.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicles")
data class Vehicle(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "manufacturer")
    val manufacturer: String,
    @ColumnInfo(name = "model")
    val model: String,
    @ColumnInfo(name = "model_year")
    val modelYear: Int,
    @ColumnInfo(name = "mileage")
    val mileage: Int
)