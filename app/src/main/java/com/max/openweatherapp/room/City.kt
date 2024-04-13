package com.max.openweatherapp.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city_table")
data class City(
    @PrimaryKey(autoGenerate = true)
    var cityId: Long = 0L,

    @ColumnInfo(name = "city_name")
    var cityName: String = "",

    @ColumnInfo(name = "city_temperature")
    var cityTemp: Double = 0.0,

    @ColumnInfo(name = "city_wind_speed")
    var cityWindSpeed: Double = 0.0,

    @ColumnInfo(name = "city_lat")
    var cityLat: Double = 0.0,

    @ColumnInfo(name = "city_lon")
    var cityLon: Double = 0.0,


)
