package com.max.openweatherapp.data.room.favoritesDataSource

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.max.openweatherapp.data.network.model.CoordinatesResponse
import com.max.openweatherapp.data.network.model.WeatherBroadcastResponse
import com.max.openweatherapp.data.network.model.WeatherParamsResponse
import com.max.openweatherapp.data.network.model.WindResponse

@Entity(tableName = "favorite_city_table")
data class FavoriteCity(
    @PrimaryKey(autoGenerate = false)
    val name: String,
    @Embedded(prefix = "coordinates_")
    val coordinates: CoordinatesResponse,
//    @ColumnInfo(name = "city_base")
//    val base: String,
    @Embedded(prefix = "weatherParams_")
    val weatherParams: WeatherParamsResponse,
//    @ColumnInfo(name = "city_visibility")
//    val visibility: Long,
    @Embedded(prefix = "wind_")
    val wind: WindResponse,
//    @ColumnInfo(name = "city_dt")
//    val dt: Long,
//    @Embedded(prefix = "sunset")
//    val sunsetTimeResponse: SunsetTimeResponse,
//    @ColumnInfo(name = "city_timezone")
//    val timezone: Long,
//    @ColumnInfo(name = "city_api_id")
//    val id: Long,

//    @ColumnInfo(name = "city_cod")
//    val cod: Long,
    @ColumnInfo(name = "city_icon")
    val icon: String?,
    @ColumnInfo(name = "city_is_starred")
    var isStarred: Boolean = false,
    )
