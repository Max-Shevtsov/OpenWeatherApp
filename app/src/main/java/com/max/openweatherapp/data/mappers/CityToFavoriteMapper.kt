package com.max.openweatherapp.data.mappers

import com.max.openweatherapp.data.room.cityDataSource.City
import com.max.openweatherapp.data.room.favoritesDataSource.FavoriteCity
// mapper (easy to test)
//class CityToFavoriteMapper : (City) -> FavoriteCity {
//
//    override fun invoke(city: City): FavoriteCity {
//        return FavoriteCity(
//            cityId = city.cityId,
//            cityName = city.cityName,
//            cityTemp = city.cityTemp,
//            cityWindSpeed = city.cityWindSpeed,
//            cityLat = city.cityLat,
//            cityLon = city.cityLon,
//            icon = city.icon,
//        )
//    }
//}
//extention static fun
fun City.toFavoriteCity(): FavoriteCity {
    return FavoriteCity(
        cityId = cityId,
        coordinates = coordinates,
        weatherParams = weatherParams,
        wind = wind,
        name = name,
        icon = icon,
        isStarred = isStarred,
    )
}