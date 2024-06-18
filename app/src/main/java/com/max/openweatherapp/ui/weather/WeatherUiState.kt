package com.max.openweatherapp.ui.weather

import com.max.openweatherapp.data.room.cityDataSource.City

data class WeatherUiState(
    //var result: WeatherBroadcastResponse? = null,
    var errorMessage: String? = null,
    var isLoading: Boolean = false,
    var city: City? = null,
)