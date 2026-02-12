package com.grensil.carinfo.data.api

import com.grensil.carinfo.data.api.dto.OpinetAvgAllPriceResponse
import com.grensil.carinfo.data.api.dto.OpinetLowTop10Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpinetApiService {
    @GET("avgAllPrice.do")
    suspend fun getAverageAllPrices(
        @Query("out") format: String = "json",
        @Query("code") apiKey: String,
    ): OpinetAvgAllPriceResponse

    @GET("lowTop10.do")
    suspend fun getLowTop10(
        @Query("out") format: String = "json",
        @Query("code") apiKey: String,
        @Query("prodcd") productCode: String,
        @Query("area") areaCode: String = "",
    ): OpinetLowTop10Response
}
