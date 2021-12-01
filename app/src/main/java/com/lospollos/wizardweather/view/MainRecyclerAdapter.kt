package com.lospollos.wizardweather.view

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lospollos.wizardweather.R
import com.lospollos.wizardweather.view.activities.MainActivity
import com.lospollos.wizardweather.view.activities.WeatherCardsActivity

class MainRecyclerAdapter(
    private val cities: List<String>,
    val startWeatherCardsActivity: (cityName: String) -> Unit
    ) : RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder>(){


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
}