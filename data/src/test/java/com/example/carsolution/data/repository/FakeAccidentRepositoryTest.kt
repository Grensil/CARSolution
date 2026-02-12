package com.example.carsolution.data.repository

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class FakeAccidentRepositoryTest {
    private lateinit var repository: FakeAccidentRepository

    @Before
    fun setup() {
        repository = FakeAccidentRepository()
    }

    @Test
    fun `사고 리스트는 3건을 반환한다`() =
        runTest {
            val result = repository.getAccidentList()
            assertEquals(3, result.size)
        }

    @Test
    fun `존재하는 ID로 조회하면 사고 정보를 반환한다`() =
        runTest {
            val result = repository.getAccidentById("acc-1")
            assertNotNull(result)
            assertEquals("acc-1", result?.id)
        }

    @Test
    fun `존재하지 않는 ID로 조회하면 null을 반환한다`() =
        runTest {
            val result = repository.getAccidentById("invalid-id")
            assertNull(result)
        }
}
