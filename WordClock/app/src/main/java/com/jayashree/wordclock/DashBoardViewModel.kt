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
    var editable: Boolean = false
    private val TAG = "TAG-CLOCK"

    private val repository: LocationRepository
    var allLocationContent: MutableLiveData<List<LocationContent>> = MutableLiveData()
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

    }

    fun getQuery(): Query {

        var query: Query = fireStore.collection("locations").whereEqualTo("author", userId.uid)

        fireStore.collection("users").document(userId.uid).addSnapshotListener(
            EventListener() {
                    snapshot, e -> if(e != null) {
                Log.e(TAG, "Listen failed!")
                return@EventListener
            }
                if(snapshot != null){
                    val role = snapshot.getString("role")
                    if(role.equals("admin")){
                        query = fireStore.collection("locations")
                    }
                    listenToLocationUpdates(query)
                }
            })
        return query
    }

    private fun listenToLocationUpdates(query: Query) {
        query.addSnapshotListener(EventListener() { documentSnapshots, e ->
            if (e != null) {
                Log.e(TAG, "Listen failed!", e)
                return@EventListener
            }

            if(documentSnapshots != null) {
                val tempContents = ArrayList<LocationContent>()
                val documents = documentSnapshots.documents
                documents.forEach {
                    val location = it.toObject(Location::class.java)
                    val content = LocationContent(location!!.timezone_id, location.timezone, editable)
                    tempContents.add(content)
                }
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
        fireStore.collection("locations").document(location.timezone_id).delete()
            .addOnSuccessListener {
                    ref -> Log.v(TAG, "Location deleted" )
            }
            .addOnFailureListener {
                    ref -> Log.v(TAG, "Failed to delete")
            }
    }

}