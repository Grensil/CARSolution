package com.grensil.carinfo.data.api

import com.grensil.carinfo.data.api.dto.NhtsaDecodeVinResponse
import com.grensil.carinfo.data.api.dto.NhtsaGetAllMakesResponse
import com.grensil.carinfo.data.api.dto.NhtsaGetModelsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NhtsaApiService {
    @GET("vehicles/DecodeVinValues/{vin}")
    suspend fun decodeVin(
        @Path("vin") vin: String,
        @Query("format") format: String = "json",
    ): NhtsaDecodeVinResponse

    @GET("vehicles/GetAllMakes")
    suspend fun getAllMakes(
        @Query("format") format: String = "json",
    ): NhtsaGetAllMakesResponse

    @GET("vehicles/GetModelsForMake/{make}")
    suspend fun getModelsForMake(
        @Path("make") make: String,
        @Query("format") format: String = "json",
    ): NhtsaGetModelsResponse
}
