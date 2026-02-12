package com.example.carsolution.domain.usecase

import com.example.carsolution.domain.model.FuelStation
import com.example.carsolution.domain.repository.FuelStationRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetFuelStationListUseCaseTest {
    private val repository: FuelStationRepository = mockk()
    private val useCase = GetFuelStationListUseCase(repository)

    @Test
    fun `주유소 리스트를 반환한다`() =
        runTest {
            val expected = listOf(
                FuelStation("fuel-1", "SK에너지", "서울시 강남구", 1680, 1480),
            )
            coEvery { repository.getFuelStationList() } returns expected

            val result = useCase()

            assertEquals(expected, result)
        }

    @Test(expected = Exception::class)
    fun `예외가 발생하면 전파한다`() =
        runTest {
            coEvery { repository.getFuelStationList() } throws Exception("오류")
            useCase()
        }
}
