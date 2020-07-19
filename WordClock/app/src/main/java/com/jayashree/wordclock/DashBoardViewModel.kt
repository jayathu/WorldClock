package com.jayashree.wordclock

import android.app.Application
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jayashree.wordclock.data.Location
import com.jayashree.wordclock.data.LocationDatabase
import com.jayashree.wordclock.data.LocationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DashBoardViewModel(application: Application): AndroidViewModel(application) {

    private lateinit var auth: FirebaseAuth
    private lateinit var fireStore: FirebaseFirestore

    private val repository: LocationRepository
    var allLocations: MutableLiveData<List<Location>> = MutableLiveData<List<Location>>()
    lateinit var userId: FirebaseUser

    init {
        val locationDao = LocationDatabase.getDatabase(application, viewModelScope).locationDao()
        repository = LocationRepository(locationDao)
        //allLocations = repository.allLocations

        fireStore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        //TODO handle null properly
        userId = auth.currentUser!!

        getQuery()
        listenToLocationUpdates()

    }

    fun getQuery() : CollectionReference {
        return fireStore.collection("locations")
    }

    fun listenToLocationUpdates() {
        fireStore.collection("locations").addSnapshotListener(EventListener() { documentSnapshots, e ->
            if (e != null) {
                Log.e("DEBUG", "Listen failed!", e)
                return@EventListener
            }

            if(documentSnapshots != null) {
                val allLocationsFirestore = ArrayList<Location>()
                val documents = documentSnapshots.documents
                documents.forEach {
                    val location = it.toObject(Location::class.java)
                    if(location != null) {
                        Log.v("DEBUG", "Location: " + location)
                        allLocationsFirestore.add(location)
                    }
                }
                allLocations.value = allLocationsFirestore
            }

        })
    }

}