package com.grensil.carinfo.feature.auth.viewmodel

import app.cash.turbine.test
import com.grensil.carinfo.core.common.UiState
import com.grensil.carinfo.domain.model.Vehicle
import com.grensil.carinfo.domain.usecase.LookupVehicleUseCase
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
class AuthViewModelTest {
    private val lookupVehicle: LookupVehicleUseCase = mockk()
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
    fun `차량 번호로 조회 성공 시 Success 상태가 된다`() =
        runTest {
            // Given
            val vehicle = Vehicle("v-1", "12가3456", "아반떼", "현대", 2022, "가솔린", 1598)
            coEvery { lookupVehicle("12가3456") } returns vehicle

            val viewModel = AuthViewModel(lookupVehicle)

            // When
            viewModel.lookupVehicle("12가3456")

            // Then
            viewModel.vehicleLookup.test {
                val state = awaitItem()
                assertTrue(state is UiState.Success)
                assertEquals(vehicle, (state as UiState.Success).data)
            }
        }

    @Test
    fun `차량 번호로 조회 결과가 없으면 Error 상태가 된다`() =
        runTest {
            // Given
            coEvery { lookupVehicle("99하9999") } throws NoSuchElementException("차량 정보를 찾을 수 없습니다")

            val viewModel = AuthViewModel(lookupVehicle)

            // When
            viewModel.lookupVehicle("99하9999")

            // Then
            viewModel.vehicleLookup.test {
                val state = awaitItem()
                assertTrue(state is UiState.Error)
                assertEquals("차량 정보를 찾을 수 없습니다", (state as UiState.Error).message)
            }
        }

    @Test
    fun `IOException 발생 시 Error 상태가 된다`() =
        runTest {
            // Given
            coEvery { lookupVehicle(any()) } throws IOException("네트워크 오류")

            val viewModel = AuthViewModel(lookupVehicle)

            // When
            viewModel.lookupVehicle("12가3456")

            // Then
            viewModel.vehicleLookup.test {
                val state = awaitItem()
                assertTrue(state is UiState.Error)
                assertEquals("네트워크 오류", (state as UiState.Error).message)
            }
        }
}
