package com.max.openweatherapp.model

import com.google.gson.annotations.SerializedName

data class BroadcastResponse(
    @SerializedName("temperature")
    val temperature: String
)
