package com.max.openweatherapp


import android.util.Log
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.lang.IllegalArgumentException

class MainViewModel(private val repository: CityRepository) : ViewModel() {

    private val _favoritesUiState: MutableStateFlow<FavoritesUiState> =
        MutableStateFlow(FavoritesUiState())
    val favoritesUiState: StateFlow<FavoritesUiState> = _favoritesUiState.asStateFlow()

    private val _weatherUiState: MutableStateFlow<WeatherUiState> =
        MutableStateFlow(WeatherUiState())
    val weatherUiState: StateFlow<WeatherUiState> = _weatherUiState.asStateFlow()

    private var currentCity:City = City()

    init {
       Log.e("!!!", "run Init")
       updateWeatherBroadcast()
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

                val city = City(
                    cityName = city,
                    cityLat = coordinates.first().lat,
                    cityLon = coordinates.first().lon,
                    cityTemp = kelvinToCelsiusConverter(result.weatherParamsResponse.temp),
                    cityWindSpeed = "${result.windResponse.speed} М/С",
                )
                
                currentCity = city

                _weatherUiState.update {
                    it.copy(city = city)
                }

            } catch (e: IOException) {
                _weatherUiState.update {
                    val message = e.message
                    it.copy(errorMessage = message)
                }
            }
        }
    }

    fun putCityIntofavorites() {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                repository.insert(currentCity)
                val cities = repository.allCity()
                _favoritesUiState.update {
                    it.copy(allCity = cities)
                }
            } catch (e: IOException) {
                _favoritesUiState.update {
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
            try{
                allCity.forEach { city ->
                    _favoritesUiState.update {
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
    
                    _favoritesUiState.update {
                        it.copy(
                            allCity = updatedCities,
                            isLoading = false
                        )
                }
            } catch (e: IOException) {
                _favoritesUiState.update {
                    val message = e.message
                    it.copy(errorMessage = message)
                }
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
            _favoritesUiState.update {
                it.copy(allCity = allCity)
            }
        }
    }

    private fun kelvinToCelsiusConverter(kelvinTemp: Double): String {
        val KELVIN_TO_CELSIUS = 273.15
        return "${(kelvinTemp - KELVIN_TO_CELSIUS).toUInt()} C"
    }
    companion object{
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras:CreationExtras): T {
                if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                    val application = checkNotNull(extras[APPLICATION_KEY])
                    return MainViewModel((application as CityApplication).repository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel")
            }

        }
    }
}


