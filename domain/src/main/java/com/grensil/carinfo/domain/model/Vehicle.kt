package com.grensil.carinfo.domain.model

data class Vehicle(
    val id: String,
    val plateNumber: String,
    val name: String,
    val manufacturer: String,
    val year: Int,
    val fuelType: String,
    val displacement: Int,
)
