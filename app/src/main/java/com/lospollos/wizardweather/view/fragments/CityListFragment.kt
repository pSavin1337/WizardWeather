package com.lospollos.wizardweather.view.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lospollos.wizardweather.Constants
import com.lospollos.wizardweather.R
import com.lospollos.wizardweather.model.network.BaseItemAdapterItem
import com.lospollos.wizardweather.view.ItemTouchHelperCallback
import com.lospollos.wizardweather.view.MainRecyclerAdapter
import com.lospollos.wizardweather.view.services.WeatherNotificationService
import com.lospollos.wizardweather.viewmodel.ViewModel

class CityListFragment : Fragment() {
    private lateinit var viewModel: ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_city_list, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.elevation = 2f
        viewModel = ViewModelProvider(
            this,
            defaultViewModelProviderFactory)[ViewModel::class.java]

        val adapter = MainRecyclerAdapter ({
            /*Start WeatherCardActivity*/
            /*intent = Intent(this, WeatherCardsActivity::class.java)
            intent.putExtra("cityName", it)
            startActivity(intent)*/
            val arguments = Bundle()
            arguments.putString("cityName", it)
            activity?.supportFragmentManager?.beginTransaction()
                ?.add(R.id.weather_cards_fragment, WeatherCardsFragment::class.java, arguments)
                ?.commit()
        }, { it1 ->
            /*Start Service*/
            //var workRequest: WorkRequest
            val intentService = Intent(activity, WeatherNotificationService::class.java)
            //val dataWeather = Data.Builder().putString("cityName", it1)
            intentService.putExtra("cityName", it1)
            viewModel.loadWeather(it1)
            with(viewModel) {
                weatherItems.observe(viewLifecycleOwner) {
                    intentService
                        .putExtra(
                            "weatherInfo",
                            (it[0][Constants.TEMP] as BaseItemAdapterItem.Temperature).temp
                        )
                    activity?.stopService(intentService)
                    activity?.startService(intentService)
                    /*workRequest = PeriodicWorkRequest.Builder(
                        WeatherNotificationWorker::class.java, 1, TimeUnit.HOURS
                    ).setInputData(dataWeather.build()).build()
                    WorkManager.getInstance(this@MainActivity).enqueue(workRequest)*/
                }
                message.observe(viewLifecycleOwner) {
                    intentService.putExtra("weatherInfo", message.value)
                    activity?.stopService(intentService)
                    activity?.startService(intentService)
                    /*workRequest = PeriodicWorkRequest.Builder(
                            WeatherNotificationWorker::class.java, 1, TimeUnit.HOURS
                        ).setInputData(dataWeather.build()).build()
                        WorkManager.getInstance(this@MainActivity).enqueue(workRequest)*/
                }
                /*icon.observe(this@MainActivity) {
                    dataWeather.put("icon", it[0])
                    WorkManager.getInstance().enqueue(workResult)
                }*/
            }
        })

        val recyclerView : RecyclerView = view.findViewById(R.id.mainRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)


        val callback: ItemTouchHelper.Callback = ItemTouchHelperCallback(adapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(recyclerView)


        recyclerView.adapter = adapter
        //TODO("Пофиксить сохранение изменений d&d при обновлении экрана")
    }
}