package com.lospollos.wizardweather.view.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.lospollos.wizardweather.R
import com.lospollos.wizardweather.view.activities.MainActivity
import kotlinx.coroutines.InternalCoroutinesApi

/**
 * Implementation of App Widget functionality.
 */
class WeatherAppWidget : AppWidgetProvider() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

}

@RequiresApi(Build.VERSION_CODES.M)
internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {

    PendingIntent.getActivity(
        /* context = */ context,
        /* requestCode = */  0,
        /* intent = */ Intent(context, MainActivity::class.java),
        /* flags = */ PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    var cityName = "Ульяновск"
    WidgetInfoLoader.loadWeather(cityName) {
        val loadedInfo = it
        val weatherInfo: String = if (loadedInfo != null) {
            loadedInfo[0].temp
        } else {
            cityName = ""
            WidgetInfoLoader.message.toString()
        }
        val widgetView = RemoteViews(
            context.packageName,
            R.layout.weather_app_widget
        )
        widgetView.setTextViewText(R.id.widget_city, cityName)
        widgetView.setTextViewText(R.id.widget_temp, weatherInfo.split(':')[1].drop(1))
        appWidgetManager.updateAppWidget(appWidgetId, widgetView)
    }
}