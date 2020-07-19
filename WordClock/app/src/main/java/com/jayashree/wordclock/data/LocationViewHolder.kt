package com.jayashree.wordclock.data

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_clock.view.*

class LocationViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    var timezone: TextView

    init {
        timezone = itemView.tv_city
    }
}