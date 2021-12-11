package com.lospollos.wizardweather.view

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestBuilder
import com.lospollos.wizardweather.App
import com.lospollos.wizardweather.Constants
import com.lospollos.wizardweather.R
import com.lospollos.wizardweather.model.network.BaseItemAdapterItem

class ViewPagerAdapter(
    private val cityName: String?,
    val startShareDisplay: (weatherData: String) -> Unit,
    val toCityList: () -> Unit):
    RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder>() {

    lateinit var apiResponse: List<List<BaseItemAdapterItem>>
    lateinit var icon: List<Bitmap>

    class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView? = null
        var image: ImageView? = null
        var shareButton: Button? = null
        var diagramView: DiagramView? = null
        var backButton: Button? = null
        init {
            image = itemView.findViewById(R.id.imageView)
            textView = itemView.findViewById(R.id.textView)
            shareButton = itemView.findViewById(R.id.shareButton)
            diagramView = itemView.findViewById(R.id.diagramView)
            backButton = itemView.findViewById(R.id.back_button)
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
        val weatherDate = (apiResponse[position][Constants.DATE] as BaseItemAdapterItem.Date).date
        holder.image?.setImageBitmap(icon[position])
        holder.textView?.text = "$cityName\n$temperature\n$pressure\n$humidity\n$windSpeed\n" +
                "$windDegree\n$clouds\n$weatherId\n$weatherDesc\n$weatherDate"
        holder.diagramView?.humidity =
            (apiResponse[position][Constants.HUMID] as BaseItemAdapterItem.Humidity)
            .value.split(" ")[1].toInt()
        holder.shareButton?.setOnClickListener {
            startShareDisplay("$cityName\n$temperature\n$weatherDate")
        }
        holder.backButton?.setOnClickListener {
            toCityList()
        }
    }

    override fun getItemCount(): Int = daysCount

    companion object {
        const val daysCount : Int = 5
    }

}