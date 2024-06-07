package com.max.openweatherapp.data.room.favoritesDataSource

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteCityDao {
    @Insert
    suspend fun insert(city: City)

    

    @Update
    suspend fun updateCity(cities: City )

    @Delete
    suspend fun delete(city: City)

    @Query("SELECT * FROM city_table WHERE cityId= :cityId")
    suspend fun getCityById(cityId: Long): City

    @Query("SELECT * FROM city_table ORDER BY city_name ASC")
    fun getAlphabetizedCity(): Flow<List<City>>

    @Query("DELETE FROM city_table")
    suspend fun deleteAll()
}