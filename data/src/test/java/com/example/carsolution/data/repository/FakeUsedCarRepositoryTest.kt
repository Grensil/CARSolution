package com.example.carsolution.data.repository

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class FakeUsedCarRepositoryTest {
    private lateinit var repository: FakeUsedCarRepository

    @Before
    fun setup() {
        repository = FakeUsedCarRepository()
    }

    @Test
    fun `중고차 리스트는 3건을 반환한다`() =
        runTest {
            val result = repository.getUsedCarList()
            assertEquals(3, result.size)
        }

    @Test
    fun `존재하는 ID로 조회하면 중고차 정보를 반환한다`() =
        runTest {
            val result = repository.getUsedCarById("car-1")
            assertNotNull(result)
            assertEquals("car-1", result?.id)
        }

    @Test
    fun `존재하지 않는 ID로 조회하면 null을 반환한다`() =
        runTest {
            val result = repository.getUsedCarById("invalid-id")
            assertNull(result)
        }
}
