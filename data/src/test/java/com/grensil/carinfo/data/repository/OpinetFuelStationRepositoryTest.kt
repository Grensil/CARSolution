package com.grensil.carinfo.data.repository

import com.grensil.carinfo.data.api.OpinetApiService
import com.grensil.carinfo.data.api.dto.OpinetAvgAllPriceResponse
import com.grensil.carinfo.data.api.dto.OpinetAvgAllPriceResult
import com.grensil.carinfo.data.api.dto.OpinetAvgPrice
import com.grensil.carinfo.data.api.dto.OpinetLowPriceStation
import com.grensil.carinfo.data.api.dto.OpinetLowTop10Response
import com.grensil.carinfo.data.api.dto.OpinetLowTop10Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class OpinetFuelStationRepositoryTest {
    private val api: OpinetApiService = mockk()
    private val fallback = FakeFuelStationRepository()

    @Test
    fun `API 키가 없으면 fallback을 사용한다`() =
        runTest {
            val repository = OpinetFuelStationRepository(api, "", fallback)

            val result = repository.getFuelStationList()

            assertEquals(3, result.size)
        }

    @Test
    fun `API 키가 있으면 OPINET API를 호출한다`() =
        runTest {
            coEvery { api.getLowTop10(any(), any(), any(), any()) } returns OpinetLowTop10Response(
                result = OpinetLowTop10Result(
                    oil = listOf(
                        OpinetLowPriceStation("S001", "Test Station", 1600.0, 1.0),
                    ),
                ),
            )
            val repository = OpinetFuelStationRepository(api, "test-key", fallback)

            val result = repository.getFuelStationList()

            assertEquals(1, result.size)
            assertEquals("Test Station", result[0].name)
        }

    @Test
    fun `평균 유가를 API에서 조회한다`() =
        runTest {
            coEvery { api.getAverageAllPrices(any(), any()) } returns OpinetAvgAllPriceResponse(
                result = OpinetAvgAllPriceResult(
                    oil = listOf(
                        OpinetAvgPrice("B027", "휘발유", 1650.0, -5.0, "20240101"),
                    ),
                ),
            )
            val repository = OpinetFuelStationRepository(api, "test-key", fallback)

            val result = repository.getAverageAllPrices()

            assertEquals(1, result.size)
            assertEquals("휘발유", result[0].productName)
        }

    @Test
    fun `API 키 없이 평균 유가 조회 시 fallback 데이터를 반환한다`() =
        runTest {
            val repository = OpinetFuelStationRepository(api, "", fallback)

            val result = repository.getAverageAllPrices()

            assertEquals(5, result.size)
        }
}
