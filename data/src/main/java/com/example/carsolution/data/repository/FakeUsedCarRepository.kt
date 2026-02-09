package com.example.carsolution.data.repository

import com.example.carsolution.domain.model.UsedCar
import com.example.carsolution.domain.repository.UsedCarRepository
import javax.inject.Inject

class FakeUsedCarRepository @Inject constructor() : UsedCarRepository {

    private val items = listOf(
        UsedCar("car-1", "현대 아반떼 CN7", 2022, 35000, 18500000),
        UsedCar("car-2", "기아 K5 DL3", 2021, 42000, 22000000),
        UsedCar("car-3", "제네시스 G70", 2023, 15000, 38000000),
    )

    override suspend fun getUsedCarList(): List<UsedCar> = items

    override suspend fun getUsedCarById(id: String): UsedCar? =
        items.find { it.id == id }
}
