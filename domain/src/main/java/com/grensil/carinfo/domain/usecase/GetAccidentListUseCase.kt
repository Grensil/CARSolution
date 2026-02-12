package com.grensil.carinfo.domain.usecase

import com.grensil.carinfo.domain.model.Accident
import com.grensil.carinfo.domain.repository.AccidentRepository
import javax.inject.Inject

class GetAccidentListUseCase
    @Inject
    constructor(
        private val repository: AccidentRepository,
    ) {
        suspend operator fun invoke(): List<Accident> = repository.getAccidentList()
    }
