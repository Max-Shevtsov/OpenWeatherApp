package com.max.openweatherapp.data.network

import com.max.openweatherapp.model.CoordinatesOfCityResponse
import com.max.openweatherapp.model.WeatherBroadcastResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
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
        @Query("lat") lat: Double? = 10.99,
        @Query("lon") lon: Double? = 44.34,
        @Query("appid") apiId: String? = "33b8f58fa9d36a34c79c1415a9e34827",
    ): WeatherBroadcastResponse

    @GET("/geo/1.0/direct")
    suspend fun getCoordinatesOfCity(
        @Query("q") city: String?,
        @Query("limit") limit: Int? = DEFAULT_LIMIT,
        @Query("appid") apiId: String? = "33b8f58fa9d36a34c79c1415a9e34827",
    ):List<CoordinatesOfCityResponse>
}


