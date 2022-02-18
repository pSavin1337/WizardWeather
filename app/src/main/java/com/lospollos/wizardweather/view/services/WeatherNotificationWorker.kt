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
import com.lospollos.wizardweather.App.Companion.appComponent
import com.lospollos.wizardweather.R
import com.lospollos.wizardweather.dagger.WorkerModule
import com.lospollos.wizardweather.data.network.WeatherResponseModel
import com.lospollos.wizardweather.view.activities.MainActivity
import javax.inject.Inject

class WeatherNotificationWorker(var context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    private val CHANNEL_ID = "channelID"
    private val NOTIFY_ID = 101
    private val notificationManager = NotificationManagerCompat.from(context)

    @Inject
    lateinit var notificationLoader: NotificationInfoLoader

    @RequiresApi(Build.VERSION_CODES.M)
    override suspend fun doWork(): Result {
        return try {
            appComponent.inject(this)
            notificationManager.cancel(NOTIFY_ID)
            val cityName = inputData.getString("cityName")
            val loadedInfo: List<WeatherResponseModel>?

            if (cityName != null) {
                loadedInfo = notificationLoader.loadWeather(cityName)
            } else {
                return Result.failure()
            }
            val weatherInfo: String = if (loadedInfo != null) {
                loadedInfo[0].temp
            } else {
                notificationLoader.message.toString()
            }
            showNotification(weatherInfo, cityName)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
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