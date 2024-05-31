package com.max.openweatherapp


import android.content.Context
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
//import com.bumptech.glide.Glide
import com.max.openweatherapp.UI.FavoritesUiState
import com.max.openweatherapp.UI.WeatherType
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


class MainViewModel(private val repository: CityRepository) : ViewModel() {

    private val _favoritesUiState: MutableStateFlow<FavoritesUiState> =
        MutableStateFlow(FavoritesUiState())
    val favoritesUiState: StateFlow<FavoritesUiState> = _favoritesUiState.asStateFlow()

    private val _weatherUiState: MutableStateFlow<WeatherUiState> =
        MutableStateFlow(WeatherUiState())
    val weatherUiState: StateFlow<WeatherUiState> = _weatherUiState.asStateFlow()

    private var currentCity: City = City()

    init {
        Log.e("!!!", "run Init")
        updateFavoritesBroadcast()
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
                    cityWeatherType = result.weatherTypeInformation.first().icon,
                )

                currentCity = city

                updateWeatherBroadcast(currentCity)

            } catch (e: IOException) {
                _weatherUiState.update {
                    val message = e.message
                    it.copy(errorMessage = message)
                }
            }
        }
    }


    fun refreshWeather() {
        viewModelScope.launch {
            _favoritesUiState.update {
                it.copy(isLoading = true)
            }
            updateCitiesWeather(_favoritesUiState.value.allCity)
            _favoritesUiState.update {
                it.copy(isLoading = false)
            }
        }
    }

    private suspend fun updateCitiesWeather(cities: List<City>) {
//        val updateWeatherJobs = mutableListOf<Deferred<Unit>>()
//        cities.forEach { city ->
//            val updatedWeatherResult = viewModelScope.async(Dispatchers.IO) {
//                val weather = WeatherApi.retrofitService.getBroadcast(city.cityLat, city.cityLon)
//                val updatedCity = city.copy(
//                    cityTemp = kelvinToCelsiusConverter(weather.weatherParamsResponse.temp),
//                    cityWindSpeed = "${weather.windResponse.speed} М/С",
//                )
//                repository.updateCity(updatedCity)
//            }
//            updateWeatherJobs.add(updatedWeatherResult)
//        }
//        updateWeatherJobs.forEach { it.await() }

        cities.map { city ->
            viewModelScope.async(Dispatchers.IO) {
                val weather = WeatherApi.retrofitService.getBroadcast(city.cityLat, city.cityLon)
                val updatedCity = city.copy(
                    cityTemp = kelvinToCelsiusConverter(weather.weatherParamsResponse.temp),
                    cityWindSpeed = "${weather.windResponse.speed} М/С",
                )
                repository.updateCity(updatedCity)
            }
        }.forEach { it.await() }
    }

    private fun updateFavoritesBroadcast() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.cities().collect { cities ->
                _favoritesUiState.update {
                    it.copy(
                        allCity = cities,
                    )
                }
            }
        }
    }

    fun updateWeatherBroadcast(city: City) {
        viewModelScope.launch {
            _weatherUiState.update {
                it.copy(city = city)
            }
        }
    }

    private suspend fun getCoordinatesOfCity(city: String): List<CoordinatesOfCityResponse> {
        return WeatherApi.retrofitService.getCoordinatesOfCity(
            city = city,
        )
    }

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

    

    fun loadWeatherTypePicture(url: String, uiItem: ImageView) {
        // не поддерживает корутины, заменил на Coil
        //Glide
        //    .with(context)
        //    .load(url)
        //    .into(uiItem)
        uiItem.load(url)
        Log.e("!!!", "url:$url")
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