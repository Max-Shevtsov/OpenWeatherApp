package com.max.openweatherapp


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.max.openweatherapp.ui.weather.WeatherViewModel
import com.max.openweatherapp.data.CityRepository
import com.max.openweatherapp.data.network.WeatherApi
import com.max.openweatherapp.data.room.cityDataSource.CityDatabase
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException


class MainViewModel(
    private val cityRepository: CityRepository,
) : ViewModel() {


    fun getWeatherBroadcast(city: String) {
        viewModelScope.launch {
            cityRepository.getWeatherBroadcast(city)
        }
    }


    companion object {
        fun createFactory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
                        val cityRepository = CityRepository(
                            localDataSource = CityDatabase.getInstance(context).cityDao(),
                            networkDataSource = WeatherApi.retrofitService
                        )
                        return MainViewModel(
                            cityRepository,
                        ) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel")
                }
            }
    }
}