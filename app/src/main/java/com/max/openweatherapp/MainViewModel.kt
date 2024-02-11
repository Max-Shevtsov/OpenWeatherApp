package com.max.openweatherapp

import androidx.lifecycle.ViewModel
import com.max.openweatherapp.network.WeatherApi

class MainViewModel : ViewModel {

    private fun getWeatherBroadcast() {
        ViewModelScope.launch {
            val result = WeatherApi.retrofitService.getBroadcast()
        }
    }
}