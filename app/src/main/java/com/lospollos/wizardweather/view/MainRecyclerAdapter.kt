package com.lospollos.wizardweather.view

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lospollos.wizardweather.R
import com.lospollos.wizardweather.view.activities.MainActivity
import com.lospollos.wizardweather.view.activities.WeatherCardsActivity
import java.util.*

class MainRecyclerAdapter(
    private var cities: List<String>,
    val startWeatherCardsActivity: (cityName: String) -> Unit
    ) : RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder>(), ItemTouchHelperAdapter{


    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var textView: TextView? = null
        init {
            textView = itemView.findViewById(R.id.cityTextView)
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.main_recyclerview_item, parent, false)
        return MainViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.textView?.text = cities[position]

        holder.textView?.setOnClickListener {
            startWeatherCardsActivity(cities[position])
        }
    }

    override fun getItemCount(): Int = cities.size

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(cities, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(cities, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
        return true
    }
}