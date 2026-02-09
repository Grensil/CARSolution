package com.example.carsolution.data.api

import com.example.carsolution.data.api.dto.VehicleResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 국토교통부 자동차등록정보 조회 API
 *
 * 사용을 위해 data.go.kr에서 활용 신청 후 서비스키 발급 필요.
 * 실제 API 경로/파라미터는 data.go.kr 문서 참고하여 조정.
 */
interface VehicleApiService {

    @GET("1613000/RPT_BASE_MVEHI_INFO/CarRegistInfo")
    suspend fun getVehicleByPlateNumber(
        @Query("serviceKey") serviceKey: String,
        @Query("vhcleNo") plateNumber: String,
        @Query("type") type: String = "json",
    ): VehicleResponse
}
