package com.max.openweatherapp.room

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
    abstract val cityDao: CityDao

    companion object {
        @Volatile
        private var INSTANCE: CityDatabase? = null
        fun getInstance(
            context: Context,
            scope: CoroutineScope
        ): CityDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CityDatabase::class.java,
                    "city_database"
                )
                    //.fallbackToDestructiveMigration()
                    .addCallback(CityDatabaseCallback(scope))
                    .build()
                Log.e("!!!", "Database created")
                INSTANCE = instance
                instance
            }
        }

        private class CityDatabaseCallback(
            private val scope: CoroutineScope
        ) : Callback() {
            //            override fun onCreate(db: SupportSQLiteDatabase) {
//                super.onCreate(db)
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { cityDatabase ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(cityDatabase.cityDao)
                    }
                }
            }
        }

        suspend fun populateDatabase(cityDao: CityDao) {
            cityDao.deleteAll()
            Log.e("!!!", "all City`s deleted")
        }
    }
}
