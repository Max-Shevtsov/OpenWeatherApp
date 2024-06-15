package com.max.openweatherapp.data

import android.util.Log
import com.max.openweatherapp.data.network.WeatherApi
import com.max.openweatherapp.data.network.WeatherApiService
import com.max.openweatherapp.data.room.cityDataSource.City
import com.max.openweatherapp.data.room.cityDataSource.CityDao
import com.max.openweatherapp.data.room.favoritesDataSource.FavoriteCityDao
import kelvinToCelsiusConverter
import kotlinx.coroutines.flow.Flow

class CityRepository(
    private val localDataSource: CityDao,
    private val networkDataSource: WeatherApiService,  // пеерименовать в NetworkDataSource
) {
    suspend fun insert(city: City) {
        localDataSource.insert(city)
    }

    suspend fun delete(city: City) {
        localDataSource.delete(city)
    }

    suspend fun update(city: City) {
        localDataSource.update(city)
    }

    suspend fun databaseisEmpty():Boolean {
       return localDataSource.databaseIsEmpty()
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