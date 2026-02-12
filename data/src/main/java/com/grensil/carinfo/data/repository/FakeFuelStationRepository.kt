@file:Suppress("MagicNumber")

package com.grensil.carinfo.data.repository

import com.grensil.carinfo.domain.model.FuelPrice
import com.grensil.carinfo.domain.model.FuelStation
import com.grensil.carinfo.domain.repository.FuelStationRepository
import javax.inject.Inject

class FakeFuelStationRepository
    @Inject
    constructor() : FuelStationRepository {
        private val items = listOf(
            FuelStation("fuel-1", "SK에너지 강남점", 1650, "SK에너지", 0.5),
            FuelStation("fuel-2", "GS칼텍스 서초점", 1630, "GS칼텍스", 1.2),
            FuelStation("fuel-3", "S-OIL 송파점", 1640, "S-OIL", 2.0),
        )

        override suspend fun getFuelStationList(): List<FuelStation> = items

        override suspend fun getFuelStationById(id: String): FuelStation? = items.find { it.id == id }

        override suspend fun getAverageAllPrices(): List<FuelPrice> =
            listOf(
                FuelPrice("B027", "휘발유", 1650.0, -5.0, "20240101"),
                FuelPrice("D047", "경유", 1450.0, -3.0, "20240101"),
                FuelPrice("K015", "등유", 1200.0, -2.0, "20240101"),
                FuelPrice("B034", "고급휘발유", 1850.0, -6.0, "20240101"),
                FuelPrice("C004", "LPG", 950.0, -1.0, "20240101"),
            )

        override suspend fun getLowTop10(
            productCode: String,
            areaCode: String,
        ): List<FuelStation> = items
    }
