package com.lospollos.wizardweather.data.network

import android.graphics.Bitmap
import android.os.Environment
import com.bumptech.glide.Glide
import com.lospollos.wizardweather.App
import com.lospollos.wizardweather.Constants.dayCount
import com.lospollos.wizardweather.data.Result
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

object ImageLoader {

    var i = 0

    private fun getIndex(): Int {
        return ++i
    }

    private fun saveImage(image: Bitmap): String? {
        var savedImagePath: String? = null
        val imageFileName = "PNG_weather_icon_${getIndex()}.png"
        val storageDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .toString() + "/YOUR_FOLDER_NAME"
        )
        var success = true
        if (!storageDir.exists()) {
            success = storageDir.mkdirs()
        }
        if (success) {
            val imageFile = File(storageDir, imageFileName)
            savedImagePath = imageFile.absolutePath
            try {
                val fOut: OutputStream = FileOutputStream(imageFile)
                image.compress(Bitmap.CompressFormat.PNG, 100, fOut)
                fOut.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return savedImagePath
    }

    fun loadImage(result: Result.Success): ArrayList<String> {
        val iconWeatherList: ArrayList<String> = ArrayList(dayCount)
        for (i in 0 until dayCount)
            saveImage(
                Glide.with(App.context)
                    .asBitmap()
                    .load(result.items[i].weatherIconUrl)
                    .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                    .error(android.R.drawable.stat_notify_error)
                    .submit()
                    .get()
            )?.let {
                iconWeatherList
                    .add(
                        it
                    )
            }

        return iconWeatherList
    }

    fun loadImageFromStorage(links: ArrayList<String>): ArrayList<Bitmap> {
        val loadedIcons: ArrayList<Bitmap> = ArrayList(dayCount)
        for (link in links)
            loadedIcons.add(
                Glide.with(App.context)
                    .asBitmap()
                    .load(link)
                    .submit()
                    .get()
            )
        return loadedIcons
    }

}