package com.jayashree.wordclock

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*
import kotlinx.android.synthetic.main.fragment_search.view.search


class SearchFragment : Fragment() {

    var locationAdapter = LocationAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)

//        val dividerItemDecoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
//        recycler_view.addItemDecoration(dividerItemDecoration)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.recycler_view.adapter = locationAdapter
        var locations = mutableListOf<Location>()

        locations.add(Location( "Africa/Abidjan"))
        locations.add(Location( "Asia"))
        locations.add(Location( "Dubai"))
        locations.add(Location( "Africa/Abidjan"))
        locations.add(Location( "Africa/Algeria"))
        locations.add(Location( "Africa/Bissau"))
        locations.add(Location( "Africa/Cairo"))
        locations.add(Location( "Africa/Casablanca"))
        locations.add(Location( "Asia/Chennai"))
        locations.add(Location( "Asia/Delhi"))
        locations.add(Location( "Asia/Mumbai"))
        locations.add(Location( "America/NewYork"))
        locations.add(Location( "America/California"))

        locationAdapter.populateListItems(locations)
        view.recycler_view.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                locationAdapter.filter.filter(newText)
                return false
            }
            override fun onQueryTextSubmit(query: String): Boolean {
                // task HERE
                return false
            }
        })
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
        val item = menu.findItem(R.id.action_search)
        val searchView: SearchView = item.actionView as SearchView
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                locationAdapter.filter.filter(newText)

                return false
            }
            override fun onQueryTextSubmit(query: String): Boolean {
                // task HERE
                return false
            }
        })

    }

}