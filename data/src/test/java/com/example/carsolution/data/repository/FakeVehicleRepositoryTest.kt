package com.example.carsolution.data.repository

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class FakeVehicleRepositoryTest {
    private lateinit var repository: FakeVehicleRepository

    @Before
    fun setup() {
        repository = FakeVehicleRepository()
    }

    @Test
    fun `차량 리스트는 3건을 반환한다`() =
        runTest {
            val result = repository.getVehicleList()
            assertEquals(3, result.size)
        }

    @Test
    fun `존재하는 ID로 조회하면 차량 정보를 반환한다`() =
        runTest {
            val result = repository.getVehicleById("v-1")
            assertNotNull(result)
            assertEquals("v-1", result?.id)
        }

    @Test
    fun `존재하지 않는 ID로 조회하면 null을 반환한다`() =
        runTest {
            val result = repository.getVehicleById("invalid-id")
            assertNull(result)
        }

    @Test
    fun `존재하는 번호판으로 조회하면 차량 정보를 반환한다`() =
        runTest {
            val result = repository.getVehicleByPlateNumber("12가3456")
            assertNotNull(result)
            assertEquals("12가3456", result?.plateNumber)
        }

    @Test
    fun `존재하지 않는 번호판으로 조회하면 기본 차량을 생성하여 반환한다`() =
        runTest {
            val result = repository.getVehicleByPlateNumber("99하1234")
            assertNotNull(result)
            assertEquals("99하1234", result?.plateNumber)
            assertEquals("v-new", result?.id)
        }
}
