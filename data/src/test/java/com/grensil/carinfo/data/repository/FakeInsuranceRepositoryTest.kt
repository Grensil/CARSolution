package com.grensil.carinfo.data.repository

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FakeInsuranceRepositoryTest {
    private lateinit var repository: FakeInsuranceRepository

    @Before
    fun setup() {
        repository = FakeInsuranceRepository()
    }

    @Test
    fun `보험 리스트는 3건을 반환한다`() =
        runTest {
            val result = repository.getInsuranceList()
            assertEquals(3, result.size)
        }

    @Test
    fun `모든 보험의 월 보험료는 양수이다`() =
        runTest {
            val result = repository.getInsuranceList()
            assertTrue(result.all { it.monthlyPremium > 0 })
        }

    @Test
    fun `존재하는 ID로 조회하면 보험 정보를 반환한다`() =
        runTest {
            val result = repository.getInsuranceById("ins-1")
            assertNotNull(result)
            assertEquals("ins-1", result?.id)
        }

    @Test
    fun `존재하지 않는 ID로 조회하면 null을 반환한다`() =
        runTest {
            val result = repository.getInsuranceById("invalid-id")
            assertNull(result)
        }
}
