package com.max.openweatherapp.room

import androidx.annotation.WorkerThread

class CityRepository(private  val cityDao: CityDao) {

    val allCity = cityDao.getAlphabetizedCity()
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(city: City) {
        cityDao.insert(city)
    }


}