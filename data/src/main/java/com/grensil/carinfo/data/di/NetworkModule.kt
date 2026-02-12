package com.grensil.carinfo.data.di

import com.grensil.carinfo.data.BuildConfig
import com.grensil.carinfo.data.api.NhtsaApiService
import com.grensil.carinfo.data.api.OpinetApiService
import com.grensil.carinfo.data.api.VehicleApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val TIMEOUT_SECONDS = 30L
    private const val DATA_GO_KR_SERVICE_KEY = ""

    @Provides
    @Singleton
    fun provideJson(): Json =
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient
            .Builder()
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                },
            ).build()

    @Provides
    @Singleton
    @Named("dataGoKr")
    fun provideDataGoKrRetrofit(
        okHttpClient: OkHttpClient,
        json: Json,
    ): Retrofit =
        Retrofit
            .Builder()
            .baseUrl("https://apis.data.go.kr/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

    @Provides
    @Singleton
    @Named("nhtsa")
    fun provideNhtsaRetrofit(
        okHttpClient: OkHttpClient,
        json: Json,
    ): Retrofit =
        Retrofit
            .Builder()
            .baseUrl("https://vpic.nhtsa.dot.gov/api/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

    @Provides
    @Singleton
    @Named("opinet")
    fun provideOpinetRetrofit(
        okHttpClient: OkHttpClient,
        json: Json,
    ): Retrofit =
        Retrofit
            .Builder()
            .baseUrl("https://www.opinet.co.kr/api/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

    @Provides
    @Singleton
    fun provideVehicleApiService(
        @Named("dataGoKr") retrofit: Retrofit,
    ): VehicleApiService = retrofit.create(VehicleApiService::class.java)

    @Provides
    @Singleton
    fun provideNhtsaApiService(
        @Named("nhtsa") retrofit: Retrofit,
    ): NhtsaApiService = retrofit.create(NhtsaApiService::class.java)

    @Provides
    @Singleton
    fun provideOpinetApiService(
        @Named("opinet") retrofit: Retrofit,
    ): OpinetApiService = retrofit.create(OpinetApiService::class.java)

    @Provides
    @Named("dataGoKrServiceKey")
    fun provideServiceKey(): String = DATA_GO_KR_SERVICE_KEY

    @Provides
    @Named("opinetApiKey")
    fun provideOpinetApiKey(): String = BuildConfig.OPINET_API_KEY
}
