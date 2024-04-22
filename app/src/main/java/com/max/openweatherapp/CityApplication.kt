package com.max.openweatherapp

import android.app.Application
import com.max.openweatherapp.room.CityDatabase
import com.max.openweatherapp.room.CityRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class CityApplication: Application() {

    val database by lazy {CityDatabase.getInstance(this)}
    val repository by lazy {CityRepository(database.cityDao)}
}