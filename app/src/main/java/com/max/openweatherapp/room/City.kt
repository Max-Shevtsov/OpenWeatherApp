package com.max.openweatherapp.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city_table")
data class City(
    @PrimaryKey(autoGenerate = true)
    var cityId: Long = 0L,

    @ColumnInfo(name = "city_name")
    var cityName: String = ""

    @ColumnInfo(name = "city_lat")
    var cityLat: String = ""

    @ColumnInfo(name = "city_lon")
    var cityLon: String = ""

    @ColumnInfo(name = "city_temp")
    var cityTemp: String = ""

    @ColumnInfo(name = "city_wind_speed")
    var cityWindSpeed: String = ""
)
