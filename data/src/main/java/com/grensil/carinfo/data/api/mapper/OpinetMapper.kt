package com.grensil.carinfo.data.api.mapper

import com.grensil.carinfo.data.api.dto.OpinetAvgPrice
import com.grensil.carinfo.data.api.dto.OpinetLowPriceStation
import com.grensil.carinfo.domain.model.FuelPrice
import com.grensil.carinfo.domain.model.FuelStation

fun OpinetAvgPrice.toFuelPrice(): FuelPrice =
    FuelPrice(
        productCode = productCode,
        productName = productName,
        price = price,
        diff = diff,
        date = date,
    )

fun OpinetLowPriceStation.toFuelStation(): FuelStation =
    FuelStation(
        id = stationId,
        name = stationName,
        price = price.toLong(),
        brand = "",
        distance = distance,
    )
