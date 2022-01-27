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
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Data
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.lospollos.wizardweather.R
import com.lospollos.wizardweather.view.City
import com.lospollos.wizardweather.view.ItemTouchHelperCallback
import com.lospollos.wizardweather.view.MainRecyclerAdapter
import com.lospollos.wizardweather.view.activities.MainActivity
import com.lospollos.wizardweather.view.services.WeatherNotificationWorker
import com.lospollos.wizardweather.viewmodel.CityListViewModel
import java.util.concurrent.TimeUnit

class CityListFragment : Fragment() {

    private lateinit var cityListViewModel: CityListViewModel
    private var cities: ArrayList<City> = ArrayList()

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
        if (context?.resources?.configuration?.orientation == Configuration.ORIENTATION_LANDSCAPE)
            view.background = context
                ?.getDrawable(R.drawable.background_rounded_landscape_left)
        else
            view.background = context
                ?.getDrawable(R.drawable.background_rounded)

        cityListViewModel = ViewModelProvider(
            this,
            defaultViewModelProviderFactory
        )[CityListViewModel::class.java]
        cityListViewModel.getCityList()
        cityListViewModel.getCityListLiveData().observe(viewLifecycleOwner) { cityList ->
            if (cityList?.isEmpty() == true) {
                resources.getStringArray(R.array.cities).forEach {
                    cities.add(City(it, false))
                }
            } else {
                cities = cityList as ArrayList<City>
            }
            val adapter = MainRecyclerAdapter(
                cities,
                ::openWeatherCardsFragment,
                cityListViewModel::openNotificationWorker,
                cityListViewModel::closeNotificationWorker,
                cityListViewModel::updateCityList
            )

            val recyclerView: RecyclerView = view.findViewById(R.id.mainRecyclerView)
            recyclerView.layoutManager = LinearLayoutManager(activity)

            val callback: ItemTouchHelper.Callback = ItemTouchHelperCallback(adapter)
            val touchHelper = ItemTouchHelper(callback)
            touchHelper.attachToRecyclerView(recyclerView)

            recyclerView.adapter = adapter
        }
    }

    private fun openWeatherCardsFragment(cityName: String) =
        (activity as MainActivity).openWeatherCardsFragment(cityName)

}