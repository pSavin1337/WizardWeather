package com.lospollos.wizardweather.view

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lospollos.wizardweather.App.Companion.cities
import com.lospollos.wizardweather.App.Companion.context
import com.lospollos.wizardweather.R
import java.util.*

class MainRecyclerAdapter(
    val startWeatherCardsActivity: (cityName: String) -> Unit,
    val startWeatherService: (cityName: String) -> Unit
    ) : RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder>(), ItemTouchHelperAdapter{

    private var selectedPosition: Int = -1

    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var textView: TextView? = null
        var favoriteCityButton: RadioButton? = null
        init {
            textView = itemView.findViewById(R.id.cityTextView)
            favoriteCityButton = itemView.findViewById(R.id.radioButton)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.main_recyclerview_item, parent, false)
        return MainViewHolder(itemView)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.textView?.text = cities[position].cityName
        holder.textView?.setOnClickListener {
            startWeatherCardsActivity(cities[position].cityName)
        }

        if(position == selectedPosition && holder.favoriteCityButton?.isChecked == true) {
            holder.favoriteCityButton?.clearFocus()

            //stop
        } else {
            holder.favoriteCityButton?.isChecked = position == selectedPosition
        }

        holder.favoriteCityButton?.setOnClickListener {
            selectedPosition = holder.bindingAdapterPosition
            startWeatherService(cities[position].cityName)
            //TODO: fix stop service

            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = cities.size

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        if (fromPosition < toPosition)
            for (i in fromPosition until toPosition)
                Collections.swap(cities, i, i + 1)
        else
            for (i in fromPosition downTo toPosition + 1)
                Collections.swap(cities, i, i - 1)
        notifyItemMoved(fromPosition, toPosition)
        return true
    }
}