package com.example.carsolution.domain.usecase

import com.example.carsolution.domain.model.Accident
import com.example.carsolution.domain.repository.AccidentRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetAccidentListUseCaseTest {
    private val repository: AccidentRepository = mockk()
    private val useCase = GetAccidentListUseCase(repository)

    @Test
    fun `사고 리스트를 반환한다`() =
        runTest {
            val expected = listOf(
                Accident("acc-1", "2024-12-01", "서울시 강남구", "추돌사고"),
            )
            coEvery { repository.getAccidentList() } returns expected

            val result = useCase()

            assertEquals(expected, result)
        }

    @Test(expected = Exception::class)
    fun `예외가 발생하면 전파한다`() =
        runTest {
            coEvery { repository.getAccidentList() } throws Exception("오류")
            useCase()
        }
}
