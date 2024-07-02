package com.max.openweatherapp.data.network.model

import com.google.gson.annotations.SerializedName

data class WindResponse(
    @SerializedName("speed")
    val speed: Double,
    @SerializedName("deg")
    val deg: Long,
    @SerializedName("gust")
    val gust: Double
)
