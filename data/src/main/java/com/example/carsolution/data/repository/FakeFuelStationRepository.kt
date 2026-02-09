package com.example.carsolution.data.repository

import com.example.carsolution.domain.model.FuelStation
import com.example.carsolution.domain.repository.FuelStationRepository
import javax.inject.Inject

class FakeFuelStationRepository @Inject constructor() : FuelStationRepository {

    private val items = listOf(
        FuelStation("fuel-1", "SK에너지 강남점", "서울시 강남구", 1650, 1450),
        FuelStation("fuel-2", "GS칼텍스 서초점", "서울시 서초구", 1630, 1430),
        FuelStation("fuel-3", "S-OIL 송파점", "서울시 송파구", 1640, 1440),
    )

    override suspend fun getFuelStationList(): List<FuelStation> = items

    override suspend fun getFuelStationById(id: String): FuelStation? =
        items.find { it.id == id }
}
