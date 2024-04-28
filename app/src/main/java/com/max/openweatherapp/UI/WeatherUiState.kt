package com.max.openweatherapp.UI

import com.max.openweatherapp.room.City

data class WeatherUiState(
    //var result: WeatherBroadcastResponse? = null,
    var errorMessage: String? = null,
    var isLoading: Boolean = false,
    var city: City? = null,
)