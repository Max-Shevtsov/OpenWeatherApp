package com.max.openweatherapp.data

import kotlinx.coroutines.flow.Flow

class CityRepository(
    private val localDataSource: CityDao
    private val networkDataSource: WeatherApiService  // пеерименовать в NetworkDataSource
) {



    suspend fun getCityById(cityId: Long): City {
        return cityDao.getCityById(cityId)
    }

    suspend fun getWeatherBroadcast(city: String): City {
        Log.e("!!!", "Start loading")

        val coordinates = networkDataSource.getCoordinatesOfCity(city)
        Log.e("!!!", "Coordinates:$coordinates")

        val result = networkDataSource.getBroadcast(
            coordinates.firstOrNull()?.lat,
            coordinates.firstOrNull()?.lon,
        )

        Log.e("!!!", "City`s: $city")

        val city = City(
            cityName = city,
            cityLat = coordinates.first().lat,
            cityLon = coordinates.first().lon,
            cityTemp = kelvinToCelsiusConverter(result.weatherParamsResponse.temp),
            cityWindSpeed = "${result.windResponse.speed} М/С",
            icon = result.weatherTypeInformation.first().icon,
        )
        return city
    }
}