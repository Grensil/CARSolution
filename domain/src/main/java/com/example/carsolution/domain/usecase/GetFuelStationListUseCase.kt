package com.example.carsolution.domain.usecase

import com.example.carsolution.domain.model.FuelStation
import com.example.carsolution.domain.repository.FuelStationRepository
import javax.inject.Inject

class GetFuelStationListUseCase
    @Inject
    constructor(
        private val repository: FuelStationRepository,
    ) {
        suspend operator fun invoke(): List<FuelStation> = repository.getFuelStationList()
    }
