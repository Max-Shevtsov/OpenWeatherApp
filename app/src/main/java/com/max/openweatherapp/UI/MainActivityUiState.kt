package com.max.openweatherapp.UI

import com.max.openweatherapp.room.City

data class MainActivityUiState(
    val main: WeatherParams,
    val wind: Wind,
    val cityInDatabase: List<City>,
)
