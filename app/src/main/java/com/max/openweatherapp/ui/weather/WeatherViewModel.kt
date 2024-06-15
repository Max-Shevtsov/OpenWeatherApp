package com.max.openweatherapp.ui.weather

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.max.openweatherapp.ui.WeatherUiState
import com.max.openweatherapp.data.CityRepository
import com.max.openweatherapp.data.FavoritesRepository
import com.max.openweatherapp.data.network.WeatherApi
import com.max.openweatherapp.data.room.cityDataSource.City
import com.max.openweatherapp.data.room.cityDataSource.CityDatabase
import com.max.openweatherapp.data.room.favoritesDataSource.FavoriteCityDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException


class WeatherViewModel(
    private val cityRepository: CityRepository,
    private val favoritesRepository: FavoritesRepository,
) : ViewModel() {

    private val _weatherUiState: MutableStateFlow<WeatherUiState> =
        MutableStateFlow(WeatherUiState())
    val weatherUiState: StateFlow<WeatherUiState> = _weatherUiState.asStateFlow()

    fun getWeatherBroadcast(city: String) {
        viewModelScope.launch {
            try {
                val currentCity = cityRepository.getWeatherBroadcast(city)

                if (cityRepository.databaseisEmpty()) {
                    cityRepository.insert(currentCity)
                } else {
                    cityRepository.update(currentCity)
                }

                updateWeatherBroadcast(currentCity)

            } catch (e: IOException) {
                _weatherUiState.update {
                    val message = e.message
                    it.copy(errorMessage = message)
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

    fun putCityIntoFavorites() {
        viewModelScope.launch(Dispatchers.Default) {
            val city = (_weatherUiState.value.city) ?: return@launch
            city.isStarred = true
            _weatherUiState.update {
                it.copy(
                    city = city,
                )
            }
            favoritesRepository.insert(city)
        }
    }

    fun deleteCityFromFavorites() {
        viewModelScope.launch(Dispatchers.Default) {
            val city = (_weatherUiState.value.city) ?: return@launch
            city.isStarred = false
            favoritesRepository.delete(city)
        }
    }

    companion object {
        fun createFactory(context: Context): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {

                        val cityRepository = CityRepository(
                            localDataSource = CityDatabase.getInstance(context).cityDao(),
                            networkDataSource = WeatherApi.retrofitService,
                        )

                        val favoritesRepository = FavoritesRepository(
                            localDataSource = FavoriteCityDatabase.getInstance(context)
                                .favoriteCityDao(),
                            networkDataSource = WeatherApi.retrofitService
                        )

                        return WeatherViewModel(
                            cityRepository,
                            favoritesRepository
                        ) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel")
                }
            }
        }
    }
}