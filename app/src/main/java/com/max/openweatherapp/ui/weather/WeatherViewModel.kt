package com.max.openweatherapp.ui.weather

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.max.openweatherapp.data.CityRepository
import com.max.openweatherapp.data.FavoritesRepository
import com.max.openweatherapp.data.mappers.toFavoriteCity
import com.max.openweatherapp.data.network.WeatherApi
import com.max.openweatherapp.data.room.cityDataSource.CityDatabase
import com.max.openweatherapp.data.room.favoritesDataSource.FavoriteCityDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class WeatherViewModel(
    private val cityRepository: CityRepository,
    private val favoritesRepository: FavoritesRepository,
) : ViewModel() {

    private val _weatherUiState: MutableStateFlow<WeatherUiState> =
        MutableStateFlow(WeatherUiState())
    val weatherUiState: StateFlow<WeatherUiState> = _weatherUiState.asStateFlow()

    init {
        updateWeatherBroadcast()
    }


    private fun updateWeatherBroadcast() {
        viewModelScope.launch(Dispatchers.Default) {
            cityRepository.getCity().collect { city ->
                Log.e("!!!", "$city")
                _weatherUiState.update {
                    it.copy(city = city.firstOrNull())
                }
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

            favoritesRepository.insert(city.toFavoriteCity())
        }
    }

    fun deleteCityFromFavorites() {
        viewModelScope.launch(Dispatchers.Default) {
            val city = (_weatherUiState.value.city) ?: return@launch
            city.isStarred = false
            favoritesRepository.delete(city.toFavoriteCity())
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