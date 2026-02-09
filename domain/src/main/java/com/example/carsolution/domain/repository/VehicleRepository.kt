package com.example.carsolution.domain.repository

import com.example.carsolution.domain.model.Vehicle

interface VehicleRepository {
    suspend fun getVehicleList(): List<Vehicle>
    suspend fun getVehicleById(id: String): Vehicle?
    suspend fun getVehicleByPlateNumber(plateNumber: String): Vehicle?
}
