package com.jayashree.wordclock

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_clock.view.*
import java.util.*
import kotlin.collections.ArrayList

class LocationAdapter : RecyclerView.Adapter<LocationAdapter.ViewHolder>(), Filterable {

    var locationList = mutableListOf<Location>()
    var locationListAll = mutableListOf<Location>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_clock, parent, false)
        return ViewHolder(v)

    }
    override fun getItemCount() = locationList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val location: Location = locationList[position]
        holder.tv_city.text = location.timezone
    }

    fun populateListItems(questions: List<Location>){
        this.locationList = questions.toMutableList()
        locationListAll = locationList.toMutableList()
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_city = itemView.tv_city
    }

    override fun getFilter():Filter {
        return myFilter
    }

    var myFilter: Filter = object : Filter() {
        //Automatic on background thread
        override fun performFiltering(charSequence: CharSequence): FilterResults {
            Log.v("DEBUG", charSequence.toString())
            val filteredList: MutableList<Location> = ArrayList()
            if (charSequence.isEmpty()) {
                filteredList.addAll(locationListAll)
            } else {
                filteredList.addAll(locationListAll.filter { it.timezone.toLowerCase().contains(charSequence.toString().toLowerCase())})
//                for (location in locationListAll) {
//                    if (location.timezone.contains(charSequence.toString().toLowerCase(Locale.ROOT))) {
//                        filteredList.add(location)
//                        Log.v("DEBUG", "Added " + location.timezone)
//                    }
//                }
            }
            val filterResults = FilterResults()
            filterResults.values = filteredList
            return filterResults
        }

        //Automatic on UI thread
        override fun publishResults(
            charSequence: CharSequence,
            filterResults: FilterResults
        ) {
            locationList.clear()
            locationList.addAll(filterResults.values as List<Location>)
            notifyDataSetChanged()
        }
    }
}