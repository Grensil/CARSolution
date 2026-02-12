package com.grensil.carinfo.data.repository

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FakeFuelStationRepositoryTest {
    private lateinit var repository: FakeFuelStationRepository

    @Before
    fun setup() {
        repository = FakeFuelStationRepository()
    }

    @Test
    fun `주유소 리스트는 3건을 반환한다`() =
        runTest {
            val result = repository.getFuelStationList()
            assertEquals(3, result.size)
        }

    @Test
    fun `모든 주유소의 유가는 양수이다`() =
        runTest {
            val result = repository.getFuelStationList()
            assertTrue(result.all { it.price > 0 })
        }

    @Test
    fun `존재하는 ID로 조회하면 주유소 정보를 반환한다`() =
        runTest {
            val result = repository.getFuelStationById("fuel-1")
            assertNotNull(result)
            assertEquals("fuel-1", result?.id)
        }

    @Test
    fun `존재하지 않는 ID로 조회하면 null을 반환한다`() =
        runTest {
            val result = repository.getFuelStationById("invalid-id")
            assertNull(result)
        }
}
