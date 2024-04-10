package com.max.openweatherapp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.max.openweatherapp.UI.WeatherParams
import com.max.openweatherapp.UI.MainActivityUiState
import com.max.openweatherapp.UI.Wind
import com.max.openweatherapp.model.CoordinatesOfCityResponse
import com.max.openweatherapp.model.WeatherParamsResponse
import com.max.openweatherapp.model.WeatherBroadcastResponse
import com.max.openweatherapp.model.WindResponse
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
    private val defaultBroadcast: MainActivityUiState = MainActivityUiState(WeatherParams(), Wind(),)


    private val _uiState: MutableStateFlow<MainActivityUiState> = MutableStateFlow(defaultBroadcast)
    val uiState: StateFlow<MainActivityUiState> = _uiState.asStateFlow()

    fun getWeatherBroadcast(city: String) {
        viewModelScope.launch(Dispatchers.IO) {

            Log.e("!!!", "Start loading")

            try {
                val coordinates = getCoordinatesOfCity(city)
                Log.e("!!!", "Coordinates:$coordinates")

                val result = WeatherApi.retrofitService.getBroadcast(
                    coordinates.firstOrNull()?.lat,
                    coordinates.firstOrNull()?.lon,
                )

                Log.e("!!!", "City`s: $cityInDatabase")

                updateUiState(result)

                val cityIntoDb: City = City(
                    cityName = city, 
                    CityTemp = result.WeatherBroadcastResponse.weatherParamsResponse.temp,
                    cityWindSpeed = result.WeatherBroadcastResponse.windResponse.speed,)

                insert(cityIntoDb)

                Log.e("!!!", "Broadcast: $result")
            } catch (e: IOException) {

            }
        }
    }

    private suspend fun getCoordinatesOfCity(city: String): List<CoordinatesOfCityResponse> {
        return WeatherApi.retrofitService.getCoordinatesOfCity(
            city = city,
        )
    }

    fun insert(city: City) {
        repository.insert(city)
    }

    private fun updateUiState(result: WeatherBroadcastResponse) {
        val uiState = mapResultResponse(src = result)
            _uiState.update { state ->
                state.copy(
                    main = uiState.main,
                    wind = uiState.wind,
                )
            }
    }
    private fun mapResultResponse(
        src: WeatherBroadcastResponse,
        mainMapper: (WeatherParamsResponse) -> WeatherParams = ::mapMainResponse,
        windMapper: (WindResponse) -> Wind = ::mapWindResponse,
        
    ) = MainActivityUiState(
        mainMapper.invoke(src.weatherParamsResponse),
        windMapper.invoke(src.windResponse),
    )

    private fun mapMainResponse(weatherParamsResponse: WeatherParamsResponse) =
        WeatherParams(
            weatherParamsResponse.temp,
            weatherParamsResponse.pressure,
            weatherParamsResponse.humidity
        )

    private fun mapWindResponse(windResponse: WindResponse) =
        Wind(
            windResponse.speed,
            windResponse.deg,
            windResponse.gust
        )
}

class MainViewModelFactory(private val repository: CityRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}

data class UiState(
    var result: WeatherBroadcastResponse? = null,
    var errorMessage: String? = null,
    var isLoading: Boolean = true
)