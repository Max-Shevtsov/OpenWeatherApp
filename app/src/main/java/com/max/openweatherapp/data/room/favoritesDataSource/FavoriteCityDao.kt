package com.max.openweatherapp.data.room.favoritesDataSource

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.max.openweatherapp.data.room.cityDataSource.City
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteCityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(city: FavoriteCity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCity(cities: FavoriteCity )

    @Delete
    suspend fun delete(city: FavoriteCity)

    @Query("SELECT * FROM favorite_city_table WHERE name= :name")
    suspend fun getCityById(name: String): FavoriteCity

    @Query("SELECT * FROM favorite_city_table ORDER BY name ASC")
    fun getAlphabetizedCity(): Flow<List<FavoriteCity>>

    @Query("DELETE FROM favorite_city_table")
    suspend fun deleteAll()
}