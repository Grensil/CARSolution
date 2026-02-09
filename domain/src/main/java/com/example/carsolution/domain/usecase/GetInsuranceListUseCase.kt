package com.example.carsolution.domain.usecase

import com.example.carsolution.domain.model.Insurance
import com.example.carsolution.domain.repository.InsuranceRepository

class GetInsuranceListUseCase(
    private val repository: InsuranceRepository,
) {
    suspend operator fun invoke(): List<Insurance> = repository.getInsuranceList()
}
