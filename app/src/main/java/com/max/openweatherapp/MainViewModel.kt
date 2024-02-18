package com.max.openweatherapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.max.openweatherapp.model.Coord
import com.max.openweatherapp.model.GeocodingResponse
import com.max.openweatherapp.model.Main
import com.max.openweatherapp.model.ResultResponse
import com.max.openweatherapp.model.Sys
import com.max.openweatherapp.model.Wind
import com.max.openweatherapp.network.WeatherApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class MainViewModel : ViewModel() {
    private val defaultBroadcast: ResultResponse =
        ResultResponse(
            base = "nothing",
            weather = emptyList(),
            coord = Coord(10.99, 44.34),
            main = Main(1.0, 1.0, 1.1, 1.1, 1, 1, 1, 1),
            visibility = 1,
            wind = Wind(1.0, 1, 1.0),
            dt = 1,
            sys = Sys(1, 1, "RF", 1, 1),
            timezone = 1,
            id = 1,
            name = "1",
            cod = 1
        )

    private val _uiState: MutableStateFlow<ResultResponse> = MutableStateFlow(defaultBroadcast)
    val uiState: StateFlow<ResultResponse> = _uiState.asStateFlow()

    val isButtonClicked = MutableLiveData<Boolean>(false)

    val _gCity = MutableLiveData<String>(null) // не смог засеттить из UI

    //    val gCity: LiveData<String>
//        get() = _gCity
    private var lat = 10.34
    private var lon = 43.49

    init {


    }

    fun getResponseByClick() {
        if (isButtonClicked.value == true) {
            getWeatherBroadcast()
        }
    }

    private fun getWeatherBroadcast() {
        viewModelScope.launch(Dispatchers.IO) {

            Log.e("!!!", "Start loading")

            try {
                val gCoord = WeatherApi.retrofitService.getCoord(_gCity.value ?: "Kaliningrad")

                lat = gCoord.lat
                lon = gCoord.lon

                Log.e("!!!", "Coordinates:$gCoord")

                val result = WeatherApi.retrofitService.getBroadcast(lat, lon)
                _uiState.update { state ->
                    state.copy(
                        result.coord,
                        result.weather,
                        result.base,
                        result.main,
                        result.visibility,
                        result.wind,
                        result.dt,
                        result.sys,
                        result.timezone,
                        result.id,
                        result.name,
                        result.cod,
                    )
                }
                Log.e("!!!", "Broadcast: $result")
            } catch (e: IOException) {
            }


        }
    }
}