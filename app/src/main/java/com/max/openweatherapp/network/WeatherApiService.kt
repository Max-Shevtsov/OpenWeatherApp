package com.max.openweatherapp.network

import com.max.openweatherapp.model.GeocodingResponse
import com.max.openweatherapp.model.ResultResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://api.openweathermap.org"
const val DEFAULT_LIMIT = 1
private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface WeatherApiService {

    @GET("/data/2.5/weather")
    suspend fun getBroadcast(
        @Query("lat") lat: Double?,
        @Query("lon") lon: Double?,
        @Query("appid") apiId: String? = "33b8f58fa9d36a34c79c1415a9e34827",
    ): ResultResponse

    @GET("/geo/1.0/direct")
    suspend fun getCoord(
        @Query("q") gCity: String?,
        @Query("limit") limit: Int? = DEFAULT_LIMIT,
        @Query("appid") apiId: String? = "33b8f58fa9d36a34c79c1415a9e34827",
    ):List<GeocodingResponse>
}

object WeatherApi {
    val retrofitService: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}

