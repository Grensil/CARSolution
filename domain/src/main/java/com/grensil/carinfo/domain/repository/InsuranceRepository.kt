package com.grensil.carinfo.domain.repository

import com.grensil.carinfo.domain.model.Insurance

interface InsuranceRepository {
    suspend fun getInsuranceList(): List<Insurance>

    suspend fun getInsuranceById(id: String): Insurance?
}
