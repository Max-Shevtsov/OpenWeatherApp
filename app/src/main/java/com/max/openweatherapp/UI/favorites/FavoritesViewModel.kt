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
}