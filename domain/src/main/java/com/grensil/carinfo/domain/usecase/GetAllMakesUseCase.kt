package com.grensil.carinfo.domain.usecase

import com.grensil.carinfo.domain.repository.VehicleSpecRepository
import javax.inject.Inject

class GetAllMakesUseCase
    @Inject
    constructor(
        private val repository: VehicleSpecRepository,
    ) {
        suspend operator fun invoke(): List<String> = repository.getAllMakes()
    }
