package com.max.openweatherapp.UI.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.max.openweatherapp.data.CityRepository
import com.max.openweatherapp.data.FavoritesRepository


class WeatherViewModel(
    private val cityRepository: CityRepository,
    private val favoritesRepository: FavoritesRepository,
    ) {
    private val _weatherUiState: MutableStateFlow<WeatherUiState> =
        MutableStateFlow(WeatherUiState())
    val weatherUiState: StateFlow<WeatherUiState> = _weatherUiState.asStateFlow()

    fun getWeatherBroadcast(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentCity = cityRepository.getWeatherBroadcast(city)
                
                if (cityRepository.databaseIsEmpty) {
                    cityRepository.insert(currentCity)
                } else {
                    cityRepository.update(currentCity)
                }
                
                updateWeatherBroadcast(cityRepository.)

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
        fun getFactory(context:Context):ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {

                        val cityRepository = CityRepository(
                            localDataSource = CityDatabase.getInstance(context),
                            networdataSource = WeatherApi.retrofitService
                        )

                        val favoritesRepository = FavoritesRepository(
                            localDataSource = FavoriteCityDatabase.getInstance(context),
                            networdataSource = WeatherApi.retrofitService
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