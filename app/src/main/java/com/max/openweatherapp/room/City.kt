package com.max.openweatherapp.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city_table")
data class City(
    @PrimaryKey(autoGenerate = true)
    val cityId: Long = 0L,

    @ColumnInfo(name = "city_name")
    val cityName: String = "",

    @ColumnInfo(name = "city_temperature")
    var cityTemp: String = "",

    @ColumnInfo(name = "city_wind_speed")
    var cityWindSpeed: String = "",

    @ColumnInfo(name = "city_lat")
    val cityLat: Double = 0.0,

    @ColumnInfo(name = "city_lon")
    val cityLon: Double = 0.0,


)
