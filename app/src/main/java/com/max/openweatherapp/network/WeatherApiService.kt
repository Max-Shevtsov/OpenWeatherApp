package com.max.openweatherapp.network

import com.max.openweatherapp.model.ResultResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://api.openweathermap.org"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface WeatherApiService {
    @GET("/data/2.5/weather?lat=44.34&lon=10.99&appid=33b8f58fa9d36a34c79c1415a9e34827")
    suspend fun getBroadcast(): ResultResponse
}

object WeatherApi {
    val retrofitService: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}

