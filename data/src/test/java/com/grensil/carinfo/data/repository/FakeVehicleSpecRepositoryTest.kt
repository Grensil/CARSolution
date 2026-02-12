package com.grensil.carinfo.data.repository

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FakeVehicleSpecRepositoryTest {
    private lateinit var repository: FakeVehicleSpecRepository

    @Before
    fun setup() {
        repository = FakeVehicleSpecRepository()
    }

    @Test
    fun `VIN 디코딩 시 차량 스펙을 반환한다`() =
        runTest {
            val result = repository.decodeVin("1HGBH41JXMN109186")
            assertEquals("1HGBH41JXMN109186", result.vin)
            assertEquals("Honda", result.make)
        }

    @Test
    fun `전체 제조사 목록은 5건을 반환한다`() =
        runTest {
            val result = repository.getAllMakes()
            assertEquals(5, result.size)
        }

    @Test
    fun `Honda 모델 목록을 반환한다`() =
        runTest {
            val result = repository.getModelsForMake("Honda")
            assertTrue(result.contains("Civic"))
        }

    @Test
    fun `존재하지 않는 제조사는 빈 목록을 반환한다`() =
        runTest {
            val result = repository.getModelsForMake("Unknown")
            assertTrue(result.isEmpty())
        }
}
