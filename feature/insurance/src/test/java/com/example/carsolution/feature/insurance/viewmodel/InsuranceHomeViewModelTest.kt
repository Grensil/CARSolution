package com.example.carsolution.feature.insurance.viewmodel

import app.cash.turbine.test
import com.example.carsolution.core.common.UiState
import com.example.carsolution.domain.model.Insurance
import com.example.carsolution.domain.usecase.GetInsuranceListUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class InsuranceHomeViewModelTest {
    private val getInsuranceList: GetInsuranceListUseCase = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `초기화 시 보험 리스트를 성공적으로 로드한다`() =
        runTest {
            // Given
            val insurances = listOf(
                Insurance("ins-1", "종합보험", "삼성화재", 50000),
                Insurance("ins-2", "책임보험", "현대해상", 30000),
            )
            coEvery { getInsuranceList() } returns insurances

            // When
            val viewModel = InsuranceHomeViewModel(getInsuranceList)

            // Then
            viewModel.uiState.test {
                val state = awaitItem()
                assertTrue(state is UiState.Success)
                assertEquals(insurances, (state as UiState.Success).data)
            }
        }

    @Test
    fun `보험 리스트가 비어있으면 빈 Success를 반환한다`() =
        runTest {
            // Given
            coEvery { getInsuranceList() } returns emptyList()

            // When
            val viewModel = InsuranceHomeViewModel(getInsuranceList)

            // Then
            viewModel.uiState.test {
                val state = awaitItem()
                assertTrue(state is UiState.Success)
                assertTrue((state as UiState.Success).data.isEmpty())
            }
        }

    @Test
    fun `IOException 발생 시 Error 상태가 된다`() =
        runTest {
            // Given
            coEvery { getInsuranceList() } throws IOException("네트워크 오류")

            // When
            val viewModel = InsuranceHomeViewModel(getInsuranceList)

            // Then
            viewModel.uiState.test {
                val state = awaitItem()
                assertTrue(state is UiState.Error)
                assertEquals("네트워크 오류", (state as UiState.Error).message)
            }
        }

    @Test
    fun `IllegalStateException 발생 시 Error 상태가 된다`() =
        runTest {
            // Given
            coEvery { getInsuranceList() } throws IllegalStateException("서버 오류")

            // When
            val viewModel = InsuranceHomeViewModel(getInsuranceList)

            // Then
            viewModel.uiState.test {
                val state = awaitItem()
                assertTrue(state is UiState.Error)
                assertEquals("서버 오류", (state as UiState.Error).message)
            }
        }
}
