package com.lospollos.wizardweather.data

import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.bumptech.glide.Glide
import com.lospollos.wizardweather.App.Companion.context
import com.lospollos.wizardweather.Constants.dayCount
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*
import kotlin.collections.ArrayList

object ImageLoader {

    private var i = 0

    fun loadIcons(result: Result): Observable<ArrayList<Bitmap>> =
        when (result) {
            is Result.Success -> {
                val imageLinks = loadImage(result)
                Observable.just(loadImageFromStorage(imageLinks)).subscribeOn(Schedulers.io())
            }
            is Result.LoadedFromDB -> {
                val imageLinks: ArrayList<String> = ArrayList(dayCount)
                for (resultItem in result.items!!) {
                    imageLinks.add(resultItem.weatherIconUrl)
                }
                Observable.just(loadImageFromStorage(imageLinks)).subscribeOn(Schedulers.io())
            }
            is Result.Error -> {
                Observable.just(ArrayList<Bitmap>(0)).subscribeOn(Schedulers.io())
            }
        }

    private fun getIndex() = ++i

    private fun saveImage(image: Bitmap): String? {
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

    private fun loadImageFromStorage(links: ArrayList<String>): ArrayList<Bitmap> {
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