package com.max.openweatherapp

import android.util.Log
import androidx.lifecycle.ViewModel
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
import com.max.openweatherapp.room.CityDao
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class MainViewModel(val dao: CityDao) : ViewModel() {
    private val defaultBroadcast: MainActivityUiState = MainActivityUiState(WeatherParams(), Wind())


    private val _uiState: MutableStateFlow<MainActivityUiState> = MutableStateFlow(defaultBroadcast)
    val uiState: StateFlow<MainActivityUiState> = _uiState.asStateFlow()

    fun getWeatherBroadcast(city: String) {
        viewModelScope.launch(Dispatchers.IO) {

            Log.e("!!!", "Start loading")

            try {
                val coordinates: Deferred<List<CoordinatesOfCityResponse>> =
                    async { getCoordinatesOfCity(city) }

                Log.e("!!!", "Coordinates:$coordinates")

                val result = WeatherApi.retrofitService.getBroadcast(
                    coordinates.await().firstOrNull()?.lat,
                    coordinates.await().firstOrNull()?.lon,
                )

                val uiState = mapResultResponse(result)
                _uiState.update { state ->
                    state.copy(
                        main = uiState.main,
                        wind = uiState.wind
                    )
                }
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

    fun addCityToDatabase(city: String) {
        viewModelScope.launch(Dispatchers.Default) {
            val cityIntoDatabase = City()
            cityIntoDatabase.cityName = city
            dao.insert(cityIntoDatabase)
        }
    }

    private fun mapResultResponse(
        src: WeatherBroadcastResponse,
        mainMapper: (WeatherParamsResponse) -> WeatherParams = ::mapMainResponse,
        windMapper: (WindResponse) -> Wind = ::mapWindResponse
    ) = MainActivityUiState(
        mainMapper.invoke(src.weatherParamsResponse),
        windMapper.invoke(src.windResponse)
    )

    private fun mapMainResponse(weatherParamsResponse: WeatherParamsResponse) =
        WeatherParams(
            weatherParamsResponse.temp,
            weatherParamsResponse.pressure,
            weatherParamsResponse.humidity
        )

    private fun mapWindResponse(windResponse: WindResponse) =
        Wind(windResponse.speed, windResponse.deg, windResponse.gust)
}

data class UiState(
    var result: WeatherBroadcastResponse? = null,
    var errorMessage: String? = null,
    var isLoading: Boolean = true
)