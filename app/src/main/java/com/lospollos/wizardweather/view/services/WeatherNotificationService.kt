package com.lospollos.wizardweather.view.services

import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC
import androidx.core.app.NotificationManagerCompat
import com.lospollos.wizardweather.App.Companion.context
import com.lospollos.wizardweather.R

class WeatherNotificationService : Service() {

    private val CHANNEL_ID = "channelID"
    private val NOTIFY_ID = 101
    private val notificationManager = NotificationManagerCompat.from(context)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val cityName = intent?.extras?.getString("cityName")
        val weatherInfo = intent?.extras?.getString("weatherInfo")
        startForeground(NOTIFY_ID, createNotification(intent, cityName, weatherInfo))
        return super.onStartCommand(intent, flags, startId)

    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun createNotification(intent: Intent?, cityName: String?, weatherInfo: String?)
            : Notification {

        val intent1 = Intent(this, WeatherNotificationService::class.java)
        intent1.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent1, 0)
        val cancel = "Закрыть"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Weather notification"
            val descriptionText = "Information about temperature in your city"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            mChannel.description = descriptionText
            val notificationManager = applicationContext.getSystemService(NOTIFICATION_SERVICE)
                    as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }

        return NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(cityName)
            .setContentText(weatherInfo)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOngoing(false)
            .setVisibility(VISIBILITY_PUBLIC)
            //.addAction(android.R.drawable.ic_delete, cancel, paddingIntent)
            .setContentIntent(pendingIntent)
            .build()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

}