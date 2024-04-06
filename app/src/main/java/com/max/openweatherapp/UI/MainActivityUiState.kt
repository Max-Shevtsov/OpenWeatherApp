package com.max.openweatherapp.UI

import com.max.openweatherapp.room.City

data class MainActivityUiState(
    val main: WeatherParams,
    val wind: Wind,
    var city: List<City> = emptyList(),
)
