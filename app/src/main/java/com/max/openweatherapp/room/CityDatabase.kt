package com.max.openweatherapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [City::class], version = 1, exportSchema = false)
abstract class CityDatabase : RoomDatabase() {
    abstract val cityDao: CityDao

    companion object {
        @Volatile
        private var INSTANCE: CityDatabase? = null
        fun getInstance(
            context: Context,
            scope: CoroutineScope
        ): CityDatabase {
            return INSTANCE?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CityDatabase::class.java,
                    "city_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(CityDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class CityDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.cityDao)
                    }
                }
            }
        }

        suspend fun populateDatabase(cityDao: CityDao) {

        }
    }
}
