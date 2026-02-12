package com.grensil.carinfo.domain.usecase

import com.grensil.carinfo.domain.repository.VehicleSpecRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetModelsForMakeUseCaseTest {
    private val repository: VehicleSpecRepository = mockk()
    private val useCase = GetModelsForMakeUseCase(repository)

    @Test
    fun `제조사에 해당하는 모델 목록을 반환한다`() =
        runTest {
            val expected = listOf("Civic", "Accord", "CR-V")
            coEvery { repository.getModelsForMake("Honda") } returns expected

            val result = useCase("Honda")

            assertEquals(expected, result)
        }

    @Test(expected = Exception::class)
    fun `예외가 발생하면 전파한다`() =
        runTest {
            coEvery { repository.getModelsForMake(any()) } throws Exception("오류")
            useCase("Invalid")
        }
}
