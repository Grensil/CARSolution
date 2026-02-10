package com.example.carsolution.domain.usecase

import com.example.carsolution.domain.model.Insurance
import com.example.carsolution.domain.repository.InsuranceRepository
import javax.inject.Inject

class GetInsuranceListUseCase
    @Inject
    constructor(
        private val repository: InsuranceRepository,
    ) {
        suspend operator fun invoke(): List<Insurance> = repository.getInsuranceList()
    }
