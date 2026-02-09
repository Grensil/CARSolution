package com.example.carsolution.data.repository

import com.example.carsolution.domain.model.Vehicle
import com.example.carsolution.domain.repository.VehicleRepository
import javax.inject.Inject

class FakeVehicleRepository @Inject constructor() : VehicleRepository {

    private val items = listOf(
        Vehicle("v-1", "아반떼 CN7", "현대", 2022),
        Vehicle("v-2", "K5 DL3", "기아", 2021),
        Vehicle("v-3", "G70", "제네시스", 2023),
    )

    override suspend fun getVehicleList(): List<Vehicle> = items

    override suspend fun getVehicleById(id: String): Vehicle? =
        items.find { it.id == id }
}
