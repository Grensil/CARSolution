package com.grensil.carinfo.data.repository

import com.grensil.carinfo.data.api.NhtsaApiService
import com.grensil.carinfo.data.api.dto.NhtsaDecodeVinResponse
import com.grensil.carinfo.data.api.dto.NhtsaGetAllMakesResponse
import com.grensil.carinfo.data.api.dto.NhtsaGetModelsResponse
import com.grensil.carinfo.data.api.dto.NhtsaMakeResult
import com.grensil.carinfo.data.api.dto.NhtsaModelResult
import com.grensil.carinfo.data.api.dto.NhtsaVinResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class NhtsaVehicleSpecRepositoryTest {
    private val api: NhtsaApiService = mockk()
    private lateinit var repository: NhtsaVehicleSpecRepository

    @Before
    fun setup() {
        repository = NhtsaVehicleSpecRepository(api)
    }

    @Test
    fun `VIN 디코딩 시 API 응답을 VehicleSpec으로 변환한다`() =
        runTest {
            val vinResult = NhtsaVinResult(
                make = "Honda",
                model = "Civic",
                modelYear = "2021",
                bodyClass = "Sedan",
                driveType = "FWD",
                fuelTypePrimary = "Gasoline",
                engineCylinders = "4",
                engineHp = "158",
                displacementL = "2.0",
                transmissionStyle = "CVT",
                vin = "1HGBH41JXMN109186",
            )
            coEvery { api.decodeVin(any(), any()) } returns NhtsaDecodeVinResponse(
                count = 1,
                message = "OK",
                results = listOf(vinResult),
            )

            val result = repository.decodeVin("1HGBH41JXMN109186")

            assertEquals("Honda", result.make)
            assertEquals("Civic", result.model)
            assertEquals(2021, result.year)
        }

    @Test(expected = IllegalStateException::class)
    fun `빈 결과 시 예외를 발생시킨다`() =
        runTest {
            coEvery { api.decodeVin(any(), any()) } returns NhtsaDecodeVinResponse(
                count = 0,
                message = "No results",
                results = emptyList(),
            )

            repository.decodeVin("INVALID")
        }

    @Test
    fun `전체 제조사 목록을 정렬하여 반환한다`() =
        runTest {
            coEvery { api.getAllMakes(any()) } returns NhtsaGetAllMakesResponse(
                count = 3,
                message = "OK",
                results = listOf(
                    NhtsaMakeResult(1, "Toyota"),
                    NhtsaMakeResult(2, "Honda"),
                    NhtsaMakeResult(3, "BMW"),
                ),
            )

            val result = repository.getAllMakes()

            assertEquals(listOf("BMW", "Honda", "Toyota"), result)
        }

    @Test
    fun `제조사별 모델 목록을 정렬하여 반환한다`() =
        runTest {
            coEvery { api.getModelsForMake(any(), any()) } returns NhtsaGetModelsResponse(
                count = 2,
                message = "OK",
                results = listOf(
                    NhtsaModelResult(1, "Honda", 1, "Civic"),
                    NhtsaModelResult(1, "Honda", 2, "Accord"),
                ),
            )

            val result = repository.getModelsForMake("Honda")

            assertEquals(listOf("Accord", "Civic"), result)
        }
}
