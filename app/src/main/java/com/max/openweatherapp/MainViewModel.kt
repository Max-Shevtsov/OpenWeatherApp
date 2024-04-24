package com.max.openweatherapp


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.max.openweatherapp.UI.MainActivityUiState
import com.max.openweatherapp.model.CoordinatesOfCityResponse
import com.max.openweatherapp.network.WeatherApi
import com.max.openweatherapp.room.City
import com.max.openweatherapp.room.CityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.lang.IllegalArgumentException

class MainViewModel(private val repository: CityRepository) : ViewModel() {

    private val _uiState: MutableStateFlow<MainActivityUiState> =
        MutableStateFlow(MainActivityUiState())
    val uiState: StateFlow<MainActivityUiState> = _uiState.asStateFlow()

    init {
       Log.e("!!!", "run Init")
       // updateWeatherBroadcast()
    }

    fun getWeatherBroadcast(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.e("!!!", "Start loading")

                val coordinates = getCoordinatesOfCity(city)
                Log.e("!!!", "Coordinates:$coordinates")

                val result = WeatherApi.retrofitService.getBroadcast(
                    coordinates.firstOrNull()?.lat,
                    coordinates.firstOrNull()?.lon,
                )

                Log.e("!!!", "City`s: $city")

                val cityIntoDb = City(
                    cityName = city,
                    cityLat = coordinates.first().lat,
                    cityLon = coordinates.first().lon,
                    cityTemp = kelvinToCelsiusConverter(result.weatherParamsResponse.temp),
                    cityWindSpeed = "${result.windResponse.speed} М/С",
                )

                repository.insert(cityIntoDb)
                val cities = repository.allCity()
                _uiState.update {
                    it.copy(allCity = cities)
                }
            } catch (e: IOException) {
                _uiState.update {
                    val message = e.message
                    it.copy(errorMessage = message)
                }
            }
        }
    }

    fun updateWeatherBroadcast() {
        viewModelScope.launch(Dispatchers.IO) {
            val allCity = repository.allCity()
            val updatedCities = mutableListOf<City>()
        
            allCity.forEach { city ->
                _uiState.update {
                    it.copy(isLoading = true)
                }
                val result = WeatherApi.retrofitService.getBroadcast(city.cityLat, city.cityLon)
                    val updatedCity = city.copy(
                        cityTemp = kelvinToCelsiusConverter(result.weatherParamsResponse.temp),
                        cityWindSpeed = "${result.windResponse.speed} М/С",
                    )
                    updatedCities.add(updatedCity)
                }

                repository.update(updatedCities)

                _uiState.update {
                    it.copy(
                        allCity = updatedCities,
                        isLoading = false
                    )
            }
        }
    }

    private suspend fun getCoordinatesOfCity(city: String): List<CoordinatesOfCityResponse> {
        return WeatherApi.retrofitService.getCoordinatesOfCity(
            city = city,
        )
    }

    fun deleteCityFromDb(cityId: Long) {
        viewModelScope.launch(Dispatchers.Default) {
            val city = repository.getCityById((cityId))
            repository.delete(city)
            val allCity = repository.allCity()
            _uiState.update {
                it.copy(allCity = allCity)
            }
        }
    }

    private fun kelvinToCelsiusConverter(kelvinTemp: Double): String {
        val KELVIN_TO_CELSIUS = 273.15
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

