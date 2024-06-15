package com.max.openweatherapp.ui.favorites

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.max.openweatherapp.ui.FavoritesUiState
import com.max.openweatherapp.data.FavoritesRepository
import com.max.openweatherapp.data.network.WeatherApi
import com.max.openweatherapp.data.room.favoritesDataSource.FavoriteCityDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class FavoritesViewModel(
    private val favoritesRepository: FavoritesRepository,
) : ViewModel() {
    private val _favoritesUiState: MutableStateFlow<FavoritesUiState> =
        MutableStateFlow(FavoritesUiState())
    val favoritesUiState: StateFlow<FavoritesUiState> = _favoritesUiState.asStateFlow()

    init {
        Log.e("!!!", "run Init")
        updateFavoritesBroadcast()
    }

    fun refreshWeather() {
        viewModelScope.launch {
            try {
                _favoritesUiState.update {
                    it.copy(isLoading = true)
                }
                favoritesRepository.updateCitiesWeather(_favoritesUiState.value.allCity)
                _favoritesUiState.update {
                    it.copy(isLoading = false)
                }
            } catch (e: IOException) {
                _favoritesUiState.update {
                    val message = e.message
                    it.copy(errorMessage = message)
                }
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
        fun createFactory(context: Context):ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
                        val favoritesRepository = FavoritesRepository(
                            localDataSource = FavoriteCityDatabase.getInstance(context).favoriteCityDao(),
                            networkDataSource = WeatherApi.retrofitService
                        )
                        return FavoritesViewModel(
                            favoritesRepository,
                        ) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel")
                }
            }
        }

//        val Factory: ViewModelProvider.Factory =
//            object : ViewModelProvider.Factory {
//                override fun <T : ViewModel> create(
//                    modelClass: Class<T>,
//                    extras: CreationExtras
//                ): T {
//                    if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
//                        val application = checkNotNull(extras[APPLICATION_KEY])
//                        val favoritesRepository = FavoritesRepository(
//                            localDataSource = FavoriteCityDatabase.getInstance(application.applicationContext)
//                                .favoriteCityDao(),
//                            networkDataSource = WeatherApi.retrofitService
//                        )
//                        return FavoritesViewModel(
//                            favoritesRepository = favoritesRepository
//                        ) as T
//                    }
//                    throw IllegalArgumentException("Unknown ViewModel")
//                }
//            }
    }

}