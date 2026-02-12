package com.grensil.carinfo.domain.usecase

import com.grensil.carinfo.domain.model.Insurance
import com.grensil.carinfo.domain.repository.InsuranceRepository
import javax.inject.Inject

class GetInsuranceListUseCase
    @Inject
    constructor(
        private val repository: InsuranceRepository,
    ) {
        suspend operator fun invoke(): List<Insurance> = repository.getInsuranceList()
    }
