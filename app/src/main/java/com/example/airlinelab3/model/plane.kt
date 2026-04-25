package com.example.airlinelab3.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "airplanes")

data class Airplane(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val model: String,
    val capacity: Int,
    val manufacturer: String,
    val airlineName: String
)