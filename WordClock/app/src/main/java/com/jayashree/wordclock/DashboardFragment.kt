package com.jayashree.wordclock

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.jayashree.wordclock.data.Location
import com.jayashree.wordclock.data.LocationViewHolder
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*
import kotlinx.android.synthetic.main.fragment_search.view.recycler_view

class DashboardFragment : Fragment() {

    private lateinit var dashBoardViewModel: DashBoardViewModel
    var adapter: LocationAdapter = LocationAdapter()
    private  lateinit var locationAdapterFirestore: FirestoreRecyclerAdapter<Location, LocationViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dashBoardViewModel = ViewModelProvider(this).get(DashBoardViewModel::class.java)
        dashBoardViewModel.allLocations.observe(viewLifecycleOwner, Observer { locations ->
            locations?.let {
                adapter.populateListItems(it)
            }
        })
        //dashBoardViewModel.deleteAll()

//        val query = dashBoardViewModel.getQuery()
//        var allLocationsFromFirestore  = FirestoreRecyclerOptions.Builder<Location>()
//            .setQuery(query, Location::class.java)
//            .build()
//
//
//        locationAdapterFirestore = object : FirestoreRecyclerAdapter<Location, LocationViewHolder>(allLocationsFromFirestore) {
//
//            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
//                rv_clock_list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
////                val view = LayoutInflater.from(parent.context)
////                    .inflate(R.layout.item_clock, parent, false)
//                return LocationViewHolder(rv_clock_list)
//            }
//
//            override fun onBindViewHolder(@NonNull locationViewHolder: LocationViewHolder, position: Int, @NonNull location: Location) {
//                locationViewHolder.timezone.text = location.timezone
//            }
//
//        }



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_add.setOnClickListener {
            findNavController().navigate(DashboardFragmentDirections.dashboardToSearch())
        }

        rv_clock_list.adapter = adapter
        rv_clock_list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        val dividerItemDecoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        rv_clock_list.addItemDecoration(dividerItemDecoration)
        //rv_clock_list.isNestedScrollingEnabled = false
    }

}