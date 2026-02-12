package com.grensil.carinfo.domain.model

data class FuelPrice(
    val productCode: String,
    val productName: String,
    val price: Double,
    val diff: Double,
    val date: String,
)
