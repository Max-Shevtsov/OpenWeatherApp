package com.max.openweatherapp.data

import kotlinx.coroutines.flow.Flow

class FavoritesRepository(
    private val localDataSource: FavoriteCityDao
    private val networkDataSource: WeatherApiService  // пеерименовать в NetworkDataSource
) {

    

    fun cities(): Flow<List<City>> {
        return cityDao.getAlphabetizedCity()
    }

    suspend fun insert(city: City) {
        cityDao.insert(city)
    }

    suspend fun delete(city: City) {
        cityDao.delete(city)
    }

    suspend fun update(cities: List<City>) {
        cityDao.update(cities)
    }

    suspend fun updateCity(city: City) {
        cityDao.updateCity(city)
    }

    suspend fun getCityById(cityId: Long): City {
        return cityDao.getCityById(cityId)
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
        viewModelScope.async(Dispatchers.IO) {
            val weather = WeatherApi.retrofitService.getBroadcast(city.cityLat, city.cityLon)
            val updatedCity = city.copy(
                cityTemp = kelvinToCelsiusConverter(weather.weatherParamsResponse.temp),
                cityWindSpeed = "${weather.windResponse.speed} М/С",
                icon = weather.weatherTypeInformation.first().icon
            )
            localDataSource.updateCity(updatedCity)
        }
    }.forEach { it.await() }
}
        