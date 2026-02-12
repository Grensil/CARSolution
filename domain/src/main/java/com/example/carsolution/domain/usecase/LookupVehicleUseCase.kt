package com.example.carsolution.domain.usecase

import com.example.carsolution.domain.model.Vehicle
import com.example.carsolution.domain.repository.VehicleRepository
import javax.inject.Inject

class LookupVehicleUseCase
    @Inject
    constructor(
        private val repository: VehicleRepository,
    ) {
        suspend operator fun invoke(plateNumber: String): Vehicle =
            repository.getVehicleByPlateNumber(plateNumber)
                ?: throw NoSuchElementException("차량 정보를 찾을 수 없습니다")
    }
