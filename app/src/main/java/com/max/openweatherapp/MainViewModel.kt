package com.max.openweatherapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.max.openweatherapp.UI.MainUiState
import com.max.openweatherapp.UI.ResultUiState
import com.max.openweatherapp.UI.WindUiState
import com.max.openweatherapp.model.Coord
import com.max.openweatherapp.model.GeocodingResponse
import com.max.openweatherapp.model.Main
import com.max.openweatherapp.model.ResultResponse
import com.max.openweatherapp.model.Sys
import com.max.openweatherapp.model.Wind
import com.max.openweatherapp.network.WeatherApi
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class MainViewModel : ViewModel() {
    private val appid = "33b8f58fa9d36a34c79c1415a9e34827"
    private val defaultBroadcast: MainUiState =
        MainUiState(
            temp = 0.0,
            pressure = 1,
            humidity = 1
        )


    private val _uiState: MutableStateFlow<MainUiState> = MutableStateFlow(defaultBroadcast)
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun getWeatherBroadcast(city:String) {
        viewModelScope.launch(Dispatchers.IO) {

            Log.e("!!!", "Start loading")

            try {
                val gCoord: Deferred<List<GeocodingResponse>> = async { getCoord(city) }

                Log.e("!!!", "Coordinates:$gCoord")

                val result = WeatherApi.retrofitService.getBroadcast(
                    gCoord.await().firstOrNull()?.lat,
                    gCoord.await().firstOrNull()?.lon,
                    appid
                )

                val mainUiState = mapMainResponse(result.main)
                _uiState.update { state ->
                    state.copy(
                        mainUiState.temp,
                        mainUiState.humidity,
                        mainUiState.pressure
                    )
                }
                Log.e("!!!", "Broadcast: $result")
            } catch (e: IOException) {
            }
        }
    }

    private suspend fun getCoord(city: String) =
        WeatherApi.retrofitService.getCoord(
            gCity = city?: "Kaliningrad",
            1,
            appid
        )

//    private fun mapResultResponse(
//        mainMapper: (Main) -> MainUiState = ::mapMainResponse,
//        windMapper: (Wind) -> WindUiState = ::mapWindResponse
//    ) = ResultUiState(MainUiState(), WindUiState())

    private fun mapMainResponse(main: Main) =
        MainUiState(main.temp, main.pressure, main.humidity)

    private fun mapWindResponse(wind: Wind) =
        WindUiState(wind.speed, wind.deg, wind.gust)
}

data class UiState(
    var result: ResultResponse? = null,
    var errorMessage: String? = null,
    var isLoading: Boolean = true
)