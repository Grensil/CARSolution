package com.example.carsolution.feature.accident.viewmodel

import app.cash.turbine.test
import com.example.carsolution.core.common.UiState
import com.example.carsolution.domain.model.Accident
import com.example.carsolution.domain.usecase.GetAccidentListUseCase
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
class AccidentHomeViewModelTest {
    private val getAccidentList: GetAccidentListUseCase = mockk()
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
    fun `초기화 시 사고 리스트를 성공적으로 로드한다`() =
        runTest {
            // Given
            val accidents = listOf(
                Accident("acc-1", "2024-12-01", "서울시 강남구", "추돌사고"),
            )
            coEvery { getAccidentList() } returns accidents

            // When
            val viewModel = AccidentHomeViewModel(getAccidentList)

            // Then
            viewModel.uiState.test {
                val state = awaitItem()
                assertTrue(state is UiState.Success)
                assertEquals(accidents, (state as UiState.Success).data)
            }
        }

    @Test
    fun `IOException 발생 시 Error 상태가 된다`() =
        runTest {
            // Given
            coEvery { getAccidentList() } throws IOException("네트워크 오류")

            // When
            val viewModel = AccidentHomeViewModel(getAccidentList)

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
            coEvery { getAccidentList() } throws IllegalStateException("서버 오류")

            // When
            val viewModel = AccidentHomeViewModel(getAccidentList)

            // Then
            viewModel.uiState.test {
                val state = awaitItem()
                assertTrue(state is UiState.Error)
                assertEquals("서버 오류", (state as UiState.Error).message)
            }
        }
}
