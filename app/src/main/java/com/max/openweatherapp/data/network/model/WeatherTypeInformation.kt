package com.max.openweatherapp.data.network.model

import com.google.gson.annotations.SerializedName

data class WeatherTypeInformation (
    @SerializedName("id")
    val id: Long,
    @SerializedName("main")
    val weatherType: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("icon")
    val icon: String

)
