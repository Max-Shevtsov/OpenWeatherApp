package com.max.openweatherapp.data.room.cityDataSource

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.max.openweatherapp.data.room.favoritesDataSource.FavoriteCity
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(city: City)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(city: City)

//    @Query("DELETE FROM city_table WHERE cityId= :cityId")
//    suspend fun deleteById(cityId: Long)

    @Query("SELECT * FROM city_table")
    fun get(): Flow<List<City>>
    
    @Query("SELECT (SELECT COUNT(*) FROM city_table) == 0")
    suspend fun databaseIsEmpty(): Boolean

}