package com.example.carsolution.data.repository

import com.example.carsolution.domain.model.Insurance
import com.example.carsolution.domain.repository.InsuranceRepository
import javax.inject.Inject

class FakeInsuranceRepository @Inject constructor() : InsuranceRepository {

    private val items = listOf(
        Insurance("ins-1", "자동차 종합보험", "삼성화재", 85000),
        Insurance("ins-2", "책임보험", "현대해상", 42000),
        Insurance("ins-3", "운전자보험", "DB손해보험", 35000),
    )

    override suspend fun getInsuranceList(): List<Insurance> = items

    override suspend fun getInsuranceById(id: String): Insurance? =
        items.find { it.id == id }
}
