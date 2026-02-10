package com.example.carsolution.domain.repository

import com.example.carsolution.domain.model.Insurance

interface InsuranceRepository {
    suspend fun getInsuranceList(): List<Insurance>

    suspend fun getInsuranceById(id: String): Insurance?
}
