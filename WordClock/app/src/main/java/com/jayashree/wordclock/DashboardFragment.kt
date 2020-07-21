package com.jayashree.wordclock

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jayashree.wordclock.data.LocationContent
import kotlinx.android.synthetic.main.fragment_dashboard.*

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
            dashBoardViewModel.editable = !dashBoardViewModel.editable
            adapter.makeListEditable(dashBoardViewModel.editable)
        }

        rv_clock_list.adapter = adapter
        rv_clock_list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        val dividerItemDecoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(activity?.resources!!.getDrawable(R.drawable.divider_white))
        rv_clock_list.addItemDecoration(dividerItemDecoration)
    }

}