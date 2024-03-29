package com.max.openweatherapp.UI

import com.max.openweatherapp.room.City
import kotlinx.coroutines.flow.Flow

data class CityInDatabase(
    val city: List<City> = listOf<City>(City(cityId = 0, cityName = "london"))
)
