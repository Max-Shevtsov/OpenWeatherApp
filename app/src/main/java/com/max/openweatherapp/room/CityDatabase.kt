package com.max.openweatherapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [City::class], version = 1, exportSchema = false)
abstract class CityDatabase : RoomDatabase() {
    abstract val cityDao: CityDao

    companion object {
        @Volatile
        private var INSTANCE: CityDatabase? = null
        fun getInstance(context: Context): CityDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CityDatabase::class.java,
                        "city_database"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
