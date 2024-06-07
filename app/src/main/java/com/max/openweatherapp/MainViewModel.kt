package com.max.openweatherapp


import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.max.openweatherapp.UI.FavoritesUiState
import com.max.openweatherapp.UI.WeatherUiState
import com.max.openweatherapp.model.CoordinatesOfCityResponse
import com.max.openweatherapp.network.WeatherApi
import com.max.openweatherapp.room.City
import com.max.openweatherapp.room.CityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.lang.IllegalArgumentException
import coil.load


class MainViewModel(
    private val cityRepository: CityRepository,
    ) : ViewModel() {

    


    fun getWeatherBroadcast(city: String) {
        cityRepository.getWeatherBroadcast(city)
    }




    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                    val application = checkNotNull(extras[APPLICATION_KEY])
                    return MainViewModel((application as CityApplication).repository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel")
            }

        }
    }
}