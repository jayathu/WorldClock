package com.jayashree.wordclock

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.jayashree.wordclock.data.Location
import com.jayashree.wordclock.data.LocationContent
import kotlinx.android.synthetic.main.fragment_dashboard.view.*
import kotlinx.android.synthetic.main.item_clock.view.*
import kotlin.collections.ArrayList

class LocationAdapter(val clickListener: (LocationContent) -> Unit) : RecyclerView.Adapter<LocationAdapter.ViewHolder>(), Filterable {

    var locationList = mutableListOf<LocationContent>()
    var locationListAll = mutableListOf<LocationContent>()

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
        (holder as ViewHolder).bind(locationList[position], clickListener )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_clock, parent, false)
        return ViewHolder(v)

    }
    override fun getItemCount() = locationList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val location: LocationContent = locationList[position]
        holder.tv_city.text = location.timezone
        holder.makeEditable(location.editable)
    }

    fun populateListItems(questions: List<LocationContent>){
        this.locationList = questions.toMutableList()
        locationListAll = locationList.toMutableList()
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_city = itemView.tv_city

        fun bind(item: LocationContent, clickListener: (LocationContent) -> Unit) {
            //makeEditable(false)
            itemView.setOnClickListener { clickListener(item) }
        }

        fun makeEditable(tf: Boolean){
            if(tf){
                itemView.iv_delete.visibility = View.VISIBLE
            }else{
                itemView.iv_delete.visibility = View.GONE
            }
        }
    }

    override fun getFilter():Filter {
        return myFilter
    }

    var myFilter: Filter = object : Filter() {
        //Automatic on background thread
        override fun performFiltering(charSequence: CharSequence): FilterResults {
            Log.v("DEBUG", charSequence.toString())
            val filteredList: MutableList<LocationContent> = ArrayList()
            if (charSequence.isEmpty()) {
                filteredList.addAll(locationListAll)
            } else {
                filteredList.addAll(locationListAll.filter { it.timezone.toLowerCase().contains(charSequence.toString().toLowerCase())})
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
            locationList.addAll(filterResults.values as List<LocationContent>)
            notifyDataSetChanged()
        }
    }

    fun makeListEditable(tf: Boolean) {
        locationList.forEach {
            it.editable = tf
        }
        notifyDataSetChanged()
    }
}