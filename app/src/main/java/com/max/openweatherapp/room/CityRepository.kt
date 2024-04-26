package com.max.openweatherapp.room

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

class CityRepository(private  val cityDao: CityDao) {
    
    suspend fun allCity(): List<City>{
        return  cityDao.getAlphabetizedCity()
    }
    suspend fun insert(city: City) {
        cityDao.insert(city)
    }

    suspend fun delete(city: City) {
        cityDao.delete(city)
    }

    suspend fun update(allCity:List<City>) {
        cityDao.update(allCity)
    }

    suspend fun getCityById(cityId: Long): City {
        return cityDao.getCityById(cityId)
    }


}