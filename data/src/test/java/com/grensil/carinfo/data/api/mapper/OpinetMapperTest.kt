package com.grensil.carinfo.data.api.mapper

import com.grensil.carinfo.data.api.dto.OpinetAvgPrice
import com.grensil.carinfo.data.api.dto.OpinetLowPriceStation
import org.junit.Assert.assertEquals
import org.junit.Test

class OpinetMapperTest {
    @Test
    fun `OpinetAvgPrice를 FuelPrice로 변환한다`() {
        val avgPrice = OpinetAvgPrice(
            productCode = "B027",
            productName = "휘발유",
            price = 1650.5,
            diff = -5.0,
            date = "20240101",
        )

        val fuelPrice = avgPrice.toFuelPrice()

        assertEquals("B027", fuelPrice.productCode)
        assertEquals("휘발유", fuelPrice.productName)
        assertEquals(1650.5, fuelPrice.price, 0.01)
        assertEquals(-5.0, fuelPrice.diff, 0.01)
        assertEquals("20240101", fuelPrice.date)
    }

    @Test
    fun `OpinetLowPriceStation을 FuelStation으로 변환한다`() {
        val station = OpinetLowPriceStation(
            stationId = "A0001",
            stationName = "SK에너지 강남점",
            price = 1630.0,
            distance = 1.5,
        )

        val fuelStation = station.toFuelStation()

        assertEquals("A0001", fuelStation.id)
        assertEquals("SK에너지 강남점", fuelStation.name)
        assertEquals(1630L, fuelStation.price)
        assertEquals(1.5, fuelStation.distance, 0.01)
    }
}
