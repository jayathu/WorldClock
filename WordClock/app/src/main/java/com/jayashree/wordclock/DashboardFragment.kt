package com.jayashree.wordclock

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jayashree.wordclock.data.LocationContent
import kotlinx.android.synthetic.main.fragment_dashboard.*
import org.koin.android.viewmodel.ext.android.viewModel

class DashboardFragment : Fragment() {

    private val dashBoardViewModel: DashBoardViewModel by viewModel()

    private var adapter: LocationAdapter = LocationAdapter { item: LocationContent -> itemClicked(item) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    fun itemClicked(item: LocationContent) {
        dashBoardViewModel.deleteLocation(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dashBoardViewModel.allLocationContent.observe(viewLifecycleOwner, Observer { locations ->
            locations?.let {
                adapter.populateListItems(it)
            }
        })

        tv_add.setOnClickListener {
            findNavController().navigate(DashboardFragmentDirections.dashboardToSearch())
        }

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