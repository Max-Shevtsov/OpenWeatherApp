package com.max.openweatherapp.UI

import com.max.openweatherapp.room.City

data class MainActivityUiState(
    //var result: WeatherBroadcastResponse? = null,
    var errorMessage: String? = null,
    var isLoading: Boolean = false,
    var allCity: List<City> = emptyList(),
)
