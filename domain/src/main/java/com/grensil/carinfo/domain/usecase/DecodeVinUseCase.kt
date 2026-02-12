package com.grensil.carinfo.domain.usecase

import com.grensil.carinfo.domain.model.VehicleSpec
import com.grensil.carinfo.domain.repository.VehicleSpecRepository
import javax.inject.Inject

class DecodeVinUseCase
    @Inject
    constructor(
        private val repository: VehicleSpecRepository,
    ) {
        suspend operator fun invoke(vin: String): VehicleSpec = repository.decodeVin(vin)
    }
