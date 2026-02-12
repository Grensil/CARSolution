package com.grensil.carinfo.feature.fuel.viewmodel

import app.cash.turbine.test
import com.grensil.carinfo.core.common.UiState
import com.grensil.carinfo.domain.model.FuelStation
import com.grensil.carinfo.domain.usecase.GetFuelStationListUseCase
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
class FuelHomeViewModelTest {
    private val getFuelStationList: GetFuelStationListUseCase = mockk()
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
    fun `초기화 시 주유소 리스트를 성공적으로 로드한다`() =
        runTest {
            // Given
            val stations = listOf(
                FuelStation("fuel-1", "SK에너지", 1680, "SK에너지", 0.5),
            )
            coEvery { getFuelStationList() } returns stations

            // When
            val viewModel = FuelHomeViewModel(getFuelStationList)

            // Then
            viewModel.uiState.test {
                val state = awaitItem()
                assertTrue(state is UiState.Success)
                assertEquals(stations, (state as UiState.Success).data)
            }
        }

    @Test
    fun `IOException 발생 시 Error 상태가 된다`() =
        runTest {
            // Given
            coEvery { getFuelStationList() } throws IOException("네트워크 오류")

            // When
            val viewModel = FuelHomeViewModel(getFuelStationList)

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
            coEvery { getFuelStationList() } throws IllegalStateException("서버 오류")

            // When
            val viewModel = FuelHomeViewModel(getFuelStationList)

            // Then
            viewModel.uiState.test {
                val state = awaitItem()
                assertTrue(state is UiState.Error)
                assertEquals("서버 오류", (state as UiState.Error).message)
            }
        }
}
