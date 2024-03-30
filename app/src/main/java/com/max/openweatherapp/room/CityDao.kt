package com.max.openweatherapp.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {
    @Insert
    suspend fun insert(city: City)

    @Update
    suspend fun update(city: City)

    @Delete
    suspend fun delete(city: City)

    @Query("SELECT * FROM city_table ORDER BY city_name ASC")
    suspend fun getAlphabetizedCity(): List<City>

    @Query("DELETE FROM city_table")
    suspend fun deleteAll()
}