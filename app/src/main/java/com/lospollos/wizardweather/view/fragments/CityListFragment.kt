package com.lospollos.wizardweather.view.fragments

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Data
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.lospollos.wizardweather.App
import com.lospollos.wizardweather.R
import com.lospollos.wizardweather.view.ItemTouchHelperCallback
import com.lospollos.wizardweather.view.MainRecyclerAdapter
import com.lospollos.wizardweather.view.activities.MainActivity
import com.lospollos.wizardweather.view.services.WeatherNotificationWorker
import java.util.concurrent.TimeUnit

class CityListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_city_list, container, false)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(App.context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            view.background = App.context
                .getDrawable(R.drawable.background_rounded_landscape_left)
        else
            view.background = App.context
                .getDrawable(R.drawable.background_rounded)
        val adapter = MainRecyclerAdapter ({

            /*Start WeatherCardFragment*/

            (activity as MainActivity).openWeatherCardsFragment(it)

        }, { it1 ->

            /*Start Service*/

            WorkManager.getInstance(App.context).cancelAllWorkByTag(
                "com.lospollos.wizardweather.view.services.WeatherNotificationWorker"
            )

            val workRequest: WorkRequest
            val dataWeather = Data.Builder().putString("cityName", it1).build()

            workRequest = PeriodicWorkRequest.Builder(
                WeatherNotificationWorker::class.java, 1, TimeUnit.HOURS
            ).setInputData(dataWeather).build()
            WorkManager.getInstance(App.context).enqueue(workRequest)

        })

        val recyclerView : RecyclerView = view.findViewById(R.id.mainRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        val callback: ItemTouchHelper.Callback = ItemTouchHelperCallback(adapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(recyclerView)

        recyclerView.adapter = adapter
    }

}