package com.max.openweatherapp.model

import com.google.gson.annotations.SerializedName

data class WeatherBroadcastResponse(
    @SerializedName("coord")
    val coordinates: CoordinatesResponse,
    @SerializedName("weatherApiSystemInformation")
    val weatherApiSystemInformation: List<WeatherApiSystemInformation>,
    @SerializedName("base")
    val base: String,
    @SerializedName("weatherParamsResponse")
    val weatherParamsResponse: WeatherParamsResponse,
    @SerializedName("visibility")
    val visibility: Long,
    @SerializedName("windResponse")
    val windResponse: WindResponse,
    @SerializedName("dt")
    val dt: Long,
    @SerializedName("sunsetTimeResponse")
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