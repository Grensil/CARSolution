package com.grensil.carinfo.feature.vehiclespec.viewmodel

import app.cash.turbine.test
import com.grensil.carinfo.core.common.UiState
import com.grensil.carinfo.domain.model.VehicleSpec
import com.grensil.carinfo.domain.usecase.DecodeVinUseCase
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
class VehicleSpecHomeViewModelTest {
    private val decodeVin: DecodeVinUseCase = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()

    private val fakeSpec = VehicleSpec(
        id = "1",
        make = "Honda",
        model = "Civic",
        year = 2021,
        bodyClass = "Sedan",
        driveType = "FWD",
        fuelType = "Gasoline",
        engineCylinders = 4,
        engineHp = 158,
        displacementL = 2.0,
        transmissionStyle = "CVT",
        vin = "1HGBH41JXMN109186",
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `초기화 시 기본 VIN으로 차량 스펙을 로드한다`() =
        runTest {
            coEvery { decodeVin(any()) } returns fakeSpec

            val viewModel = VehicleSpecHomeViewModel(decodeVin)

            viewModel.uiState.test {
                val state = awaitItem()
                assertTrue(state is UiState.Success)
                assertEquals(fakeSpec, (state as UiState.Success).data)
            }
        }

    @Test
    fun `IOException 발생 시 Error 상태가 된다`() =
        runTest {
            coEvery { decodeVin(any()) } throws IOException("네트워크 오류")

            val viewModel = VehicleSpecHomeViewModel(decodeVin)

            viewModel.uiState.test {
                val state = awaitItem()
                assertTrue(state is UiState.Error)
            }
        }

    @Test
    fun `IllegalStateException 발생 시 Error 상태가 된다`() =
        runTest {
            coEvery { decodeVin(any()) } throws IllegalStateException("서버 오류")

            val viewModel = VehicleSpecHomeViewModel(decodeVin)

            viewModel.uiState.test {
                val state = awaitItem()
                assertTrue(state is UiState.Error)
                assertEquals("서버 오류", (state as UiState.Error).message)
            }
        }
}
