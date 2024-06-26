package com.max.openweatherapp.data.network.model

import com.google.gson.annotations.SerializedName

data class SunsetTimeResponse(
    @SerializedName("type")
    val type: Long,
    @SerializedName("id")
    val id: Long,
    @SerializedName("country")
    val country: String,
    @SerializedName("sunrise")
    val sunrise: Long,
    @SerializedName("sunset")
    val sunset: Long
)
