package com.lospollos.wizardweather.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.lospollos.wizardweather.R
import java.util.*
import kotlin.collections.ArrayList

class MainRecyclerAdapter(
    private val cities: ArrayList<City>,
    val onCityClick: (cityName: String) -> Unit,
    val onFavoriteCityClick: (cityName: String) -> Unit,
    val onFavoriteCityRepeatedClick: () -> Unit,
    val onCityListChanged: (newCityList: ArrayList<City>) -> Unit
) : RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder>(), ItemTouchHelperAdapter {

    private var selectedPosition: Int = -1

    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
            onCityClick(cities[position].cityName)
        }

        holder.favoriteCityButton?.isChecked = cities[holder.bindingAdapterPosition].isFavorite

        holder.favoriteCityButton?.setOnClickListener {
            selectedPosition = holder.bindingAdapterPosition
            if (cities[selectedPosition].isFavorite) {
                cities.forEach {
                    it.isFavorite = false
                }
                onFavoriteCityRepeatedClick()
            } else {
                cities.forEach {
                    it.isFavorite = false
                }
                cities[position].isFavorite = true
                onFavoriteCityClick(cities[position].cityName)
            }
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
        onCityListChanged(cities)
        return true
    }

}