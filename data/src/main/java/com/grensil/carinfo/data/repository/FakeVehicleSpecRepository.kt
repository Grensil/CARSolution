@file:Suppress("MagicNumber")

package com.grensil.carinfo.data.repository

import com.grensil.carinfo.domain.model.VehicleSpec
import com.grensil.carinfo.domain.repository.VehicleSpecRepository
import javax.inject.Inject

class FakeVehicleSpecRepository
    @Inject
    constructor() : VehicleSpecRepository {
        private val fakeSpec = VehicleSpec(
            id = "1",
            make = "Honda",
            model = "Civic",
            year = 2021,
            bodyClass = "Sedan/Saloon",
            driveType = "Front-Wheel Drive",
            fuelType = "Gasoline",
            engineCylinders = 4,
            engineHp = 158,
            displacementL = 2.0,
            transmissionStyle = "CVT",
            vin = "1HGBH41JXMN109186",
        )

        override suspend fun decodeVin(vin: String): VehicleSpec = fakeSpec.copy(vin = vin)

        override suspend fun getAllMakes(): List<String> = listOf("Honda", "Toyota", "Hyundai", "Kia", "BMW")

        override suspend fun getModelsForMake(make: String): List<String> =
            when (make) {
                "Honda" -> listOf("Civic", "Accord", "CR-V")
                "Toyota" -> listOf("Camry", "Corolla", "RAV4")
                "Hyundai" -> listOf("Sonata", "Tucson", "Ioniq 5")
                "Kia" -> listOf("K5", "Sportage", "EV6")
                "BMW" -> listOf("3 Series", "5 Series", "X5")
                else -> emptyList()
            }
    }
