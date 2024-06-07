package com.max.openweatherapp.data.network.model

import com.google.gson.annotations.SerializedName

data class CloudsResponse(
    @SerializedName("all")
    val all: Long
)