package com.lospollos.wizardweather.view.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import com.lospollos.wizardweather.App.Companion.context
import com.lospollos.wizardweather.R
import com.lospollos.wizardweather.data.network.WeatherResponseModel
import com.lospollos.wizardweather.view.activities.MainActivity
import io.reactivex.Single

class WeatherNotificationWorker(context: Context, params: WorkerParameters) :
    RxWorker(context, params) {

    private val CHANNEL_ID = "channelID"
    private val NOTIFY_ID = 101
    private val notificationManager = NotificationManagerCompat.from(context)

    @SuppressLint("CheckResult")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun createWork(): Single<Result> {
        try {
            notificationManager.cancel(NOTIFY_ID)
            val cityName = inputData.getString("cityName")
            //val loadedInfo: List<WeatherResponseModel>?

            return if (cityName != null) {
                NotificationInfoLoader.loadWeather(cityName).subscribe {
                    val weatherInfo: String = if (it != null) {
                        it[0].temp
                    } else {
                        NotificationInfoLoader.message.toString()
                    }
                    showNotification(weatherInfo, cityName)
                }
                Single.create { Result.success() }
            } else {
                Single.create { Result.failure() }
            }
        } catch (e: Exception) {
            return Single.create { Result.failure() }
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun showNotification(weatherInfo: String, cityName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.notification_channel_name)
            val descriptionText = context.getString(R.string.notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            mChannel.description = descriptionText
            val notificationManager = applicationContext.getSystemService(NOTIFICATION_SERVICE)
                    as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }

        val pendingIntent = Intent(applicationContext, MainActivity::class.java).let {
            PendingIntent.getActivity(applicationContext, 0, it, FLAG_UPDATE_CURRENT)
        }

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_weather_notif)
            .setContentTitle(cityName)
            .setContentText(weatherInfo)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(NOTIFY_ID, notification)
    }

}