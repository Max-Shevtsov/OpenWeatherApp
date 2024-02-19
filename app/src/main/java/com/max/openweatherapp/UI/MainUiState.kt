package com.max.openweatherapp.UI

import com.google.gson.annotations.SerializedName


data class MainUiState(
    val temp: Double,
    val pressure: Long,
    val humidity: Long,
)
