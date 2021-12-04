package com.lospollos.wizardweather.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lospollos.wizardweather.R
import com.lospollos.wizardweather.model.BaseItemAdapterItem

class ViewPagerAdapter(private val cityName: String?):
    RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder>() {

    lateinit var apiResponse: List<List<BaseItemAdapterItem>>

    class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView? = null
        var image: ImageView? = null
        init {
            image = itemView.findViewById(R.id.imageView)
            textView = itemView.findViewById(R.id.textView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder =
        PagerViewHolder(LayoutInflater
            .from(parent.context)
            .inflate(R.layout.cards_viewpager_item, parent, false))

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        val temperature = (apiResponse[position][0] as BaseItemAdapterItem.Temperature).temp
        val pressure = (apiResponse[position][1] as BaseItemAdapterItem.Pressure).value
        val humidity = (apiResponse[position][2] as BaseItemAdapterItem.Humidity).value
        val windSpeed = (apiResponse[position][3] as BaseItemAdapterItem.Wind).speed
        val windDegree = (apiResponse[position][3] as BaseItemAdapterItem.Wind).degree
        val clouds = (apiResponse[position][4] as BaseItemAdapterItem.Clouds).value
        val weatherId = (apiResponse[position][5] as BaseItemAdapterItem.Weather).id
        val weatherDescription = (apiResponse[position][5] as BaseItemAdapterItem.Weather)
            .description
        val weatherIcon = (apiResponse[position][5] as BaseItemAdapterItem.Weather).iconUrl
        holder.image?.let { Glide.with(it.context).load(weatherIcon).into(it) }
        holder.textView?.text = "$cityName\n$temperature\n$pressure\n$humidity\n$windSpeed\n" +
                "$windDegree\n$clouds\n$weatherId\n$weatherDescription"
    }

    override fun getItemCount(): Int = daysCount

    companion object {
        const val daysCount : Int = 5
    }

}