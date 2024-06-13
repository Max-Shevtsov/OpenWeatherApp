package com.max.openweatherapp


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.max.openweatherapp.room.CityRepository
import java.lang.IllegalArgumentException


class MainViewModel(
    private val cityRepository: CityRepository,
    ) : ViewModel() {

    


    fun getWeatherBroadcast(city: String) {
        cityRepository.getWeatherBroadcast(city)
    }




    companion object {
        fun getFactory(context:Context):ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
                        val cityRepository = CityRepository(
                            localDataSource = CityDatabase.getInstance(context),
                            networdataSource = WeatherApi.retrofitService
                        )
                        return WeatherViewModel(
                            cityRepository,
                        ) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel")
                }
            }
        } 
    }
}