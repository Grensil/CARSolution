package com.example.carsolution.feature.fuel.viewmodel

import app.cash.turbine.test
import com.example.carsolution.core.common.UiState
import com.example.carsolution.domain.model.FuelStation
import com.example.carsolution.domain.repository.FuelStationRepository
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
    private val repository: FuelStationRepository = mockk()
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
                FuelStation("fuel-1", "SK에너지", "서울시 강남구", 1680, 1480),
            )
            coEvery { repository.getFuelStationList() } returns stations

            // When
            val viewModel = FuelHomeViewModel(repository)

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
            coEvery { repository.getFuelStationList() } throws IOException("네트워크 오류")

            // When
            val viewModel = FuelHomeViewModel(repository)

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
            coEvery { repository.getFuelStationList() } throws IllegalStateException("서버 오류")

            // When
            val viewModel = FuelHomeViewModel(repository)

            // Then
            viewModel.uiState.test {
                val state = awaitItem()
                assertTrue(state is UiState.Error)
                assertEquals("서버 오류", (state as UiState.Error).message)
            }
        }
}
