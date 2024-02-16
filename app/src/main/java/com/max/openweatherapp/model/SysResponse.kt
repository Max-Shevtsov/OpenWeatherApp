package com.max.openweatherapp.model

import com.google.gson.annotations.SerializedName

data class SysResponse(
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
