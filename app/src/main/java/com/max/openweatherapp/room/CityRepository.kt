package com.max.openweatherapp.room

import androidx.annotation.WorkerThread

class CityRepository(private  val cityDao: CityDao) {

    suspend fun allCity(): List<City>{
        return  cityDao.getAlphabetizedCity()
    }
    //@Suppress("RedundantSuspendModifier")
    //@WorkerThread
    suspend fun insert(city: City) {
        cityDao.insert(city)
    }

    suspend fun delete(city: City) {
        cityDao.delete(city)
    }

    suspendfun getCityById(cityId: Int) {
        cityDao.getcityById(cityId)
    }


}