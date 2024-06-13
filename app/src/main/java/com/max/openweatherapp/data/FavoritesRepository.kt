package com.max.openweatherapp.data

import com.max.openweatherapp.data.network.WeatherApiService
import com.max.openweatherapp.data.room.cityDataSource.City
import com.max.openweatherapp.data.room.favoritesDataSource.FavoriteCityDao
import kelvinToCelsiusConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow

class FavoritesRepository(
    private val localDataSource: FavoriteCityDao,
    private val networkDataSource: WeatherApiService,  // пеерименовать в NetworkDataSource
) {

    fun cities(): Flow<List<City>> {
        return localDataSource.getAlphabetizedCity()
    }

    suspend fun insert(city: City) {
        localDataSource.insert(city)
    }

    suspend fun delete(city: City) {
        localDataSource.delete(city)
    }

    suspend fun updateCity(city: City) {
        localDataSource.updateCity(city)
    }

    suspend fun getCityById(cityId: Long): City {
        return localDataSource.getCityById(cityId)
    }

    suspend fun updateCitiesWeather(cities: List<City>) {
        //        val updateWeatherJobs = mutableListOf<Deferred<Unit>>()
        //        cities.forEach { city ->
        //            val updatedWeatherResult = viewModelScope.async(Dispatchers.IO) {
        //                val weather = WeatherApi.retrofitService.getBroadcast(city.cityLat, city.cityLon)
        //                val updatedCity = city.copy(
        //                    cityTemp = kelvinToCelsiusConverter(weather.weatherParamsResponse.temp),
        //                    cityWindSpeed = "${weather.windResponse.speed} М/С",
        //                )
        //                repository.updateCity(updatedCity)
        //            }
        //            updateWeatherJobs.add(updatedWeatherResult)
        //        }
        //        updateWeatherJobs.forEach { it.await() }

        cities.map { city ->
            CoroutineScope(Dispatchers.IO).async {
                val weather = networkDataSource.getBroadcast(city.cityLat, city.cityLon)
                val updatedCity = city.copy(
                    cityTemp = kelvinToCelsiusConverter(weather.weatherParamsResponse.temp),
                    cityWindSpeed = "${weather.windResponse.speed} М/С",
                    icon = weather.weatherTypeInformation.first().icon
                )
                updateCity(updatedCity)
            }
        }.forEach { it.await() }
    }
}
        