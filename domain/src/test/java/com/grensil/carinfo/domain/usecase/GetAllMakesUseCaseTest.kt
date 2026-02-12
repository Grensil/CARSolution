package com.grensil.carinfo.domain.usecase

import com.grensil.carinfo.domain.repository.VehicleSpecRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetAllMakesUseCaseTest {
    private val repository: VehicleSpecRepository = mockk()
    private val useCase = GetAllMakesUseCase(repository)

    @Test
    fun `전체 제조사 목록을 반환한다`() =
        runTest {
            val expected = listOf("Honda", "Toyota", "Hyundai")
            coEvery { repository.getAllMakes() } returns expected

            val result = useCase()

            assertEquals(expected, result)
        }

    @Test(expected = Exception::class)
    fun `예외가 발생하면 전파한다`() =
        runTest {
            coEvery { repository.getAllMakes() } throws Exception("오류")
            useCase()
        }
}
