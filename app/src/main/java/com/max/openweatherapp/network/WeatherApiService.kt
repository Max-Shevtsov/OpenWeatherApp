package com.max.openweatherapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://api.openweathermap.org"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface WeatherApiService {
    @GET("data/3.0/onecall?lat=33.44&lon=-94.04&exclude=hourly,daily&appid=33b8f58fa9d36a34c79c1415a9e34827")
    fun getBroadcast(): String
}

object WeatherApi {
    val retrofitService: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}

