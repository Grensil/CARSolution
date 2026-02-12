package com.example.carsolution.feature.usedcar.viewmodel

import app.cash.turbine.test
import com.example.carsolution.core.common.UiState
import com.example.carsolution.domain.model.UsedCar
import com.example.carsolution.domain.usecase.GetUsedCarListUseCase
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
class UsedCarHomeViewModelTest {
    private val getUsedCarList: GetUsedCarListUseCase = mockk()
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
    fun `초기화 시 중고차 리스트를 성공적으로 로드한다`() =
        runTest {
            // Given
            val cars = listOf(
                UsedCar("car-1", "아반떼 CN7", 2022, 15000, 22000000),
            )
            coEvery { getUsedCarList() } returns cars

            // When
            val viewModel = UsedCarHomeViewModel(getUsedCarList)

            // Then
            viewModel.uiState.test {
                val state = awaitItem()
                assertTrue(state is UiState.Success)
                assertEquals(cars, (state as UiState.Success).data)
            }
        }

    @Test
    fun `IOException 발생 시 Error 상태가 된다`() =
        runTest {
            // Given
            coEvery { getUsedCarList() } throws IOException("네트워크 오류")

            // When
            val viewModel = UsedCarHomeViewModel(getUsedCarList)

            // Then
            viewModel.uiState.test {
                val state = awaitItem()
                assertTrue(state is UiState.Error)
            }
        }

    @Test
    fun `IllegalStateException 발생 시 Error 상태가 된다`() =
        runTest {
            // Given
            coEvery { getUsedCarList() } throws IllegalStateException("서버 오류")

            // When
            val viewModel = UsedCarHomeViewModel(getUsedCarList)

            // Then
            viewModel.uiState.test {
                val state = awaitItem()
                assertTrue(state is UiState.Error)
                assertEquals("서버 오류", (state as UiState.Error).message)
            }
        }
}
