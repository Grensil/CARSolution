package com.grensil.carinfo.data.repository

import com.grensil.carinfo.data.api.NhtsaApiService
import com.grensil.carinfo.data.api.mapper.toVehicleSpec
import com.grensil.carinfo.domain.model.VehicleSpec
import com.grensil.carinfo.domain.repository.VehicleSpecRepository
import javax.inject.Inject

class NhtsaVehicleSpecRepository
    @Inject
    constructor(
        private val api: NhtsaApiService,
    ) : VehicleSpecRepository {
        override suspend fun decodeVin(vin: String): VehicleSpec {
            val response = api.decodeVin(vin)
            val result = response.results.firstOrNull()
                ?: throw IllegalStateException("VIN 디코딩 결과가 없습니다")
            return result.toVehicleSpec()
        }

        override suspend fun getAllMakes(): List<String> {
            val response = api.getAllMakes()
            return response.results.map { it.makeName }.sorted()
        }

        override suspend fun getModelsForMake(make: String): List<String> {
            val response = api.getModelsForMake(make)
            return response.results.map { it.modelName }.sorted()
        }
    }
