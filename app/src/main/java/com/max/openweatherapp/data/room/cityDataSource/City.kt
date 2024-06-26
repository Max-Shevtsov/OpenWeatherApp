package com.max.openweatherapp.data.room.cityDataSource

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city_table")
data class City(
    @PrimaryKey(autoGenerate = false)
    val key: Long = 0L,
    @ColumnInfo(name = "city_id")
    val cityId: Long = 0L,
    @ColumnInfo(name = "city_name")
    val cityName: String = "",

    @ColumnInfo(name = "city_temperature")
    var cityTemp: String = "",

    @ColumnInfo(name = "city_wind_speed")
    var cityWindSpeed: String = "",

    @ColumnInfo(name = "city_weather_type_icon")
    var icon: String = "",

    @ColumnInfo(name = "city_lat")
    val cityLat: Double = 0.0,

    @ColumnInfo(name = "city_lon")
    val cityLon: Double = 0.0,

    @ColumnInfo(name = "city_is_starred")
    var isStarred: Boolean = false,

    )
