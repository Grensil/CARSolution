package com.grensil.carinfo.domain.model

data class VehicleSpec(
    val id: String,
    val make: String,
    val model: String,
    val year: Int,
    val bodyClass: String,
    val driveType: String,
    val fuelType: String,
    val engineCylinders: Int,
    val engineHp: Int,
    val displacementL: Double,
    val transmissionStyle: String,
    val vin: String,
)
