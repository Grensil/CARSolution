package com.example.carsolution.domain.repository

import com.example.carsolution.domain.model.FuelStation

interface FuelStationRepository {
    suspend fun getFuelStationList(): List<FuelStation>

    suspend fun getFuelStationById(id: String): FuelStation?
}
