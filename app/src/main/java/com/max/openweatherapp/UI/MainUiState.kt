package com.max.openweatherapp.UI

import com.google.gson.annotations.SerializedName


data class MainUiState(
    val temp: Double = 0.0,
    val pressure: Long = 1L,
    val humidity: Long = 1L,
)
