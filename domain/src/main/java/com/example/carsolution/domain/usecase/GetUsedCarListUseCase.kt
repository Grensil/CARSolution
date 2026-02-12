package com.example.carsolution.domain.usecase

import com.example.carsolution.domain.model.UsedCar
import com.example.carsolution.domain.repository.UsedCarRepository
import javax.inject.Inject

class GetUsedCarListUseCase
    @Inject
    constructor(
        private val repository: UsedCarRepository,
    ) {
        suspend operator fun invoke(): List<UsedCar> = repository.getUsedCarList()
    }
