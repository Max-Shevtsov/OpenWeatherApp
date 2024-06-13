package com.max.openweatherapp


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.max.openweatherapp.room.CityRepository
import java.lang.IllegalArgumentException


class viewModel(
    private val cityRepository: CityRepository,
    ) : ViewModel() {

    


    fun getWeatherBroadcast(city: String) {
        cityRepository.getWeatherBroadcast(city)
    }




    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                if (modelClass.isAssignableFrom(viewModel::class.java)) {
                    val application = checkNotNull(extras[APPLICATION_KEY])
                    return viewModel((application as CityApplication).repository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel")
            }

        }
    }
}