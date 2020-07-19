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
import com.jayashree.wordclock.data.LocationContent
import com.jayashree.wordclock.data.LocationDatabase
import com.jayashree.wordclock.data.LocationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DashBoardViewModel(application: Application): AndroidViewModel(application) {

    private lateinit var auth: FirebaseAuth
    private lateinit var fireStore: FirebaseFirestore
    var editable: Boolean = false;

    private val repository: LocationRepository
    var allLocations: MutableLiveData<List<Location>> = MutableLiveData<List<Location>>()
    var allLocationContent: MutableLiveData<List<LocationContent>> = MutableLiveData<List<LocationContent>>()
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
        return fireStore.collection("locations").document(userId.uid).collection("my_timezones")
    }

    fun listenToLocationUpdates() {
        fireStore.collection("locations").document(userId.uid).collection("my_timezones").addSnapshotListener(EventListener() { documentSnapshots, e ->
            if (e != null) {
                Log.e("DEBUG", "Listen failed!", e)
                return@EventListener
            }

            if(documentSnapshots != null) {
                val allLocationsFirestore = ArrayList<Location>()
                val tempContents = ArrayList<LocationContent>()
                val documents = documentSnapshots.documents
                documents.forEach {
                    val location = it.toObject(Location::class.java)
                    val content = LocationContent(location!!.timezone, false)
                    if(location != null) {
                        Log.v("DEBUG", "Location: " + location)
                        allLocationsFirestore.add(location)
                        tempContents.add(content)
                    }
                }
                allLocations.value = allLocationsFirestore
                allLocationContent.value = tempContents
            }

        })
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }

    fun deleteLocation(location: LocationContent) = viewModelScope.launch(Dispatchers.IO) {
        //repository.insert(location)

        //save in firestore
        val documentReference = fireStore.collection("locations").document(userId.uid).collection("my_timezones")
            .document(location.timezone).delete()
            .addOnSuccessListener {
                    ref -> Log.v("DEBUG", "Location deleted" )
            }
            .addOnFailureListener {
                    ref -> Log.v("DEBUG", "Failed to delete")
            }
    }

}