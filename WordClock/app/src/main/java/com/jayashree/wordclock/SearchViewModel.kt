package com.jayashree.wordclock

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.jayashree.wordclock.data.Location
import com.jayashree.wordclock.data.LocationContent
import com.jayashree.wordclock.data.LocationDatabase
import com.jayashree.wordclock.data.LocationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel(application: Application): AndroidViewModel(application) {

    private var auth: FirebaseAuth
    private var fireStore: FirebaseFirestore
    private var userId: FirebaseUser

    //private val repository: LocationRepository
    //private val allLocations: LiveData<List<Location>>

    init {
        //val locationDao = LocationDatabase.getDatabase(application, viewModelScope).locationDao()
        //repository = LocationRepository(locationDao)
        //allLocations = repository.allLocations

        fireStore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        //TODO handle null properly
        userId = auth.currentUser!!

    }

    fun insert(location: LocationContent) = viewModelScope.launch(Dispatchers.IO) {
        //repository.insert(location)

        //save in firestore

        val documentReference = fireStore.collection("locations").document(userId.uid).collection("my_timezones").document()
//         documentReference.collection(location.timezone_id).document().get()
//             .addOnSuccessListener {
//                 documentSnapshot -> if(documentSnapshot != null) {
//
//             }else{
//                    Log.v("DEBUG", "No Such Document. Adding to the dashboard")
//             }
//             }
//             .addOnFailureListener { exception ->
//                 Log.v("DEBUG", "get failed with ", exception)
//             }


        var cloudLocation = mutableMapOf<String, Any>()
        cloudLocation.put("timezone", location.timezone)
        cloudLocation.put("timezone_id", documentReference.id)

        documentReference.set(cloudLocation).addOnSuccessListener {
                ref -> Log.v("DEBUG", "Location added for user " + userId.toString())

        }.addOnFailureListener {
            Log.v("DEBUG", "Failed to add location to firestore")
        }
    }
}