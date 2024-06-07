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

    




    



    fun putCityIntoFavorites() {
        viewModelScope.launch(Dispatchers.Default) {
            val city = (_weatherUiState.value.city) ?: return@launch
            city.isStarred = true
            _weatherUiState.update {
                it.copy(
                    city = city,
                )
            }
            repository.insert(city)
        }
    }

    fun deleteCityFromFavorites() {
        viewModelScope.launch(Dispatchers.Default) {
            //val city = _favoritesUiState.value.allCity.firstOrNull { it.cityId == currentCity.cityId } ?: return@launch
            val city = (_weatherUiState.value.city) ?: return@launch
            city.isStarred = false
            repository.delete(city)
        }
    }

    



    private fun kelvinToCelsiusConverter(kelvinTemp: Double): String {
        val KELVIN_TO_CELSIUS = 273.15
        return "${(kelvinTemp - KELVIN_TO_CELSIUS).toUInt()} C"
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