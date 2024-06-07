package com.max.openweatherapp.data.network.model

import com.google.gson.annotations.SerializedName

data class CoordinatesResponse (
    @SerializedName ("lon")
    val lon: Double,
    @SerializedName ("lat")
    val lat: Double
)