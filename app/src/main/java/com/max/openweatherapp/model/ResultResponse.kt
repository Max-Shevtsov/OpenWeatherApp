package com.max.openweatherapp.model

import com.google.gson.annotations.SerializedName

data class ResultResponse (
//    @SerializedName("coord")
//    val coord: String? = null,
    @SerializedName("weather")
    val weather: List<Weather> //,
//    @SerializedName("base")
//    val base: String,
//    @SerializedName("main")
//    val main: String? = null,
//    @SerializedName("visibility")
//    val visibility: Long,
//    @SerializedName("wind")
//    val wind: String? = null,
//    @SerializedName("clouds")
//    val clouds: String? = null,
//    @SerializedName("dt")
//    val dt: Long,
//    @SerializedName("sys")
//    val sys: String? = null,
//    @SerializedName("timezone")
//    val timezone: Long,
//    @SerializedName("id")
//    val id: Long,
//    @SerializedName("name")
//    val name: String,
//    @SerializedName("cod")
//    val cod: Long
) {}