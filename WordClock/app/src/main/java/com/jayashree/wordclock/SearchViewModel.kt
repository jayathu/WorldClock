package com.jayashree.wordclock

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.jayashree.wordclock.data.Location
import com.jayashree.wordclock.data.LocationDatabase
import com.jayashree.wordclock.data.LocationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel(application: Application): AndroidViewModel(application) {

    private val repository: LocationRepository
    private val allLocations: LiveData<List<Location>>


    init {
        val locationDao = LocationDatabase.getDatabase(application, viewModelScope).locationDao()
        repository = LocationRepository(locationDao)
        allLocations = repository.allLocations
    }

    fun insert(location: Location) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(location)
    }
}