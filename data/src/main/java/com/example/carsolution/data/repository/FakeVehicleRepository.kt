package com.example.carsolution.data.repository

import com.example.carsolution.domain.model.Vehicle
import com.example.carsolution.domain.repository.VehicleRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class FakeVehicleRepository @Inject constructor() : VehicleRepository {

    private val items = listOf(
        Vehicle("v-1", "12가3456", "아반떼 CN7", "현대", 2022, "가솔린", 1598),
        Vehicle("v-2", "34나5678", "K5 DL3", "기아", 2021, "가솔린", 1999),
        Vehicle("v-3", "56다7890", "G70", "제네시스", 2023, "가솔린", 2497),
    )

    override suspend fun getVehicleList(): List<Vehicle> = items

    override suspend fun getVehicleById(id: String): Vehicle? =
        items.find { it.id == id }

    override suspend fun getVehicleByPlateNumber(plateNumber: String): Vehicle? {
        delay(1000) // 실제 API 호출 시뮬레이션
        return items.find { it.plateNumber == plateNumber }
            ?: Vehicle(
                id = "v-new",
                plateNumber = plateNumber,
                name = "쏘나타 DN8",
                manufacturer = "현대",
                year = 2023,
                fuelType = "가솔린",
                displacement = 1999,
            )
    }
}
