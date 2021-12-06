package com.lospollos.wizardweather.model.network

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.lospollos.wizardweather.App
import com.lospollos.wizardweather.Constants
import com.lospollos.wizardweather.model.network.retrofit.Result
import com.bumptech.glide.request.FutureTarget




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