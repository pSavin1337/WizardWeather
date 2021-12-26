package com.lospollos.wizardweather.view.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import com.lospollos.wizardweather.Constants
import com.lospollos.wizardweather.R
import com.lospollos.wizardweather.model.network.BaseItemAdapterItem

class WeatherNotificationWorker(context: Context, params: WorkerParameters)
    : CoroutineWorker(context, params) {

    private val CHANNEL_ID = "channelID"
    private val NOTIFY_ID = 101
    private val notificationManager = NotificationManagerCompat.from(context)

    @RequiresApi(Build.VERSION_CODES.M)
    override suspend fun doWork(): Result {
        return try {
            notificationManager.cancel(NOTIFY_ID)
            val cityName = inputData.getString("cityName")
            val loadedInfo: List<List<BaseItemAdapterItem>>?

            if (cityName != null) {
                loadedInfo = NotificationInfoLoader.loadWeather(cityName)
            } else {
                return Result.failure()
            }
            val weatherInfo: String = if(loadedInfo != null) {
                (loadedInfo[0][Constants.TEMP] as BaseItemAdapterItem.Temperature).temp
            } else {
                NotificationInfoLoader.message.toString()
            }
            showNotification(weatherInfo, cityName)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun showNotification(weatherInfo: String, cityName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Weather Information"
            val descriptionText = "Information about temperature in your city"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            mChannel.description = descriptionText
            val notificationManager = applicationContext.getSystemService(NOTIFICATION_SERVICE)
                    as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_weather_notif)
            .setContentTitle(cityName)
            .setContentText(weatherInfo)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(NOTIFY_ID, notification)
    }

}