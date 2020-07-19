package com.jayashree.wordclock.data

import androidx.lifecycle.LiveData

class LocationRepository(private val locationDao: LocationDao) {

    val allLocations: LiveData<List<Location>> = locationDao.getLocations()

    suspend fun insert(location: Location) {
        locationDao.insert(location)
    }
}