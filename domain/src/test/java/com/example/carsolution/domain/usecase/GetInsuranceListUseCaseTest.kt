package com.example.carsolution.domain.usecase

import com.example.carsolution.domain.model.Insurance
import com.example.carsolution.domain.repository.InsuranceRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class GetInsuranceListUseCaseTest {
    private val repository: InsuranceRepository = mockk()
    private val useCase = GetInsuranceListUseCase(repository)

    @Test
    fun `보험 리스트를 정상적으로 반환한다`() =
        runTest {
            // Given
            val expected = listOf(
                Insurance("ins-1", "종합보험", "삼성화재", 50000),
                Insurance("ins-2", "책임보험", "현대해상", 30000),
            )
            coEvery { repository.getInsuranceList() } returns expected

            // When
            val result = useCase()

            // Then
            assertEquals(expected, result)
            coVerify(exactly = 1) { repository.getInsuranceList() }
        }

    @Test
    fun `보험 리스트가 비어있으면 빈 리스트를 반환한다`() =
        runTest {
            // Given
            coEvery { repository.getInsuranceList() } returns emptyList()

            // When
            val result = useCase()

            // Then
            assertTrue(result.isEmpty())
        }

    @Test(expected = IOException::class)
    fun `Repository에서 IOException 발생 시 그대로 전파한다`() =
        runTest {
            // Given
            coEvery { repository.getInsuranceList() } throws IOException("네트워크 오류")

            // When
            useCase()
        }
}
