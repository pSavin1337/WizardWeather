package com.lospollos.wizardweather.view

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestBuilder
import com.lospollos.wizardweather.Constants
import com.lospollos.wizardweather.R
import com.lospollos.wizardweather.model.network.BaseItemAdapterItem

class ViewPagerAdapter(private val cityName: String?):
    RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder>() {

    lateinit var apiResponse: List<List<BaseItemAdapterItem>>
    lateinit var icon: List<Bitmap>

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
        val temperature = (apiResponse[position][Constants.TEMP] as BaseItemAdapterItem.Temperature)
            .temp
        val pressure = (apiResponse[position][Constants.PRES] as BaseItemAdapterItem.Pressure).value
        val humidity = (apiResponse[position][Constants.HUMID] as BaseItemAdapterItem.Humidity)
            .value
        val windSpeed = (apiResponse[position][Constants.WIND] as BaseItemAdapterItem.Wind).speed
        val windDegree = (apiResponse[position][Constants.WIND] as BaseItemAdapterItem.Wind).degree
        val clouds = (apiResponse[position][Constants.CLOUDS] as BaseItemAdapterItem.Clouds).value
        val weatherId = (apiResponse[position][Constants.WEATHER] as BaseItemAdapterItem.Weather).id
        val weatherDesc = (apiResponse[position][Constants.WEATHER] as BaseItemAdapterItem.Weather)
            .description
        holder.image?.setImageBitmap(icon[position])
        holder.textView?.text = "$cityName\n$temperature\n$pressure\n$humidity\n$windSpeed\n" +
                "$windDegree\n$clouds\n$weatherId\n$weatherDesc"
    }

    override fun getItemCount(): Int = daysCount

    companion object {
        const val daysCount : Int = 5
    }

}