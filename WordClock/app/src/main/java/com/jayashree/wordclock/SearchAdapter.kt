package com.jayashree.wordclock

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_clock.view.*
import kotlin.collections.ArrayList

class SearchAdapter(private val clickListener: (String) -> Unit) : RecyclerView.Adapter<SearchAdapter.ViewHolder>(), Filterable {

    var locationList = mutableListOf<String>()
    var locationListAll = mutableListOf<String>()

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
        holder.bind(locationList[position], clickListener )
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
        return ViewHolder(v)

    }
    override fun getItemCount() = locationList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tv_city.text = locationList[position]
    }

    fun populateListItems(locations: List<String>){
        this.locationList = locations.toMutableList()
        locationListAll = locationList.toMutableList()
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_city = itemView.tv_city

        fun bind(item: String, clickListener: (String) -> Unit) {
            itemView.setOnClickListener { clickListener(item) }
        }
    }

    override fun getFilter():Filter {
        return myFilter
    }

    var myFilter: Filter = object : Filter() {

        override fun performFiltering(charSequence: CharSequence): FilterResults {
            val filteredList: MutableList<String> = ArrayList()
            if (charSequence.isEmpty()) {
                filteredList.addAll(locationListAll)
            } else {
                filteredList.addAll(locationListAll.filter { it.toLowerCase().contains(charSequence.toString().toLowerCase())})
            }
            val filterResults = FilterResults()
            filterResults.values = filteredList
            return filterResults
        }

        override fun publishResults(
            charSequence: CharSequence,
            filterResults: FilterResults
        ) {
            locationList.clear()
            locationList.addAll(filterResults.values as List<String>)
            notifyDataSetChanged()
        }
    }
}
