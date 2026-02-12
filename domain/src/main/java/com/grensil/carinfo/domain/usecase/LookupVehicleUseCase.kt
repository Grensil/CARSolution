package com.grensil.carinfo.domain.usecase

import com.grensil.carinfo.domain.model.Vehicle
import com.grensil.carinfo.domain.repository.VehicleRepository
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
