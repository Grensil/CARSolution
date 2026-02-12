package com.grensil.carinfo.domain.repository

import com.grensil.carinfo.domain.model.Vehicle

interface VehicleRepository {
    suspend fun getVehicleList(): List<Vehicle>

    suspend fun getVehicleById(id: String): Vehicle?

    suspend fun getVehicleByPlateNumber(plateNumber: String): Vehicle?
}
