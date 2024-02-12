package com.max.openweatherapp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.max.openweatherapp.model.ResultResponse
import com.max.openweatherapp.network.WeatherApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class MainViewModel : ViewModel() {
    private val defaultBroadcast: ResultResponse = ResultResponse(weather = emptyList())

    private val _uiState: MutableStateFlow<ResultResponse> = MutableStateFlow(defaultBroadcast)
    val uiState: StateFlow<ResultResponse> = _uiState.asStateFlow()

    init {
        getWeatherBroadcast()
    }

    private fun getWeatherBroadcast() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.e("!!!", "Start loading")
            try {
                val result = WeatherApi.retrofitService.getBroadcast()
                _uiState.update { state ->
                    state.copy(result.weather)
                }
                Log.e("!!!", "Broadcast: $result")
            } catch (e: IOException) {
            }


        }
    }
}