package com.jayashree.wordclock

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
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
import com.jayashree.wordclock.data.LocationContent
import com.jayashree.wordclock.data.LocationViewHolder
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*
import kotlinx.android.synthetic.main.fragment_search.view.recycler_view

class DashboardFragment : Fragment() {

    private lateinit var dashBoardViewModel: DashBoardViewModel
    var adapter: LocationAdapter = LocationAdapter { item: LocationContent -> itemClicked(item) }
    //private  lateinit var locationAdapterFirestore: FirestoreRecyclerAdapter<Location, LocationViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dashBoardViewModel = ViewModelProvider(this).get(DashBoardViewModel::class.java)
        dashBoardViewModel.allLocationContent.observe(viewLifecycleOwner, Observer { locations ->
            locations?.let {
                adapter.populateListItems(it)
            }
        })

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    //TODO: Move this to viewmodel
    fun itemClicked(item: LocationContent) {
        dashBoardViewModel.deleteLocation(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_add.setOnClickListener {
            findNavController().navigate(DashboardFragmentDirections.dashboardToSearch())
        }

        //TODO refactor this code so that everything is handled in the model and adapter seemlessly gets notified of the content changes
        tv_edit.setOnClickListener {
            Log.v("DEBUG", "Clicked")
            dashBoardViewModel.editable = !dashBoardViewModel.editable
            adapter.makeListEditable(dashBoardViewModel.editable)
        }

        rv_clock_list.adapter = adapter
        rv_clock_list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        val dividerItemDecoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        rv_clock_list.addItemDecoration(dividerItemDecoration)
    }

}