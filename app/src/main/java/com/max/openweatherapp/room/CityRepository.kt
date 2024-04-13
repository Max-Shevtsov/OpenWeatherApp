package com.max.openweatherapp.room

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class CityRepository(private  val cityDao: CityDao) {
    val allCity: Flow<List<City>> = cityDao.getAlphabetizedCity()
//    suspend fun allCity(): List<City>{
//        return  cityDao.getAlphabetizedCity()
//    }
    suspend fun insert(city: City) {
        cityDao.insert(city)
    }

    suspend fun delete(city: City) {
        cityDao.delete(city)
    }

    suspend fun getCityById(cityId: Long): City {
        return cityDao.getCityById(cityId)
    }


}