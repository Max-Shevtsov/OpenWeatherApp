package com.max.openweatherapp.model

import com.google.gson.annotations.SerializedName

data class CoordinatesResponse (
    @SerializedName ("lon")
    val lon: Double,
    @SerializedName ("lat")
    val lat: Double
)