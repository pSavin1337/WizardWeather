package com.lospollos.wizardweather.view

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lospollos.wizardweather.App.Companion.context
import com.lospollos.wizardweather.Constants.dayCount
import com.lospollos.wizardweather.R
import com.lospollos.wizardweather.data.network.WeatherResponseModel

class ViewPagerAdapter(
    private val cityName: String?,
    val onShareButtonClick: (weatherData: String) -> Unit,
    val onBackButtonClick: () -> Unit
) :
    RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder>() {

    lateinit var apiResponse: List<WeatherResponseModel>
    lateinit var icon: List<Bitmap>

    @SuppressLint("UseCompatLoadingForDrawables")
    class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView? = null
        var image: ImageView? = null
        var shareButton: Button? = null
        var diagramView: DiagramView? = null
        var backButton: Button? = null

        init {
            if (context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
                itemView.background = context
                    .getDrawable(R.drawable.background_rounded_landscape_right)
            else
                itemView.background = context
                    .getDrawable(R.drawable.background_rounded)
            image = itemView.findViewById(R.id.imageView)
            textView = itemView.findViewById(R.id.textView)
            shareButton = itemView.findViewById(R.id.shareButton)
            diagramView = itemView.findViewById(R.id.diagramView)
            backButton = itemView.findViewById(R.id.back_button)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder =
        PagerViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.cards_viewpager_item, parent, false)
        )


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        val temperature = apiResponse[position].temp
        val pressure = apiResponse[position].pressure
        val windSpeed = apiResponse[position].windSpeed
        val weatherDate = apiResponse[position].date
        holder.image?.setImageBitmap(icon[position])
        holder.textView?.text =
            if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
                "$cityName\n$temperature\n$pressure\n$windSpeed\n\n$weatherDate"
            else
                "$cityName\n$temperature"
        holder.diagramView?.humidity = apiResponse[position].humidity.split(" ")[1].toInt()
        holder.shareButton?.setOnClickListener {
            onShareButtonClick("$cityName\n$temperature\n$weatherDate")
        }
        holder.backButton?.setOnClickListener {
            onBackButtonClick()
        }
    }

    override fun getItemCount(): Int = dayCount

}