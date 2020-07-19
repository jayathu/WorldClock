package com.jayashree.wordclock.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = arrayOf(Location::class), version = 2, exportSchema = false)
public abstract class LocationDatabase : RoomDatabase() {

    abstract fun locationDao(): LocationDao

    companion object {
        @Volatile
        private var INSTANCE: LocationDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): LocationDatabase {
            val tempInstance  = INSTANCE
            if(tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                context.applicationContext,
                LocationDatabase::class.java,
                "location_database"
                )
                    .addCallback(LocationDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                return instance
            }
        }

        private class LocationDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {


            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.locationDao())
                    }
                }
            }
        }


        suspend fun populateDatabase(locationDao: LocationDao) {

            //locationDao.deleteAll()
//
//            var location = Location("Hello")
//            locationDao.insert(location)
//            location = Location("World!")
//            locationDao.insert(location)
       }
    }
}