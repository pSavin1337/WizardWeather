package com.lospollos.wizardweather.data.network

import android.annotation.SuppressLint
import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.lospollos.wizardweather.App
import com.lospollos.wizardweather.App.Companion.appComponent
import com.lospollos.wizardweather.Constants.dayCount
import com.lospollos.wizardweather.data.Result
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

object ImageLoader {

    private var i = 0

    private fun getIndex(): Int {
        return ++i
    }

    private fun saveImage(image: Bitmap): String? {
        val context = appComponent.getContext()
        var savedImagePath: String? = null
        val imageFileName = "PNG_weather_icon_${getIndex()}.png"
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, imageFileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
            }

            val resolver = context.contentResolver
            var uri: Uri? = null

            try {
                uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                    ?: throw IOException("Failed to create new MediaStore record.")

                resolver.openOutputStream(uri)?.use {
                    if (!image.compress(Bitmap.CompressFormat.PNG, 95, it))
                        throw IOException("Failed to save bitmap.")
                } ?: throw IOException("Failed to open output stream.")

                uri.toString()

            } catch (e: IOException) {

                uri?.let { orphanUri ->
                    resolver.delete(orphanUri, null, null)
                }

                throw e
            }
        } else {
            val storageDir = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    .toString() + "/weatherIcons"
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
            savedImagePath
        }

    }

    fun loadImage(result: Result.Success): ArrayList<String> {
        val context = appComponent.getContext()
        val iconWeatherList: ArrayList<String> = ArrayList(dayCount)
        for (i in 0 until dayCount)
            saveImage(
                Glide.with(context)
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
        val context = appComponent.getContext()
        val loadedIcons: ArrayList<Bitmap> = ArrayList(dayCount)
        for (link in links)
            loadedIcons.add(
                Glide.with(context)
                    .asBitmap()
                    .load(link)
                    .submit()
                    .get()
            )
        return loadedIcons
    }

}