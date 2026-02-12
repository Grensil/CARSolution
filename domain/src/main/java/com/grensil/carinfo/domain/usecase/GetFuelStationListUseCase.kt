package com.grensil.carinfo.domain.usecase

import com.grensil.carinfo.domain.model.FuelStation
import com.grensil.carinfo.domain.repository.FuelStationRepository
import javax.inject.Inject

class GetFuelStationListUseCase
    @Inject
    constructor(
        private val repository: FuelStationRepository,
    ) {
        suspend operator fun invoke(): List<FuelStation> = repository.getFuelStationList()
    }
