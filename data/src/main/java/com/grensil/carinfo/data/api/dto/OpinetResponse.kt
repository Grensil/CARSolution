package com.grensil.carinfo.data.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpinetAvgAllPriceResponse(
    @SerialName("RESULT") val result: OpinetAvgAllPriceResult,
)

@Serializable
data class OpinetAvgAllPriceResult(
    @SerialName("OIL") val oil: List<OpinetAvgPrice>,
)

@Serializable
data class OpinetAvgPrice(
    @SerialName("PRODCD") val productCode: String,
    @SerialName("PRODNM") val productName: String,
    @SerialName("PRICE") val price: Double,
    @SerialName("DIFF") val diff: Double,
    @SerialName("DATE") val date: String,
)

@Serializable
data class OpinetLowTop10Response(
    @SerialName("RESULT") val result: OpinetLowTop10Result,
)

@Serializable
data class OpinetLowTop10Result(
    @SerialName("OIL") val oil: List<OpinetLowPriceStation>,
)

@Serializable
data class OpinetLowPriceStation(
    @SerialName("UNI_ID") val stationId: String,
    @SerialName("OS_NM") val stationName: String,
    @SerialName("PRICE") val price: Double,
    @SerialName("DISTANCE") val distance: Double = 0.0,
    @SerialName("GIS_X_COOR") val gisX: Double = 0.0,
    @SerialName("GIS_Y_COOR") val gisY: Double = 0.0,
)
