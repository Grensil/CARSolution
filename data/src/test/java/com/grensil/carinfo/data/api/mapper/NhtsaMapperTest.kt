package com.grensil.carinfo.data.api.mapper

import com.grensil.carinfo.data.api.dto.NhtsaVinResult
import org.junit.Assert.assertEquals
import org.junit.Test

class NhtsaMapperTest {
    @Test
    fun `NhtsaVinResult를 VehicleSpec으로 변환한다`() {
        val result = NhtsaVinResult(
            make = "Honda",
            model = "Civic",
            modelYear = "2021",
            bodyClass = "Sedan/Saloon",
            driveType = "Front-Wheel Drive",
            fuelTypePrimary = "Gasoline",
            engineCylinders = "4",
            engineHp = "158",
            displacementL = "2.0",
            transmissionStyle = "CVT",
            vin = "1HGBH41JXMN109186",
        )

        val spec = result.toVehicleSpec()

        assertEquals("Honda", spec.make)
        assertEquals("Civic", spec.model)
        assertEquals(2021, spec.year)
        assertEquals("Sedan/Saloon", spec.bodyClass)
        assertEquals(4, spec.engineCylinders)
        assertEquals(158, spec.engineHp)
        assertEquals(2.0, spec.displacementL, 0.01)
        assertEquals("1HGBH41JXMN109186", spec.vin)
    }

    @Test
    fun `빈 숫자 필드는 0으로 변환된다`() {
        val result = NhtsaVinResult(
            make = "Honda",
            model = "Civic",
            modelYear = "",
            engineCylinders = "",
            engineHp = "",
            displacementL = "",
        )

        val spec = result.toVehicleSpec()

        assertEquals(0, spec.year)
        assertEquals(0, spec.engineCylinders)
        assertEquals(0, spec.engineHp)
        assertEquals(0.0, spec.displacementL, 0.01)
    }

    @Test
    fun `빈 VIN은 unknown ID를 생성한다`() {
        val result = NhtsaVinResult()

        val spec = result.toVehicleSpec()

        assertEquals("unknown", spec.id)
    }
}
