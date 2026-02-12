package com.grensil.carinfo.data.api.mapper

import com.grensil.carinfo.data.api.dto.NhtsaVinResult
import com.grensil.carinfo.domain.model.VehicleSpec

fun NhtsaVinResult.toVehicleSpec(): VehicleSpec =
    VehicleSpec(
        id = vin.ifBlank { "unknown" },
        make = make,
        model = model,
        year = modelYear.toIntOrNull() ?: 0,
        bodyClass = bodyClass,
        driveType = driveType,
        fuelType = fuelTypePrimary,
        engineCylinders = engineCylinders.toIntOrNull() ?: 0,
        engineHp = engineHp.toIntOrNull() ?: 0,
        displacementL = displacementL.toDoubleOrNull() ?: 0.0,
        transmissionStyle = transmissionStyle,
        vin = vin,
    )
