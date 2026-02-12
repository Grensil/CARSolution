package com.example.carsolution.domain.usecase

import com.example.carsolution.domain.model.Vehicle
import com.example.carsolution.domain.repository.VehicleRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class LookupVehicleUseCaseTest {
    private val repository: VehicleRepository = mockk()
    private val useCase = LookupVehicleUseCase(repository)

    @Test
    fun `차량 번호로 조회 성공 시 Vehicle을 반환한다`() =
        runTest {
            val expected = Vehicle("v-1", "12가3456", "아반떼", "현대", 2022, "가솔린", 1598)
            coEvery { repository.getVehicleByPlateNumber("12가3456") } returns expected

            val result = useCase("12가3456")

            assertEquals(expected, result)
        }

    @Test(expected = NoSuchElementException::class)
    fun `차량이 없으면 NoSuchElementException을 던진다`() =
        runTest {
            coEvery { repository.getVehicleByPlateNumber("99하9999") } returns null
            useCase("99하9999")
        }
}
