package com.max.openweatherapp.room

import kotlinx.coroutines.flow.Flow

class CityRepository(private val cityDao: CityDao) {

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


}