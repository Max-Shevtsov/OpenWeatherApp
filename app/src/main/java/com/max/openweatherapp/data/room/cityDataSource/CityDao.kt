package com.max.openweatherapp.data.room.cityDataSource

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
    
    @Query("SELECT (SELECT COUNT(*) FROM city_table) == 0")
    suspend fun databaseIsEmpty(): Boolean

    @Delete
    suspend fun delete(city: City)
}