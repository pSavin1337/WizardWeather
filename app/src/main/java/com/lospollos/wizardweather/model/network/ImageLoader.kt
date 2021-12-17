package com.lospollos.wizardweather.model.network

import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.lospollos.wizardweather.App
import com.lospollos.wizardweather.model.network.retrofit.Result


object ImageLoader {

    fun loadImage(result: Result.Success): ArrayList<Bitmap>{
        val iconWeatherList: ArrayList<Bitmap> = ArrayList(5)
        for(i in 0..4)
            iconWeatherList
                .add(
                    Glide.with(App.context)
                        .asBitmap()
                        .load((result.items[i][5] as BaseItemAdapterItem.Weather).iconUrl)
                        .submit()
                        .get()
                )

        return iconWeatherList
    }

}