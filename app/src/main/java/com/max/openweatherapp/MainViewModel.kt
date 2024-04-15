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

class MainViewModel(private val repository: CityRepository) : ViewModel() {

    val _uiState: MutableStateFlow<MainActivityUiState> = MutableStateFlow(MainActivityUiState())
    val uiState: StateFlow<MainActivityUiState = _uiState.asStateFlow()

    fun getWeatherBroadcast(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
        try{
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
                    cityTemp = kelvinToCelsiusConverter(result.weatherParamsResponse.temp),
                    cityWindSpeed = "${result.windResponse.speed} М/С",
                )

                repository.insert(cityIntoDb)
                val allCity = repository.allCity()
                _uiState.update{
                    it.copy(allCity = allCity)
                }
            }catch(ioe: IOExceptions) {
                _uiState.update{
                    val message = getMessagesFromThrowable(ioe)
                    it.copy(errorMessage = message)
            }
        }
            Log.e("!!!", "Broadcast: $result")

        }
    }

    fun updateWeatherBroadcast() {
        viewModelScope.launch {
            val allCity = repository.allCity()
            allCity.forEach {city ->
               val result = WeatherApi.retrofitService.getBroadcast(city.cityLat, city.cityLon)

               city.cityTemp = kelvinToCelsiusConverter(result.weatherParamsResponse.temp),
               city.cityWindSpeed = "${result.windResponse.speed} М/С"
               
               repository.update(city)
               }
            }

            _uiState.update{
                it.copy(allCity = allCity)

        }

    }

    private suspend fun getCoordinatesOfCity(city: String): List<CoordinatesOfCityResponse> {
        return WeatherApi.retrofitService.getCoordinatesOfCity(
            city = city,
        )
    }

    fun deleteCityFromDb(cityId: Long) {
        viewModelScope.launch {
            val city = repository.getCityById((cityId))
            repository.delete(city)
        }
    }

    fun kelvinToCelsiusConverter(kelvinTemp): String {
        const val KELVIN_TO_CELSIUS = 273.15
        return "${(kelvinTemp - KELVIN_TO_CELSIUS).toUInt()} C"
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

