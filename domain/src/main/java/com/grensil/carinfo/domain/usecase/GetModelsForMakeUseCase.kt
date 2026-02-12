package com.grensil.carinfo.domain.usecase

import com.grensil.carinfo.domain.repository.VehicleSpecRepository
import javax.inject.Inject

class GetModelsForMakeUseCase
    @Inject
    constructor(
        private val repository: VehicleSpecRepository,
    ) {
        suspend operator fun invoke(make: String): List<String> = repository.getModelsForMake(make)
    }
