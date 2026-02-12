package com.grensil.carinfo.domain.repository

import com.grensil.carinfo.domain.model.VehicleSpec

interface VehicleSpecRepository {
    suspend fun decodeVin(vin: String): VehicleSpec

    suspend fun getAllMakes(): List<String>

    suspend fun getModelsForMake(make: String): List<String>
}
