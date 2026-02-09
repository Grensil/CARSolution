package com.example.carsolution.domain.model

data class FuelStation(
    val id: String,
    val name: String,
    val address: String,
    val gasolinePrice: Int,
    val dieselPrice: Int,
)
