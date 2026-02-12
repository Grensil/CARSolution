package com.grensil.carinfo.domain.usecase

import com.grensil.carinfo.domain.model.VehicleSpec
import com.grensil.carinfo.domain.repository.VehicleSpecRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class DecodeVinUseCaseTest {
    private val repository: VehicleSpecRepository = mockk()
    private val useCase = DecodeVinUseCase(repository)

    @Test
    fun `VIN을 디코딩하면 차량 스펙을 반환한다`() =
        runTest {
            val expected = VehicleSpec(
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
            coEvery { repository.decodeVin("1HGBH41JXMN109186") } returns expected

            val result = useCase("1HGBH41JXMN109186")

            assertEquals(expected, result)
        }

    @Test(expected = Exception::class)
    fun `예외가 발생하면 전파한다`() =
        runTest {
            coEvery { repository.decodeVin(any()) } throws Exception("오류")
            useCase("INVALID")
        }
}
