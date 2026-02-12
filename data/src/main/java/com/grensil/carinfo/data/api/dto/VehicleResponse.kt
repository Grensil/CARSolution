package com.grensil.carinfo.data.api.dto

import com.grensil.carinfo.domain.model.Vehicle
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 국토교통부 자동차등록정보 API 응답 DTO.
 * 실제 API 응답 구조에 맞게 조정 필요 — 아래는 예시 구조.
 */
@Serializable
data class VehicleResponse(
    val response: ResponseBody,
)

@Serializable
data class ResponseBody(
    val header: ResponseHeader,
    val body: ResponseItems,
)

@Serializable
data class ResponseHeader(
    val resultCode: String,
    val resultMsg: String,
)

@Serializable
data class ResponseItems(
    val items: List<VehicleDto>,
    val totalCount: Int = 0,
)

@Serializable
data class VehicleDto(
    @SerialName("vhcleNo") val plateNumber: String = "",
    @SerialName("vhcleNm") val name: String = "",
    @SerialName("mnfctrNm") val manufacturer: String = "",
    @SerialName("mdlYear") val year: Int = 0,
    @SerialName("fuelNm") val fuelType: String = "",
    @SerialName("dsplvl") val displacement: Int = 0,
) {
    fun toDomain(): Vehicle =
        Vehicle(
            id = "v-${plateNumber.hashCode()}",
            plateNumber = plateNumber,
            name = name,
            manufacturer = manufacturer,
            year = year,
            fuelType = fuelType,
            displacement = displacement,
        )
}
