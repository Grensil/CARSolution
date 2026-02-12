package com.example.carsolution.domain.usecase

import com.example.carsolution.domain.model.UsedCar
import com.example.carsolution.domain.repository.UsedCarRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetUsedCarListUseCaseTest {
    private val repository: UsedCarRepository = mockk()
    private val useCase = GetUsedCarListUseCase(repository)

    @Test
    fun `중고차 리스트를 반환한다`() =
        runTest {
            val expected = listOf(
                UsedCar("car-1", "아반떼 CN7", 2022, 15000, 22000000),
            )
            coEvery { repository.getUsedCarList() } returns expected

            val result = useCase()

            assertEquals(expected, result)
        }

    @Test(expected = Exception::class)
    fun `예외가 발생하면 전파한다`() =
        runTest {
            coEvery { repository.getUsedCarList() } throws Exception("오류")
            useCase()
        }
}
