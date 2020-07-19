package com.jayashree.wordclock.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LocationDao {

    @Query("SELECT * from location_table")
    fun getLocations(): LiveData<List<Location>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(location: Location)

    @Query("DELETE FROM location_table where timezone = :timezone")
    suspend fun delete(timezone: String)

    @Query("DELETE FROM location_table")
    suspend fun deleteAll()
}