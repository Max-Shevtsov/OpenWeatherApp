package com.max.openweatherapp.data.room.favoritesDataSource

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [FavoriteCity::class], version = 1, exportSchema = false)
abstract class FavoriteCityDatabase : RoomDatabase() {
    abstract fun favoriteCityDao(): FavoriteCityDao

    companion object {
        @Volatile
        private var INSTANCE: FavoriteCityDatabase? = null
        fun getInstance(context: Context): FavoriteCityDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteCityDatabase::class.java,
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
