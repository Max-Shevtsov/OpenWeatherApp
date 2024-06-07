package com.max.openweatherapp.UI.weather


class WeatherViewModel(private val cityRepository: CityRepository) {
    private val _weatherUiState: MutableStateFlow<WeatherUiState> =
        MutableStateFlow(WeatherUiState())
    val weatherUiState: StateFlow<WeatherUiState> = _weatherUiState.asStateFlow()

    fun getWeatherBroadcast(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                cityRepository.insert(
                    cityRepository.getWeatherBroadcast(city)
                )
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

}