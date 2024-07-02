package com.max.openweatherapp.ui.favorites

import com.max.openweatherapp.data.room.favoritesDataSource.FavoriteCity


data class FavoritesUiState(
    //var result: WeatherBroadcastResponse? = null,
    var errorMessage: String? = null,
    var isLoading: Boolean = false,
    var allCity: List<FavoriteCity> = emptyList(),
)