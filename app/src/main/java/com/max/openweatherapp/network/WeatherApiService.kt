package com.max.openweatherapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://api.openweathermap.org"

class WeatherApiService {

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    interface WeatherApiService {
        @GET("broadcast")
        fun getBroadcast(): String
    }

    object WeatherApi {
        var retrofitService : WeatherApiService by lazy {
            retrofit.create(WeatherApiService::class.java)
        }
    }
}

