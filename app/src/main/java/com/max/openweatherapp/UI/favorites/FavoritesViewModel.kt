package com.max.openweatherapp.UI.favorites

class FavoritesViewModel(
    private val favoritesRepository: FavoritesRepository,
) {
    private val _favoritesUiState: MutableStateFlow<FavoritesUiState> =
        MutableStateFlow(FavoritesUiState())
    val favoritesUiState: StateFlow<FavoritesUiState> = _favoritesUiState.asStateFlow()

    init {
        Log.e("!!!", "run Init")
        updateFavoritesBroadcast()
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


    private fun updateFavoritesBroadcast() {
        viewModelScope.launch(Dispatchers.IO) {
            favoritesRepository.cities().collect { cities ->
                _favoritesUiState.update {
                    it.copy(
                        allCity = cities,
                    )
                }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
                    val favoritesRepository = FavoritesRepository()
                    return MainViewModel(
                        favoritesRepository,
                    ) as T
                }
                throw IllegalArgumentException("Unknown ViewModel")
            }

        }
    }

}