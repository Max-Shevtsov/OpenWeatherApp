package com.max.openweatherapp.data

import android.util.Log
import com.max.openweatherapp.data.network.WeatherApiService
import com.max.openweatherapp.data.room.cityDataSource.City
import com.max.openweatherapp.data.room.cityDataSource.CityDao
import kelvinToCelsiusConverter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class CityRepository(
    private val localDataSource: CityDao,
    private val networkDataSource: WeatherApiService,  // пеерименовать в NetworkDataSource
) {
    suspend fun insert(city: City) {
        localDataSource.insert(city)
    }

    private suspend fun update(city: City) {
        localDataSource.update(city)
    }

//    private suspend fun deleteById(cityId:Long) {
//        localDataSource.deleteById(cityId)
//    }

    fun get(): Flow<City> {
        return localDataSource.get()
    }
    private suspend fun databaseIsEmpty():Boolean {
       return localDataSource.databaseIsEmpty()
    }

    suspend fun putInDatabase(city:City) {
        if (databaseIsEmpty()) {
            Log.e("!!!", "${databaseIsEmpty()}")
            insert(city)
        } else {
            update(city)
        }
    }

    suspend fun searchWeatherByCity(city: String) {

        Log.e("!!!", "Start loading")

        val coordinates = networkDataSource.getCoordinatesOfCity(city)
        Log.e("!!!", "Coordinates:$coordinates")

        val result = networkDataSource.getBroadcast(
            coordinates.firstOrNull()?.lat,
            coordinates.firstOrNull()?.lon,
        )

        Log.e("!!!", "City`s: $city")

        val updatedCityId = get().firstOrNull()?.cityId?.plus(1)

        val updatedCity = City(
            key = 0L,
            cityId = updatedCityId?:0,
            cityName = city,
            cityLat = coordinates.first().lat,
            cityLon = coordinates.first().lon,
            cityTemp = kelvinToCelsiusConverter(result.weatherParamsResponse.temp),
            cityWindSpeed = "${result.windResponse.speed} М/С",
            icon = result.weatherTypeInformation.first().icon,
        )
        putInDatabase(updatedCity)
    }

}