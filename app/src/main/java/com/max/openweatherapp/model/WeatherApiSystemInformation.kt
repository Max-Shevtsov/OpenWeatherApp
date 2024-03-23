package com.max.openweatherapp.model

import com.google.gson.annotations.SerializedName

data class WeatherApiSystemInformation (
    @SerializedName("id")
    val id: Long,
    @SerializedName("weatherParamsResponse")
    val main: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("icon")
    val icon: String
)
