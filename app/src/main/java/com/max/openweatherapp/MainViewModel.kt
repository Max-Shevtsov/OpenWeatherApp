package com.max.openweatherapp

import android.util.Log
import androidx.lifecycle.asLiveData
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.max.openweatherapp.UI.WeatherParams
import com.max.openweatherapp.UI.MainActivityUiState
import com.max.openweatherapp.UI.Wind
import com.max.openweatherapp.model.CoordinatesOfCityResponse
import com.max.openweatherapp.model.WeatherParamsResponse
import com.max.openweatherapp.model.WeatherBroadcastResponse
import com.max.openweatherapp.model.WindResponse
import com.max.openweatherapp.network.WeatherApi
import com.max.openweatherapp.room.City
import com.max.openweatherapp.room.CityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.lang.IllegalArgumentException
const val KELVIN_TO_CELSIUS = 273.15

class MainViewModel(private val repository: CityRepository) : ViewModel() {

    val uiState: LiveData<List<City>> = repository.allCity.asLiveData()

    fun getWeatherBroadcast(city: String) {
        viewModelScope.launch(Dispatchers.IO) {

            Log.e("!!!", "Start loading")


            val coordinates = getCoordinatesOfCity(city)
            Log.e("!!!", "Coordinates:$coordinates")

            val result = WeatherApi.retrofitService.getBroadcast(
                coordinates.firstOrNull()?.lat,
                coordinates.firstOrNull()?.lon,
            )

            Log.e("!!!", "City`s: $city")

            viewModelScope.launch {
                val cityIntoDb = City(
                    cityName = city,
                    cityLat = coordinates.first().lat,
                    cityLon = coordinates.first().lon,
                    cityTemp = "${(result.weatherParamsResponse.temp - KELVIN_TO_CELSIUS).toUInt()} C",
                    cityWindSpeed = "${result.windResponse.speed} М/С",
                )

                insert(cityIntoDb)
            }


            Log.e("!!!", "Broadcast: $result")

        }
    }

//    fun updateWeatherBroadcast() {
//        viewModelScope.launch {
//            val city = repository.allCity
//
//            city.forEach {city ->
//               WeatherApi.retrofitService.getBroadcast(city.cityLat, city.cityLon)
//            }
//        }
//
//    }

    private suspend fun getCoordinatesOfCity(city: String): List<CoordinatesOfCityResponse> {
        return WeatherApi.retrofitService.getCoordinatesOfCity(
            city = city,
        )
    }

    suspend fun insert(city: City) {
        repository.insert(city)
    }

    fun deleteCityFromDb(cityId: Long) {
        viewModelScope.launch {
            val city = repository.getCityById((cityId))
            repository.delete(city)
        }
    }
}

class MainViewModelFactory(private val repository: CityRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}

data class UiState(
    var result: WeatherBroadcastResponse? = null,
    var errorMessage: String? = null,
    var isLoading: Boolean = true
)