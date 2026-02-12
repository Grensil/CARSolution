package com.grensil.carinfo.data.repository

import com.grensil.carinfo.data.api.OpinetApiService
import com.grensil.carinfo.data.api.mapper.toFuelPrice
import com.grensil.carinfo.data.api.mapper.toFuelStation
import com.grensil.carinfo.domain.model.FuelPrice
import com.grensil.carinfo.domain.model.FuelStation
import com.grensil.carinfo.domain.repository.FuelStationRepository
import javax.inject.Inject
import javax.inject.Named

class OpinetFuelStationRepository
    @Inject
    constructor(
        private val api: OpinetApiService,
        @Named("opinetApiKey") private val apiKey: String,
        private val fallback: FakeFuelStationRepository,
    ) : FuelStationRepository {
        private val hasApiKey: Boolean get() = apiKey.isNotBlank()

        override suspend fun getFuelStationList(): List<FuelStation> =
            if (hasApiKey) {
                getLowTop10("B027")
            } else {
                fallback.getFuelStationList()
            }

        override suspend fun getFuelStationById(id: String): FuelStation? = fallback.getFuelStationById(id)

        override suspend fun getAverageAllPrices(): List<FuelPrice> =
            if (hasApiKey) {
                val response = api.getAverageAllPrices(apiKey = apiKey)
                response.result.oil.map { it.toFuelPrice() }
            } else {
                fallback.getAverageAllPrices()
            }

        override suspend fun getLowTop10(
            productCode: String,
            areaCode: String,
        ): List<FuelStation> =
            if (hasApiKey) {
                val response = api.getLowTop10(apiKey = apiKey, productCode = productCode, areaCode = areaCode)
                response.result.oil.map { it.toFuelStation() }
            } else {
                fallback.getLowTop10(productCode, areaCode)
            }
    }
