package com.max.openweatherapp.data.room.cityDataSource

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [City::class], version = 1, exportSchema = false)
abstract class CityDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao
    companion object {
        @Volatile
        private var INSTANCE: CityDatabase? = null
        fun getInstance(context: Context): CityDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CityDatabase::class.java,
                    "city_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                Log.e("!!!", "Database created")
                INSTANCE = instance
                instance
            }
        }
    }
}
