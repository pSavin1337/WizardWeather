package com.lospollos.wizardweather.view.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.lospollos.wizardweather.R
import com.lospollos.wizardweather.data.network.WeatherResponseModel
import com.lospollos.wizardweather.view.ViewPagerAdapter
import com.lospollos.wizardweather.view.activities.MainActivity
import com.lospollos.wizardweather.viewmodel.LoadWeatherViewModel

class WeatherCardsFragment : Fragment() {

    private lateinit var loadWeatherViewModel: LoadWeatherViewModel
    private lateinit var adapter: ViewPagerAdapter
    private lateinit var selectedCityName: String
    private lateinit var viewPager: ViewPager2
    private lateinit var progressBar: ProgressBar
    private lateinit var apiResponse: List<WeatherResponseModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather_cards, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadWeatherViewModel = ViewModelProvider(
            this,
            defaultViewModelProviderFactory
        )[LoadWeatherViewModel::class.java]
        selectedCityName = (activity as MainActivity).selectedCity
        progressBar = view.findViewById(R.id.progressBar)
        viewPager = view.findViewById(R.id.viewPager)
        getData()
        loadWeatherViewModel.loadWeather(selectedCityName)
    }

    private fun getData() {
        with(loadWeatherViewModel) {
            getWeatherItems().observe(viewLifecycleOwner) {
                apiResponse = it
            }
            getIsLoading().observe(viewLifecycleOwner) {
                if (it) {
                    viewPager.visibility = View.INVISIBLE
                    progressBar.visibility = View.VISIBLE
                } else {
                    progressBar.visibility = View.INVISIBLE
                    viewPager.visibility = View.VISIBLE
                }
            }
            getIcon().observe(viewLifecycleOwner) {
                adapter = ViewPagerAdapter(
                    selectedCityName,
                    loadWeatherViewModel::openShareMenu,
                    ::closeWeatherCardsFragment)
                viewPager.adapter = adapter
                adapter.icon = it
                adapter.apiResponse = apiResponse
            }
            getMessage().observe(viewLifecycleOwner) {
                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                (activity as MainActivity).closeWeatherCardsFragment()
            }
        }
    }

    private fun closeWeatherCardsFragment() = (activity as MainActivity).closeWeatherCardsFragment()

}