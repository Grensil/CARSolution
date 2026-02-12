package com.grensil.carinfo.data.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NhtsaDecodeVinResponse(
    @SerialName("Count") val count: Int,
    @SerialName("Message") val message: String,
    @SerialName("Results") val results: List<NhtsaVinResult>,
)

@Serializable
data class NhtsaVinResult(
    @SerialName("Make") val make: String = "",
    @SerialName("Model") val model: String = "",
    @SerialName("ModelYear") val modelYear: String = "",
    @SerialName("BodyClass") val bodyClass: String = "",
    @SerialName("DriveType") val driveType: String = "",
    @SerialName("FuelTypePrimary") val fuelTypePrimary: String = "",
    @SerialName("EngineCylinders") val engineCylinders: String = "",
    @SerialName("EngineHP") val engineHp: String = "",
    @SerialName("DisplacementL") val displacementL: String = "",
    @SerialName("TransmissionStyle") val transmissionStyle: String = "",
    @SerialName("VIN") val vin: String = "",
    @SerialName("ErrorCode") val errorCode: String = "",
    @SerialName("ErrorText") val errorText: String = "",
)

@Serializable
data class NhtsaGetAllMakesResponse(
    @SerialName("Count") val count: Int,
    @SerialName("Message") val message: String,
    @SerialName("Results") val results: List<NhtsaMakeResult>,
)

@Serializable
data class NhtsaMakeResult(
    @SerialName("Make_ID") val makeId: Int,
    @SerialName("Make_Name") val makeName: String,
)

@Serializable
data class NhtsaGetModelsResponse(
    @SerialName("Count") val count: Int,
    @SerialName("Message") val message: String,
    @SerialName("Results") val results: List<NhtsaModelResult>,
)

@Serializable
data class NhtsaModelResult(
    @SerialName("Make_ID") val makeId: Int,
    @SerialName("Make_Name") val makeName: String,
    @SerialName("Model_ID") val modelId: Int,
    @SerialName("Model_Name") val modelName: String,
)
