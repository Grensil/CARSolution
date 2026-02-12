package com.example.carsolution.domain.usecase

import com.example.carsolution.domain.model.Accident
import com.example.carsolution.domain.repository.AccidentRepository
import javax.inject.Inject

class GetAccidentListUseCase
    @Inject
    constructor(
        private val repository: AccidentRepository,
    ) {
        suspend operator fun invoke(): List<Accident> = repository.getAccidentList()
    }
