package com.grensil.carinfo.domain.repository

import com.grensil.carinfo.domain.model.FuelPrice
import com.grensil.carinfo.domain.model.FuelStation

interface FuelStationRepository {
    suspend fun getFuelStationList(): List<FuelStation>

    suspend fun getFuelStationById(id: String): FuelStation?

    suspend fun getAverageAllPrices(): List<FuelPrice>

    suspend fun getLowTop10(
        productCode: String,
        areaCode: String = "",
    ): List<FuelStation>
}
