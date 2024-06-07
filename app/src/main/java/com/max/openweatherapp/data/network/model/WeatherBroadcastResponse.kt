package com.max.openweatherapp.data.network.model

import com.google.gson.annotations.SerializedName

data class WeatherBroadcastResponse(
    @SerializedName("coord")
    val coordinates: CoordinatesResponse,
    @SerializedName("weather")
    val weatherTypeInformation: List<WeatherTypeInformation>,
    @SerializedName("base")
    val base: String,
    @SerializedName("main")
    val weatherParamsResponse: WeatherParamsResponse,
    @SerializedName("visibility")
    val visibility: Long,
    @SerializedName("wind")
    val windResponse: WindResponse,
    @SerializedName("dt")
    val dt: Long,
    @SerializedName("sys")
    val sunsetTimeResponse: SunsetTimeResponse,
    @SerializedName("timezone")
    val timezone: Long,
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("cod")
    val cod: Long
) {}