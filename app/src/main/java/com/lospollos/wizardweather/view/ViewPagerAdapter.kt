package com.lospollos.wizardweather.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lospollos.wizardweather.R

class ViewPagerAdapter(private val cityName: String?):
    RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder>() {

    class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView? = null

        init {
            textView = itemView.findViewById(R.id.textView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder =
        PagerViewHolder(LayoutInflater
            .from(parent.context)
            .inflate(R.layout.cards_viewpager_item, parent, false))

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.textView?.text = "$cityName $position"
    }

    override fun getItemCount(): Int = daysCount

    companion object {
        const val daysCount : Int = 7
    }

}